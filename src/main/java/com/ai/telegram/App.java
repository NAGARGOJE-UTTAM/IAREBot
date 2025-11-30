package com.ai.telegram;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class App {
    public static void main(String[] args) {
        try {
            TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
            api.registerBot(new AIBot());
            System.out.println("IARE Bot Running 24x7...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
