package org.blogapplication.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.blogapplication.dto.AuthenticationRequest;
import org.blogapplication.dto.AuthenticationResponse;
import org.blogapplication.dto.OtpRequest;
import org.blogapplication.dto.UserRequest;
import org.blogapplication.dto.UserResponse;
import org.blogapplication.services.AuthenticationService;
import org.blogapplication.services.OtpService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/public")
@RequiredArgsConstructor
public class PublicController {
  private final AuthenticationService authenticationService;
  private final OtpService otpService;

  @PostMapping("/send-otp")
  public ResponseEntity<String> sendOtp(@RequestBody OtpRequest request) {
    otpService.generateAndSendOtp(request.getPhoneNumber());
    return ResponseEntity.ok("OTP sent to " + request.getPhoneNumber());
  }

  @PostMapping("/register")
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<?> register(@RequestBody UserRequest request) {
    boolean isOtpValid = otpService.verifyOtp(request.getPhoneNumber(), request.getOtp());

    if (!isOtpValid) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid OTP");
    }

    UserResponse response = authenticationService.registerNewUser(request);
    otpService.clearOtp(request.getPhoneNumber());

    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @PostMapping("/login")
  public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest request) {
    AuthenticationResponse response = authenticationService.login(request);
    return ResponseEntity.ok(response);
  }

}
