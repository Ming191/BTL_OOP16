package org.library.btl_oop16_library.services;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Properties;

public class EmailAPI {
    public static void sendEmail(Map<String, List<String[]>> groupedEmails) {
        final String senderEmail = ENV.getInstance().get("EMAIL");
        final String senderPassword = ENV.getInstance().get("EMAIL_PASSWORD");

        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        groupedEmails.forEach((recipient, books) -> {
            try {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(senderEmail));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
                message.setSubject("Library Due Date Reminder");
                String fullContent = getContent(books);
                // Specify UTF-8 encoding
                message.setContent(fullContent, "text/html; charset=UTF-8");
                Transport.send(message);
                System.out.println("Email sent to " + recipient);
            } catch (MessagingException e) {
                System.err.println(e.getMessage());
            }
        });
    }

    private static String getContent(List<String[]> books) {
        String header = """
        <div style="background-color: #f4f4f4; padding: 20px; text-align: center;">
            <h2 style="color: #333;">2-Minute Library</h2>
            <p style="color: #555;">Keeping you informed about your borrowed books</p>
        </div>
        """;

        StringBuilder body = getStringBuilder(books);

        String footer = """
        <div style="background-color: #f4f4f4; padding: 20px; text-align: center; font-size: 12px; color: #888;">
            <p>&copy; 2024 2-Minute Library | All rights reserved</p>
            <p>This is an automated email. Please do not reply to this address.</p>
        </div>
        """;

        return header + body + footer;
    }

    @NotNull
    private static StringBuilder getStringBuilder(List<String[]> books) {
        StringBuilder body = new StringBuilder();
        String userName = books.get(0)[0];
        body.append(String.format("""
        <div style="padding: 20px; font-family: Arial, sans-serif; color: #333;">
            <p>Dear %s,</p>
            <p>This is a reminder of your overdue books:</p>
            <ul>
        """, userName));

        for (String[] book : books) {
            String bookTitle = book[1];
            String dueDate = book[2];
            body.append(String.format("<li><strong>%s</strong> (Due Date: %s)</li>", bookTitle, dueDate));
        }

        body.append("""
            </ul>
            <p>Please ensure the books are returned on time to avoid any late fees.</p>
            <p>Thank you for using our library services!</p>
        </div>
        """);
        return body;
    }
}
