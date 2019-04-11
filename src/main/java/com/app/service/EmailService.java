package com.app.service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailService {

    private static final String emailAddress = "";
    private static final String emailPassword = "";


    public void send(String to, String title, String content) throws MessagingException
    {
        System.out.println("Sending email ...");
        Session session = createSession();
        MimeMessage mimeMessage = new MimeMessage(session);
        prepareEmailMessage(mimeMessage, to, title, content);
        Transport.send(mimeMessage);
        System.out.println("Email has been sent");
    }

    private void prepareEmailMessage(MimeMessage mimeMessage, String to, String title, String content) throws MessagingException {
        mimeMessage.setContent(content, "text/html; charset=utf-8");
        mimeMessage.setFrom();new InternetAddress(emailAddress);
        mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        mimeMessage.setSubject(title);
    }

    private Session createSession() {
        Properties props = new Properties();
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");

        return Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailAddress, emailPassword);
            }
        });
    }

}
