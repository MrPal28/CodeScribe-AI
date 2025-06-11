package org.blogapplication.entity;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "Blog_data")
public class BlogEntries {

    // all fields are change later
    @Id
    String id;
    String content;
}
