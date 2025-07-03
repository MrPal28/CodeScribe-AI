package org.blogapplication.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
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

    @GetMapping("/")
    public String getString(){
        return "OK";
    }

    @PostMapping("/send-otp")
    public ResponseEntity<String> sendOtp(@RequestBody OtpRequest request) {
        otpService.generateAndSendOtp(request.getPhoneNumber());
        return ResponseEntity.ok("OTP sent to " + request.getPhoneNumber());
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> register(@RequestBody UserRequest request) {
        try {
            boolean isOtpValid = otpService.verifyOtp(request.getPhoneNumber(), request.getOtp());

            if (!isOtpValid) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid OTP");
            }

            UserResponse response = authenticationService.registerNewUser(request);
            otpService.clearOtp(request.getPhoneNumber());

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest request, HttpServletResponse response) {
        try {
            AuthenticationResponse authResponse = authenticationService.login(request);

            Cookie jwtCookie = new Cookie("jwt", authResponse.getToken());
            jwtCookie.setHttpOnly(true);
            jwtCookie.setPath("/");
            jwtCookie.setMaxAge(24 * 60 * 60);
            response.addCookie(jwtCookie);
            log.info("Log in successful");
            return ResponseEntity.ok(authResponse);
        } catch (Exception e) {
            log.error("Login failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
