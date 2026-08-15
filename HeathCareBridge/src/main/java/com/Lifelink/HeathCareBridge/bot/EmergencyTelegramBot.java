package com.Lifelink.HeathCareBridge.bot;

import com.Lifelink.HeathCareBridge.model.ResourceType;
import com.Lifelink.HeathCareBridge.payload.AiResponse;
import com.Lifelink.HeathCareBridge.projection.FacilityLocationProjection;
import com.Lifelink.HeathCareBridge.repository.ResourceRepository;
import com.Lifelink.HeathCareBridge.service.AiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Profile("!test")
@ConditionalOnProperty(
        name="telegram.bot.enabled",
        havingValue = "true",
        matchIfMissing = false
)
public class EmergencyTelegramBot extends TelegramLongPollingBot {

    @Value("${telegram.bot.username}")
    private String botUsername;

    private final AiService aiService;
    private final ResourceRepository resourceRepository;
    private final Map<Long, UserSession> userSessions = new ConcurrentHashMap<>();

    public EmergencyTelegramBot(
            @Value("${telegram.bot.token}") String botToken,
            AiService aiService,
            ResourceRepository resourceRepository) {
        super(botToken);
        this.aiService = aiService;
        this.resourceRepository = resourceRepository;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage()) return;
        long chatId = update.getMessage().getChatId();

        if (update.getMessage().hasText() && update.getMessage().getText().equals("/start")) {
            userSessions.put(chatId, new UserSession("WAITING_FOR_LOCATION"));
            requestLocation(chatId);
            return;
        }

        UserSession session = userSessions.get(chatId);
        if (session == null) return;

        if (update.getMessage().hasLocation() && "WAITING_FOR_LOCATION".equals(session.state)) {
            session.latitude = update.getMessage().getLocation().getLatitude();
            session.longitude = update.getMessage().getLocation().getLongitude();
            session.state = "WAITING_FOR_DETAILS"; // Updated state to handle both text and photo

            sendMessage(chatId, "📍 Location saved! Now, please upload a photo of the situation, OR just type a description of the emergency.");
        }
        else if ("WAITING_FOR_DETAILS".equals(session.state)) {

            // Check if it's either a photo OR text
            if (update.getMessage().hasPhoto() || update.getMessage().hasText()) {
                sendMessage(chatId, "⏳ AI is analyzing the emergency and finding nearby resources. Please hold on...");

                try {
                    String description = null;
                    MultipartFile multipartFile = null;

                    // If it's a photo, download it and grab the caption
                    if (update.getMessage().hasPhoto()) {
                        String fileId = update.getMessage().getPhoto().get(update.getMessage().getPhoto().size() - 1).getFileId();
                        description = update.getMessage().getCaption();

                        GetFile getFile = new GetFile(fileId);
                        org.telegram.telegrambots.meta.api.objects.File tgFile = execute(getFile);
                        File downloadedImage = downloadFile(tgFile);

                        multipartFile = new TelegramMultipartFile(downloadedImage);
                    }
                    // If it's pure text, just grab the text
                    else if (update.getMessage().hasText()) {
                        description = update.getMessage().getText();
                    }

                    // Call AI Service (it handles null multipartFile gracefully!)
                    AiResponse triageData = aiService.analyzeEmergency(description, multipartFile);

                    List<FacilityLocationProjection> nearestFacilities;
                    if (triageData.requiredResources().contains(ResourceType.BLOOD)
                            && triageData.bloodGroup() != null
                            && triageData.bloodComponent() != null) {

                        List<String> requiredResources = triageData.requiredResources().stream().map(Enum::name).toList();
                        nearestFacilities = resourceRepository.findTop10NearestBloodFacilityLocations(
                                requiredResources, session.longitude, session.latitude,
                                triageData.bloodGroup().name(), triageData.bloodComponent().name()
                        );
                    } else {
                        List<String> requiredResources = triageData.requiredResources().stream().map(Enum::name).toList();
                        nearestFacilities = resourceRepository.findTop10NearestFacilityLocations(
                                requiredResources, session.longitude, session.latitude
                        );
                    }

                    String finalMessage = formatTelegramResponse(triageData, nearestFacilities);
                    sendMessage(chatId, finalMessage);

                } catch (Exception e) {
                    e.printStackTrace();
                    sendMessage(chatId, "❌ Sorry, an error occurred while processing your request: " + e.getMessage());
                } finally {
                    userSessions.remove(chatId);
                }
            } else {
                sendMessage(chatId, "⚠️ Please send either a photo or a text description of the emergency to proceed.");
            }
        }
    }

    private String formatTelegramResponse(AiResponse triage, List<FacilityLocationProjection> facilities) {
        StringBuilder sb = new StringBuilder();
        sb.append("🚨 **AI Triage Assessment** 🚨\n\n");
        sb.append("⚠️ **Severity Level:** ").append(triage.severityLevel()).append("\n");
        sb.append("🩺 **Clinical Reasoning:** ").append(triage.clinicalReasoning()).append("\n");
        sb.append("⚙️ **Required Resources:** ").append(triage.requiredResources()).append("\n");

        if (triage.bloodGroup() != null) {
            sb.append("🩸 **Blood Needed:** ").append(triage.bloodGroup()).append(" (").append(triage.bloodComponent()).
                    append(")\n");
        }

        sb.append("\n🏥 **Nearest Available Facilities:**\n");
        if (facilities == null || facilities.isEmpty()) {
            sb.append("⚠️ No nearby facilities found with the required resources.");
        } else {
            for (FacilityLocationProjection f : facilities) {
                sb.append("• **").append(f.getFacilityName()).append("**\n");

                double dist = f.getDistance();
                if (dist > 1000) {
                    sb.append("   📍 Distance: ").append(String.format("%.2f", dist / 1000)).append(" km\n\n");
                } else {
                    sb.append("   📍 Distance: ").append(Math.round(dist)).append(" meters\n\n");
                }
            }
        }
        return sb.toString();
    }

    private void requestLocation(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Hello! I am LifeLink AI. Please tap the button below to share your GPS location so we can find help near you.");

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(true);

        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();

        KeyboardButton locationButton = new KeyboardButton("📍 Share My Location");
        locationButton.setRequestLocation(true);
        row.add(locationButton);

        keyboard.add(row);
        keyboardMarkup.setKeyboard(keyboard);
        message.setReplyMarkup(keyboardMarkup);

        try { execute(message); } catch (TelegramApiException e) { e.printStackTrace(); }
    }

    private void sendMessage(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        try { execute(message); } catch (TelegramApiException e) { e.printStackTrace(); }
    }

    private static class UserSession {
        String state;
        Double latitude;
        Double longitude;
        UserSession(String state) { this.state = state; }
    }

    private static class TelegramMultipartFile implements MultipartFile {
        private final byte[] content;
        private final String name;

        public TelegramMultipartFile(File file) throws IOException {
            this.content = Files.readAllBytes(file.toPath());
            this.name = file.getName();
        }

        @Override public String getName() { return name; }
        @Override public String getOriginalFilename() { return name; }
        @Override public String getContentType() { return "image/jpeg"; }
        @Override public boolean isEmpty() { return content.length == 0; }
        @Override public long getSize() { return content.length; }
        @Override public byte[] getBytes() { return content; }
        @Override public InputStream getInputStream() { return new java.io.ByteArrayInputStream(content); }
        @Override public void transferTo(File dest) throws IOException,
                IllegalStateException { Files.write(dest.toPath(), content); }
    }
}