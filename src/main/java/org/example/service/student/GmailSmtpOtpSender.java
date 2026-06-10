package org.example.service.student;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.AuthenticationFailedException;

import java.nio.charset.StandardCharsets;
import java.util.Properties;

public final class GmailSmtpOtpSender implements OtpSender {
    private static final String DEFAULT_HOST = "smtp.gmail.com";
    private static final int DEFAULT_PORT = 587;

    private final String host;
    private final int port;
    private final String username;
    private final String password;
    private final String fromAddress;

    public GmailSmtpOtpSender(String host, int port, String username, String password, String fromAddress) {
        this.host = requireText(host, "host");
        this.port = port;
        if (port <= 0) {
            throw new IllegalArgumentException("SMTP port must be positive.");
        }
        this.username = username == null ? null : username.trim();
        this.password = password == null ? null : password.trim();
        this.fromAddress = fromAddress == null ? null : fromAddress.trim();
    }

    public static GmailSmtpOtpSender fromEnvironment() {
        String username = requireConfiguredSetting("course.smtp.username", "COURSE_SMTP_USERNAME");
        String password = requireConfiguredSetting("course.smtp.password", "COURSE_SMTP_PASSWORD");
        String fromAddress = readSetting("course.smtp.from", "COURSE_SMTP_FROM", username);
        return new GmailSmtpOtpSender(
                readSetting("course.smtp.host", "COURSE_SMTP_HOST", DEFAULT_HOST),
                Integer.parseInt(readSetting("course.smtp.port", "COURSE_SMTP_PORT", String.valueOf(DEFAULT_PORT))),
                username,
                password,
                fromAddress);
    }

    @Override
    public void sendPasswordChangeOtp(String recipientEmail, String otpCode) {
        String normalizedRecipient = requireText(recipientEmail, "recipientEmail");
        String normalizedOtp = requireText(otpCode, "otpCode");
        if (username == null || username.isEmpty()) {
            throw new IllegalStateException("COURSE_SMTP_USERNAME chua duoc cau hinh.");
        }
        if (password == null || password.isEmpty()) {
            throw new IllegalStateException("COURSE_SMTP_PASSWORD chua duoc cau hinh. Gia tri nay phai la Gmail App Password, khong phai mat khau dang nhap thuong.");
        }
        if (fromAddress == null || fromAddress.isEmpty()) {
            throw new IllegalStateException("COURSE_SMTP_FROM hoac COURSE_SMTP_USERNAME chua duoc cau hinh.");
        }
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", String.valueOf(port));
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        Session session =
                Session.getInstance(
                        properties,
                        new Authenticator() {
                            @Override
                            protected PasswordAuthentication getPasswordAuthentication() {
                                return new PasswordAuthentication(username, password);
                            }
                        });
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromAddress));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(normalizedRecipient));
            message.setSubject("CourseManagement OTP");
            message.setText(buildMessage(normalizedOtp), StandardCharsets.UTF_8.name());
            Transport.send(message);
        } catch (AuthenticationFailedException exception) {
            throw new IllegalStateException(
                    "Gmail SMTP khong xac thuc duoc. Hay kiem tra App Password, khong dung mat khau dang nhap thuong.",
                    exception);
        } catch (MessagingException exception) {
            throw new IllegalStateException(
                    "Khong the gui OTP qua Gmail SMTP: " + exception.getMessage(),
                    exception);
        }
    }

    private String buildMessage(String otpCode) {
        return "Ma OTP cua ban la: " + otpCode + "\n"
                + "Ma nay co hieu luc trong 5 phut.\n"
                + "Neu ban khong yeu cau doi mat khau, vui long bo qua email nay.";
    }

    private static String requireText(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " khong duoc de trong.");
        }
        return value.trim();
    }

    private static String readSetting(String systemProperty, String envKey, String defaultValue) {
        String systemValue = System.getProperty(systemProperty);
        if (systemValue != null && !systemValue.trim().isEmpty()) {
            return systemValue.trim();
        }

        String envValue = System.getenv(envKey);
        if (envValue != null && !envValue.trim().isEmpty()) {
            return envValue.trim();
        }

        return defaultValue;
    }

    private static String requireConfiguredSetting(String systemProperty, String envKey) {
        String value = readSetting(systemProperty, envKey, null);
        if (value == null || value.isEmpty()) {
            throw new IllegalStateException(
                    "Missing SMTP configuration: "
                            + systemProperty
                            + " or "
                            + envKey
                            + ". With Gmail SMTP, the password property must be an App Password.");
        }
        return value;
    }
}
