package com.ai.telegram;

import java.io.InputStream;
import java.util.*;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class AIBot extends TelegramLongPollingBot {

    @Override
    public String getBotUsername() {
        return "IARECOLLEGE_bot";
    }

    @Override
    public String getBotToken() {
        return System.getenv("TELEGRAM_TOKEN"); // Secure token
    }

    @Override
    public void onUpdateReceived(Update update) {

        try {
            if (update.hasMessage() && update.getMessage().hasText()) {

                long chatId = update.getMessage().getChatId();
                String msg = update.getMessage().getText();

                Long userId = update.getMessage().getFrom().getId();
                String userName = update.getMessage().getFrom().getFirstName();

                if (msg.equalsIgnoreCase("/start")) {
                    sendWelcome(chatId);
                    return;
                }

                // 1️⃣ Check FAQ first
                String answer = CollegeFAQ.findAnswer(msg);

                if (answer != null) {
                    sendText(chatId, answer);
                } else {

                    sendText(chatId,
                            "We will update you soon. Your query has been recorded.");

                    // 2️⃣ Send Email with full details
                    EmailHelper.sendQueryEmail(userId, userName, msg);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendWelcome(long chatId) {

        try {

            // 🔹 Send Logo from resources
            InputStream is = getClass().getClassLoader()
                    .getResourceAsStream("logo.jpeg");

            if (is != null) {
                SendPhoto photo = new SendPhoto();
                photo.setChatId(String.valueOf(chatId));
                photo.setPhoto(new InputFile(is, "logo.jpeg"));
                photo.setCaption("⭐ WELCOME TO IARE ASSISTANT ⭐");
                execute(photo);
            }

            // 🔹 Create Menu Buttons
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

            SendMessage menuMsg = new SendMessage();
            menuMsg.setChatId(String.valueOf(chatId));
            menuMsg.setText("📌 Please choose an option below:");
            menuMsg.setReplyMarkup(menu);

            execute(menuMsg);

            // 🔹 Instruction message
            SendMessage instruction = new SendMessage();
            instruction.setChatId(String.valueOf(chatId));
            instruction.setText("💬 Type your query here to get the solution.");
            execute(instruction);

        } catch (Exception e) {
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