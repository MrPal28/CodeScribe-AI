package org.blogapplication.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
     private String id;
    private String firstname;
    private String lastName;
    private String email;
    private String phoneNumber;
}
