package org.blogapplication.entity;


import lombok.Data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "user_data")
public class User {

    /*
        all filed will be change latter this filed is use for testing
    **/

    @Id
    String id;
    
    String name;
    
    String email;
    
    String password;
    
    String phonenumber;
    
    String role;

}
