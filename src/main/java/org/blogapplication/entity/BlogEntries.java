package org.blogapplication.entity;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "Blog_data")
public class BlogEntries {

    @Id
    private String id;

    @NonNull
    private String title;

    @NonNull
    private String content;


    @JsonProperty("create_at")
    private LocalDateTime createdDate;
}
