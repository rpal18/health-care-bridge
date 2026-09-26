package com.Lifelink.HeathCareBridge.bot;

import com.Lifelink.HeathCareBridge.ai.model.EmergencyResponse;
import com.Lifelink.HeathCareBridge.ai.model.FacilityResult;
import com.Lifelink.HeathCareBridge.ai.service.GeminiMultiModelService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
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
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//@Component
//@Profile("!test")
public class EmergencyTelegramBot extends TelegramLongPollingBot {

    @Value("${telegram.bot.username}")
    private String botUsername;

    private final GeminiMultiModelService geminiMultimodalService;
    private final Map<Long, UserSession> userSessions = new ConcurrentHashMap<>();

    public EmergencyTelegramBot(
            @Value("${telegram.bot.token}") String botToken,
            GeminiMultiModelService geminiMultimodalService) {
        super(botToken);
        this.geminiMultimodalService = geminiMultimodalService;
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
            session.state = "WAITING_FOR_DETAILS";
            sendMessage(chatId, "📍 Location saved! Now tell me what's happening — send a voice note, a photo, or just type a description of the emergency.");
            return;
        }

        if ("WAITING_FOR_DETAILS".equals(session.state)) {
            boolean hasVoice = update.getMessage().hasVoice();
            boolean hasPhoto = update.getMessage().hasPhoto();
            boolean hasText = update.getMessage().hasText();

            if (!hasVoice && !hasPhoto && !hasText) {
                sendMessage(chatId, "⚠️ Please send a voice note, a photo, or a text description of the emergency to proceed.");
                return;
            }

            sendMessage(chatId, "⏳ AI is analyzing the emergency and finding nearby resources. Please hold on...");

            try {
                String description = null;
                byte[] audioBytes = null, imageBytes = null;
                String audioMimeType = null, imageMimeType = null;

                if (hasVoice) {
                    File downloaded = downloadTelegramFile(update.getMessage().getVoice().getFileId());
                    audioBytes = Files.readAllBytes(downloaded.toPath());
                    audioMimeType = "audio/ogg"; // Telegram voice notes are Ogg/Opus — verify against Gemini's supported audio types if this errors
                    description = update.getMessage().getCaption();
                } else if (hasPhoto) {
                    String fileId = update.getMessage().getPhoto()
                            .get(update.getMessage().getPhoto().size() - 1).getFileId();
                    File downloaded = downloadTelegramFile(fileId);
                    imageBytes = Files.readAllBytes(downloaded.toPath());
                    imageMimeType = "image/jpeg";
                    description = update.getMessage().getCaption();
                } else {
                    description = update.getMessage().getText();
                }

                EmergencyResponse response = geminiMultimodalService
                        .handleEmergency(description, audioBytes, audioMimeType, imageBytes, imageMimeType,
                                session.latitude, session.longitude)
                        .block(); // safe here — runs on the bot library's own polling thread, not a shared web request thread

                sendMessage(chatId, formatTelegramResponse(response));

            } catch (Exception e) {
                e.printStackTrace();
                sendMessage(chatId, "❌ Sorry, an error occurred while processing your request: " + escapeHtml(e.getMessage()));
            } finally {
                userSessions.remove(chatId);
            }
        }
    }

    private File downloadTelegramFile(String fileId) throws TelegramApiException, IOException {
        org.telegram.telegrambots.meta.api.objects.File tgFile = execute(new GetFile(fileId));
        return downloadFile(tgFile);
    }

    private String formatTelegramResponse(EmergencyResponse response) {
        var assessment = response.assessment();
        StringBuilder sb = new StringBuilder();

        sb.append("🚨 <b>AI Triage Assessment</b> 🚨\n\n");
        sb.append("⚠️ <b>Severity:</b> ").append(escapeHtml(assessment.severityLevel())).append("\n");
        sb.append("🩺 <b>Summary:</b> ").append(escapeHtml(assessment.summary())).append("\n");

        sb.append("⚙️ <b>Required Resources:</b>\n");
        assessment.resources().forEach(r -> {
            sb.append("   • ").append(escapeHtml(r.resourceType()))
                    .append(" x").append(r.quantity())
                    .append(" — ").append(escapeHtml(r.reason()));
            if (r.bloodGroup() != null) {
                sb.append(" (").append(escapeHtml(r.bloodGroup()))
                        .append(", ").append(escapeHtml(r.bloodComponent())).append(")");
            }
            sb.append("\n");
        });

        sb.append("\n🏥 <b>Nearest Facilities:</b>\n");
        appendFacilities(sb, response.generalFacilities());

        if (!response.bloodFacilities().isEmpty()) {
            sb.append("\n🩸 <b>Nearest Blood Facilities:</b>\n");
            appendFacilities(sb, response.bloodFacilities());
        }

        if (response.generalFacilities().isEmpty() && response.bloodFacilities().isEmpty()) {
            sb.append("⚠️ <b>No nearby facilities found with the required resources.</b> Please contact emergency services directly.\n");
        }

        return sb.toString();
    }

    private void appendFacilities(StringBuilder sb, List<FacilityResult> facilities) {
        for (FacilityResult f : facilities) {
            sb.append("• <b>").append(escapeHtml(f.facilityName())).append("</b>\n");
            if (f.distance() != null) {
                double dist = f.distance();
                sb.append("   📍 ").append(dist > 1000
                        ? String.format("%.2f km", dist / 1000)
                        : Math.round(dist) + " m").append("\n");
            }
            if (f.mapLink() != null) {
                // mapLink is our own generated URL from numeric lat/long, not AI/user text — safe to place directly in href
                sb.append("   📍 <a href=\"").append(f.mapLink()).append("\">Open in Maps</a>\n");
            }
            sb.append("\n");
        }
    }

    private String escapeHtml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;");
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
        message.setParseMode("HTML");
        try { execute(message); } catch (TelegramApiException e) { e.printStackTrace(); }
    }

    private static class UserSession {
        String state;
        Double latitude;
        Double longitude;
        UserSession(String state) { this.state = state; }
    }
}