package com.ai.telegram;

import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.*;

import java.io.IOException;

public class EmailHelper {

    public static void sendQueryEmail(String body) {

        // Read correct environment variables
        String apiKey = System.getenv("SENDGRID_API_KEY");
        String fromEmail = System.getenv("FROM_EMAIL");
        String toEmail = System.getenv("TO_EMAIL");

        if (apiKey == null || fromEmail == null || toEmail == null) {
            System.out.println("❌ SendGrid ENV variables NOT configured.");
            System.out.println("apiKey = " + apiKey);
            System.out.println("fromEmail = " + fromEmail);
            System.out.println("toEmail = " + toEmail);
            return;
        }

        Email from = new Email(fromEmail);
        Email to = new Email(toEmail);

        String subject = "New Query from IARE Bot";
        Content content = new Content("text/plain", body);

        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(apiKey);
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = sg.api(request);

            System.out.println("SendGrid Response: " + response.getStatusCode());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
