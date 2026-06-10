package org.example.service.student;

public interface OtpSender {
    void sendPasswordChangeOtp(String recipientEmail, String otpCode);
}
