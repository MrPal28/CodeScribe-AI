package org.blogapplication.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlogResponse {
    private String id;
    private String title;
    private String content;
    private String authorName;
    private String status;
    private boolean isApproved;
    private LocalDateTime createdDate;
}
