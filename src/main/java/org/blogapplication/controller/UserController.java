package org.blogapplication.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.blogapplication.dto.UserResponse;
import org.blogapplication.services.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final AuthenticationService authenticationService;

    /**
     * this end point return logged user details this end point need optimize and this end point make for testing
     * after few days this end point might be optimized
     * */
    @GetMapping
    public ResponseEntity<UserResponse> getLoggedUserDetails() {
        try {
            return ResponseEntity.ok(authenticationService.getLoggedUser());
        } catch (Exception e) {
            log.error("Error getting user details: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}