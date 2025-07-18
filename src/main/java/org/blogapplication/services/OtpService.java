package org.blogapplication.services;

public interface OtpService {
  String generateOtp();
  void generateAndSendOtp(String toPhone);
  boolean verifyOtp(String toPhone, String otp);
  void clearOtp(String phone);
}
