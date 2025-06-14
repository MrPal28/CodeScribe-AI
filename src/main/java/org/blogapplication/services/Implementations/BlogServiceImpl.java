package org.blogapplication.services.Implementations;

import lombok.RequiredArgsConstructor;
import org.blogapplication.model.PromptRequest;
import org.blogapplication.dto.BlogRequest;
import org.blogapplication.dto.BlogResponse;
import org.blogapplication.entity.BlogEntries;
import org.blogapplication.model.ContentCheckResponse;
import org.blogapplication.repository.BlogRepository;
import org.blogapplication.repository.UserRepository;
import org.blogapplication.services.BlogService;
import org.blogapplication.services.UserService;
import org.blogapplication.util.UserUtilityService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BlogServiceImpl implements BlogService {
    private final UserService userService;
    private final UserUtilityService userUtilityService;
    private final BlogRepository blogRepository;
    private final UserRepository userRepository;
    private final ContentCheckerService contentCheckerService;


    @Transactional
    @Override
    public BlogResponse createBlog(BlogRequest blogRequest) {
        PromptRequest promptRequest = new PromptRequest(blogRequest.getContent());
        ContentCheckResponse response = contentCheckerService.sendPrompt(promptRequest);

        if (response.isInappropriate()) {
            throw new RuntimeException(response.getModeration_result());
        }

        BlogEntries newBlogEntry = convertToEntity(blogRequest);
        newBlogEntry = blogRepository.save(newBlogEntry);
        return convertToResponse(newBlogEntry);
    }

    @Override
    public List<BlogResponse> getAllBlogs() {
        List<BlogResponse> all = Collections.singletonList(convertToResponse((BlogEntries) blogRepository.findAll()));
        return List.of((BlogResponse) all);
    }


    private BlogEntries convertToEntity(BlogRequest request) {
        return BlogEntries.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .createdDate(LocalDateTime.now())
                .build();
    }

    private BlogResponse convertToResponse(BlogEntries blogEntry) {
        return BlogResponse.builder()
                .id(blogEntry.getId())
                .title(blogEntry.getTitle())
                .content(blogEntry.getContent())
                .createdDate(blogEntry.getCreatedDate())
                .build();
    }
}