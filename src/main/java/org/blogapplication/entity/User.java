package org.blogapplication.entity;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "user_data")
@NoArgsConstructor
public class User {

    @Id
    private String id;

    @NonNull
    private String firstname;

    @NonNull
    private String lastname;

    @Indexed(unique = true)
    @NonNull
    private String email;

    @Indexed(unique = true)
    @NonNull
    private String phoneNumber;

    private String role;

    @NonNull
    private String password;

    @Indexed(unique = true)
    private String imgUrl;
}
