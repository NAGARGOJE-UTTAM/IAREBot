package com.ai.telegram;

import java.util.*;

public class CollegeFAQs {

    private static final List<FAQ> faqs = new ArrayList<>();

    static {
        faqs.add(new FAQ(
                Arrays.asList("attendance", "attendance requirement", "minimum attendance"),
                "The required attendance is 75% minimum."
        ));

        faqs.add(new FAQ(
                Arrays.asList("bonafide", "apply bonafide", "bonafide certificate"),
                "Login to Samvidha → Requisitions → Certificate Request → Bonafide and choose the reason."
        ));

        faqs.add(new FAQ(
                Arrays.asList("computer science fees", "cse fees", "cse fee"),
                "The CSE fee is ₹101,000 per year."
        ));

        faqs.add(new FAQ(
                Arrays.asList("who is creator", "creator", "founder"),
                "The creators are NAGARGOJE UTTAM & CH. NAGARAJU."
        ));

        faqs.add(new FAQ(
                Arrays.asList("hod", "who is the hod of cse"),
                "Ms. G. Marry Swaranalatha is the HOD of CSE."
        ));

        faqs.add(new FAQ(
                Arrays.asList("who is the principal of IARE", "principal"),
                "Dr. L. V. Narasimha Prasad is the Principal of IARE."
        ));
    }

    public static String getAnswer(String userMessage) {
        userMessage = userMessage.toLowerCase();

        for (FAQ faq : faqs) {
            for (String q : faq.questions) {
                if (userMessage.contains(q.toLowerCase())) {
                    return faq.answer;
                }
            }
        }
        return null; // no answer found
    }

    private static class FAQ {
        List<String> questions;
        String answer;

        FAQ(List<String> questions, String answer) {
            this.questions = questions;
            this.answer = answer;
        }
    }
}
