package org.blogapplication.entity;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "user_data")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    @Id
    private String id;

    private String username;

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

    @NonNull
    private String password;
    
    private String role = "ROLE_USER";

    @Indexed(unique = true)
    private String imgUrl;

    @DBRef // this annotation using for make a reference of another collections
    @JsonProperty("Blog_data")
    @Builder.Default
    private List<BlogEntries> blogEntries = new ArrayList<>();
}
