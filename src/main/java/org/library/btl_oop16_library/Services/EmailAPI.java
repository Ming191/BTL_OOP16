package org.library.btl_oop16_library.Services;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Properties;

public class EmailAPI {
    public static void sendEmail(List<String[]> overdueEmails) {
        final String senderEmail = "2minlibrary@gmail.com";
        final String senderPassword = "xspg gfqk dxhd ddwh";

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

        try {
            Message message = new MimeMessage(session);
            String recipient = overdueEmails.getFirst()[1];
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            message.setSubject("Library Due Date Reminder");
            String fullContent = getContent(overdueEmails);
            message.setContent(fullContent, "text/html");
            Transport.send(message);
            System.out.println("Email sent to " + recipient);

        } catch (MessagingException e) {
            System.err.println(e.getMessage());
        }
    }

    private static String getContent(List<String[]> overdueEmails) {
        String header = """
            <div style="background-color: #f4f4f4; padding: 20px; text-align: center;">
                <h2 style="color: #333;">2-Minute Library</h2>
                <p style="color: #555;">Keeping you informed about your borrowed books</p>
            </div>
            """;

        StringBuilder body = getStringBuilder(overdueEmails);


        String footer = """
            <div style="background-color: #f4f4f4; padding: 20px; text-align: center; font-size: 12px; color: #888;">
                <p>&copy; 2024 2-Minute Library | All rights reserved</p>
                <p>This is an automated email. Please do not reply to this address.</p>
            </div>
            """;

        return header + body + footer;
    }

    @NotNull
    private static StringBuilder getStringBuilder(List<String[]> overdueEmails) {
        StringBuilder body = new StringBuilder();
        for (String[] email : overdueEmails) {
            String userName = email[2];
            String bookTitle = email[3];
            String dueDate = email[4];
            String s = String.format("""
            <div style="padding: 20px; font-family: Arial, sans-serif; color: #333;">
                <p>Dear %s,</p>
                <p>This is a reminder that the book titled <strong>%s</strong> is due for return on <strong>%s</strong>.</p>
                <p>Please ensure the book is returned on time to avoid any late fees.</p>
                <p>Thank you for using our library services!</p>
            </div>
            """, userName, bookTitle, dueDate);
            body.append(s);
        }
        return body;
    }

}