package org.blogapplication.services.Implementations;

import lombok.RequiredArgsConstructor;
import org.blogapplication.entity.ImageEntity;
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
import org.blogapplication.services.ImageService;
import org.blogapplication.services.UserService;
import org.blogapplication.util.UserUtilityService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BlogServiceImpl implements BlogService {
    private final BlogRepository blogRepository;
    private final ContentCheckerService contentCheckerService;
    private final UserUtilityService userUtilityService;
    private final UserRepository userRepository;
    private final UserService userService;
    private final ImageService imageService;


    @Transactional
    @Override
    public BlogResponse createBlog(BlogRequest blogRequest, MultipartFile file) throws RuntimeException {
        PromptRequest promptRequest = new PromptRequest(blogRequest.getContent());
        ContentCheckResponse response = contentCheckerService.sendPrompt(promptRequest);
        User loggedUser = userRepository.findByEmail(userUtilityService.getLoggedUserName()).orElseThrow(() -> new RuntimeException("User not found"));

        if (response.isInappropriate()) {
            throw new RuntimeException(response.getModeration_result());
        }

        Map<String, String> imageData = new HashMap<>();

        if (file != null && !file.isEmpty()) {
            try {
                ImageEntity image = imageService.uploadImage(file);
                imageData.put("id", image.getPublicId());
                imageData.put("url", image.getImageUrl());
            } catch (IOException e) {
                throw new RuntimeException("Image upload failed: " + e.getMessage());
            }
        }

        BlogEntries newBlogEntry = convertToEntity(blogRequest, loggedUser, imageData);
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
    public Void deleteBlog(String blogId) {

        BlogEntries blogEntry = blogRepository.findById(blogId)
                .orElseThrow(() -> new RuntimeException("Blog not found with id: " + blogId));
        blogRepository.delete(blogEntry);
        return null;
    }

    private Map<String, String> upload(MultipartFile file) throws IOException {
        Map<String, String> response = new HashMap<>();
        ImageEntity image = imageService.uploadImage(file);
        response.put("id", image.getPublicId());
        response.put("url", image.getImageUrl());
        return response;
    }


    private BlogEntries convertToEntity(BlogRequest request, User user, Map<String, String> imageData) {
        return BlogEntries.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .authorName(user.getFirstname() + " " + user.getLastname())
                .image(imageData)
                .isAiApproved(true)
                .status(BlogStatus.APPROVED)
                .createdDate(LocalDateTime.now())
                .build();
    }

    private BlogResponse convertToResponse(BlogEntries blogEntry) {
        return BlogResponse.builder()
                .id(blogEntry.getId())
                .title(blogEntry.getTitle())
                .content(blogEntry.getContent())
                .author(blogEntry.getAuthorName())
                .status(blogEntry.getStatus())
                .createdDate(blogEntry.getCreatedDate())
                .build();
    }
}