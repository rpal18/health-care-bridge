package com.Lifelink.HeathCareBridge.AppConfig;

import com.Lifelink.HeathCareBridge.bot.EmergencyTelegramBot;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
@Profile("!test")
public class TelegramConfig {
    @Bean
    @ConditionalOnBean(EmergencyTelegramBot.class)
    public TelegramBotsApi telegramBotsApi(EmergencyTelegramBot nearbyHelpBot) throws TelegramApiException {
        TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
        api.registerBot(nearbyHelpBot);
        return api;
    }
}
