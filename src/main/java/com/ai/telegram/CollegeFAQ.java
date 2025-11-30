package com.ai.telegram;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.json.JSONArray;
import org.json.JSONObject;

public class CollegeFAQ {

    private static final JSONArray faqList;

    static {
        JSONArray temp = new JSONArray();
        try {
            InputStream is = CollegeFAQ.class.getClassLoader().getResourceAsStream("college_faq.json");
            if (is != null) {
                String text = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                JSONObject root = new JSONObject(text);
                temp = root.getJSONArray("faq");
                System.out.println("FAQ Loaded: " + temp.length() + " items");
            } else {
                System.out.println("ERROR: college_faq.json not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        faqList = temp;
    }

    public static String findAnswer(String userText) {
        if (userText == null || userText.isBlank()) return null;

        userText = userText.toLowerCase();

        for (int i = 0; i < faqList.length(); i++) {
            JSONObject obj = faqList.getJSONObject(i);

            JSONArray keywords = obj.getJSONArray("questions");
            for (int j = 0; j < keywords.length(); j++) {
                String key = keywords.getString(j).toLowerCase();
                if (userText.contains(key)) {
                    return obj.getString("answer");
                }
            }
        }
        return null;
    }
}
