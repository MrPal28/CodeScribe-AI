package org.blogapplication.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequest {

    private String id;
    private String firstname;
    private String lastname;
    private String email;
    private String phoneNumber;
    private String password;
    private String otp;
}
