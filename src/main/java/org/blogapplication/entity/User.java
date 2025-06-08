package org.blogapplication.entity;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "user_data")
@NoArgsConstructor
@Getter
@Setter
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
    private String pho;

    @NonNull
    private String password;

    @Indexed(unique = true)
    private String imgUrl;
}
