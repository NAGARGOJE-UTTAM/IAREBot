package com.ai.telegram;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;

public class EmailHelper {

    private static final String SMTP_USERNAME = System.getenv("SMTP_USERNAME");
    private static final String SMTP_PASSWORD = System.getenv("SMTP_PASSWORD");
    private static final String FROM_EMAIL = System.getenv("FROM_EMAIL");
    private static final String TO_EMAIL = System.getenv("TO_EMAIL");

    public static void sendQueryEmail(Long userId, String userName, String body) {

        if (SMTP_USERNAME == null || SMTP_PASSWORD == null ||
                FROM_EMAIL == null || TO_EMAIL == null) {

            System.out.println("❌ SMTP ENV variables NOT configured.");
            return;
        }

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp-relay.brevo.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(SMTP_USERNAME, SMTP_PASSWORD);
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(TO_EMAIL));
            message.setSubject("New Query from IARE Bot");

            String emailContent =
                    "New Query Received from IARE Bot\n\n" +
                            "User ID: " + userId + "\n" +
                            "User Name: " + userName + "\n\n" +
                            "Message:\n" + body;

            message.setText(emailContent);

            Transport.send(message);

            System.out.println("✅ Email Sent Successfully via SMTP");

        } catch (MessagingException e) {
            System.out.println("❌ Email Failed");
            e.printStackTrace();
        }
    }
}