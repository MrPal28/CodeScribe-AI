package org.blogapplication.services.Implementations;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.blogapplication.dto.OtpEntry;
import org.blogapplication.services.OtpService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;


import jakarta.annotation.PostConstruct;

@Service
public class OtpServiceImpl implements OtpService {
  
   @Value("${twilio.accountSid}")
    private String accountSid;

    @Value("${twilio.authToken}")
    private String authToken;

    @Value("${twilio.fromPhone}")
    private String fromPhone;


    private final Map<String, OtpEntry> otpCache = new HashMap<>();
    private final Random random = new Random();


  @Override
  @PostConstruct
  public void initTwilio() {
    Twilio.init(accountSid, authToken);
  }

  @Override
  public String generateOtp() {
   int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
  }

  @Override
  public void generateAndSendOtp(String toPhone) {
    String otp = generateOtp();
    long expiry = System.currentTimeMillis() + 5 * 60 * 1000; // 5 minutes
    otpCache.put(toPhone, new OtpEntry(otp, expiry));

   String messageBody = String.format(
    "📝 Welcome to CodeScribe AI.io!\nYour One-Time Password (OTP) is: *%s*\n" +
        "Use this code to verify your account and continue your journey with us.\n" +
        "⏳ This OTP is valid for a limited time. Please do not share it with anyone.\n\n" +
        "Thanks for joining CodeScribe AI.io — where ideas meet intelligence! 🚀",
    otp);

        Message message = Message.creator(
                new PhoneNumber("whatsapp:+91" + toPhone),
                new PhoneNumber(fromPhone),
                messageBody
        ).create();

        System.out.println("Sent OTP: " + otp + " | SID: " + message.getSid());
  }

  @Override
  public boolean verifyOtp(String toPhone, String otp) {
      OtpEntry entry = otpCache.get(toPhone);
    if (entry == null) return false;

    long now = System.currentTimeMillis();
    if (now > entry.getExpiryTimeMillis()) {
        otpCache.remove(toPhone); // clean up expired
        return false; // OTP expired
    }
    
    return entry.getOtp().equals(otp);
  }

  @Override
  public void clearOtp(String phone) {
    otpCache.remove(phone);
  }
}
