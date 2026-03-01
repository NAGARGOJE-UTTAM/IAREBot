package com.ai.telegram;

import sibApi.*;
import sibModel.*;
import java.util.*;

public class EmailHelper {

    private static final String API_KEY = System.getenv("BREVO_API_KEY");
    private static final String FROM_EMAIL = System.getenv("FROM_EMAIL");
    private static final String TO_EMAIL = System.getenv("TO_EMAIL");

    public static void sendQueryEmail(Long userId, String userName, String body) {

        if (API_KEY == null || FROM_EMAIL == null || TO_EMAIL == null) {
            System.out.println("❌ Brevo ENV variables NOT configured.");
            return;
        }

        try {
            ApiClient client = Configuration.getDefaultApiClient();
            client.setApiKey(API_KEY);

            TransactionalEmailsApi api = new TransactionalEmailsApi();

            SendSmtpEmailSender sender = new SendSmtpEmailSender();
            sender.setEmail(FROM_EMAIL);
            sender.setName("IARE Bot");

            SendSmtpEmailTo to = new SendSmtpEmailTo();
            to.setEmail(TO_EMAIL);

            // Build email content with user details
            String emailContent =
                    "New Query Received from IARE Bot\n\n" +
                    "User ID: " + userId + "\n" +
                    "User Name: " + userName + "\n\n" +
                    "Message:\n" + body;

            SendSmtpEmail email = new SendSmtpEmail();
            email.setSender(sender);
            email.setTo(Collections.singletonList(to));
            email.setSubject("New Query from IARE Bot");
            email.setTextContent(emailContent);

            api.sendTransacEmail(email);

            System.out.println("✅ Brevo Email Sent Successfully");

        } catch (Exception e) {
            System.out.println("❌ Brevo Email Failed");
            e.printStackTrace();
        }
    }
}
