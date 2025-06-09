package org.blogapplication.services;

public interface OtpService {
  void initTwilio();
  String generateOtp();
  void generateAndSendOtp(String toPhone);
  boolean verifyOtp(String toPhone, String otp);
  void clearOtp(String phone);
}
