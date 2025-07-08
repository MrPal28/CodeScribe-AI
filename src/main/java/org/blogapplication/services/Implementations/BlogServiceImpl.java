package org.blogapplication.services.Implementations;

import lombok.RequiredArgsConstructor;
import org.blogapplication.entity.User;
import org.blogapplication.model.PromptRequest;
import org.blogapplication.constants.BlogStatus;
import org.blogapplication.dto.BlogRequest;
import org.blogapplication.dto.BlogResponse;
import org.blogapplication.entity.BlogEntries;
import org.blogapplication.model.ContentCheckResponse;
import org.blogapplication.repository.BlogRepository;
import org.blogapplication.repository.UserRepository;
import org.blogapplication.services.BlogService;
import org.blogapplication.services.UserService;
import org.blogapplication.util.UserUtilityService;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BlogServiceImpl implements BlogService {
    private final BlogRepository blogRepository;
    private final ContentCheckerService contentCheckerService;
    private final UserUtilityService userUtilityService;
    private final UserRepository userRepository;
    private final UserService userService;

    @Transactional
    @Override
    public BlogResponse createBlog(BlogRequest blogRequest) {
        PromptRequest promptRequest = new PromptRequest(blogRequest.getContent());
        ContentCheckResponse response = contentCheckerService.sendPrompt(promptRequest);
        User loggedUser = userRepository.findByEmail(userUtilityService.getLoggedUserName()).orElseThrow(() -> new RuntimeException("User not found"));

        if (response.isInappropriate()) {
            throw new RuntimeException(response.getModeration_result());
        }

        BlogEntries newBlogEntry = convertToEntity(blogRequest);
        newBlogEntry.setAiApproved(true);
        newBlogEntry.setStatus(BlogStatus.APPROVED);
        newBlogEntry.setAuthorName(userUtilityService.getLoggedUserName());
        newBlogEntry = blogRepository.save(newBlogEntry);

        // save in user
        loggedUser.getBlogEntries().add(newBlogEntry);
        userService.saveUser(loggedUser);

        return convertToResponse(newBlogEntry);
    }

    @Override
    public List<BlogResponse> getAllBlogs() {
        List<BlogResponse> all = Collections.singletonList(convertToResponse((BlogEntries) blogRepository.findAll()));
        return List.of((BlogResponse) all);
    }


    @Override
    public void deleteBlog(String blogId) {
        BlogEntries blogEntry = blogRepository.findById(blogId)
                .orElseThrow(() -> new RuntimeException("Blog not found with id: " + blogId));
        blogRepository.delete(blogEntry);
    }

    @Override
    public BlogResponse getBlogById(String id) {
        BlogEntries blog = blogRepository.findById(id).orElseThrow(() -> new RuntimeException("Blog not found with id: " + id));
        return convertToResponse(blog);
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
                .authorName(blogEntry.getAuthorName())
                .isApproved(blogEntry.isAiApproved())
                .status(blogEntry.getStatus().toString())
                .createdDate(blogEntry.getCreatedDate())
                .build();
    }
}