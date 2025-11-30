package com.ai.telegram;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.io.File;
import java.util.*;

public class AIBot extends TelegramLongPollingBot {

    // FIXED
    @Override
    public String getBotUsername() {
        return "IARECOLLEGE_bot";  // your bot username
    }

    // FIXED
    @Override
    public String getBotToken() {
        return "8451991977:AAGBAT5zkQKckm43U6FSH8dr-GWjzg84PRE"; // your real token
    }

    @Override
    public void clearWebhook() {}

    @Override
    public void onUpdateReceived(Update update) {

        try {
            if (update.hasMessage() && update.getMessage().hasText()) {

                long chatId = update.getMessage().getChatId();
                String msg = update.getMessage().getText();

                if (msg.equalsIgnoreCase("/start")) {
                    sendWelcome(chatId);
                    return;
                }

                // 1. CHECK JSON FIRST
                String answer = CollegeFAQ.findAnswer(msg);

                if (answer != null) {
                    sendText(chatId, answer);
                } else {
                    // 2. NO ANSWER → AUTO REPLY + SEND EMAIL
                    sendText(chatId, "We will update you soon. Your query has been recorded.");

                    EmailHelper.sendQueryEmail(
                            "User Query: " + msg + "\nChat ID: " + chatId
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendWelcome(long chatId) {

        SendPhoto photo = new SendPhoto();
        photo.setChatId(chatId);

        File logo = new File("C:\\Users\\nagar\\AIChatBot\\src\\main\\resources\\logo.jpeg");

        if (logo.exists()) {
            photo.setPhoto(new InputFile(logo));
        }

        photo.setCaption("⭐ WELCOME TO IARE ASSISTANT ⭐\n\nSelect an option below:");

        try {
            execute(photo);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

        // MENU GRID
        InlineKeyboardMarkup menu = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        rows.add(Arrays.asList(
                button("Fees", "fees"),
                button("Attendance", "attendance"),
                button("Bonafide", "bonafide")
        ));

        rows.add(Arrays.asList(
                button("Courses", "courses"),
                button("Hostel", "hostel"),
                button("Contact", "contact")
        ));

        menu.setKeyboard(rows);

        SendMessage msg = new SendMessage();
        msg.setChatId(chatId);
        msg.setText("Choose an option:");
        msg.setReplyMarkup(menu);

        try {
            execute(msg);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private InlineKeyboardButton button(String text, String data) {
        InlineKeyboardButton btn = new InlineKeyboardButton();
        btn.setText(text);
        btn.setCallbackData(data);
        return btn;
    }

    private void sendText(long chatId, String text) {
        SendMessage msg = new SendMessage(String.valueOf(chatId), text);
        try {
            execute(msg);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
