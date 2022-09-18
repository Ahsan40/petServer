package utils;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import main.Configs;

import java.util.Properties;

public class MyMail {
    private static final String HOST = "smtp.gmail.com";
    private static final String PORT = "465";

    public static void main(String[] args) {
        System.out.println("preparing to send message ...");
        String message = "Hello , Dear, this is message for security check . ";
        String subject = "Test : Confirmation";
        String to = "nobody.ahsan@gmail.com";
        String from = "noreplypet202@gmail.com";

        sendEmail(message, subject, to, from);
    }

    //this is responsible to send email.
    public static void sendEmail(String message, String subject, String to, String from) {
        //get the system properties
        Properties properties = System.getProperties();

        //host config
//        properties.put("mail.smtp.starttls.enable","true");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.host", HOST);
        properties.put("mail.smtp.port", PORT);

        //Step 1: to get the session object.
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(Configs.serverEmail, Configs.serverPass);
            }
        });
//        session.setDebug(true);

        MimeMessage m = new MimeMessage(session);
        try {
            // from email
            m.setFrom(from);

            // adding recipient to message
            m.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            // adding subject to message
            m.setSubject(subject);

            // adding text to message
            m.setText(message);

            // send

            // Step 3 : send the message using Transport class
            Transport.send(m);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
