package org.example.service;

public interface OtpSender {
    void sendPasswordChangeOtp(String recipientEmail, String otpCode);
}
