package org.blogapplication.services.Implementations;

import lombok.RequiredArgsConstructor;
import org.blogapplication.api.response.PromptRequest;
import org.blogapplication.dto.BlogRequest;
import org.blogapplication.entity.BlogEntries;
import org.blogapplication.model.ContentCheckResponse;
import org.blogapplication.repository.BlogRepository;
import org.blogapplication.services.BlogService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BlogServiceImpl implements BlogService {


    private final BlogRepository blogRepository;
    private final ContentCheckerService contentCheckerService;


    @Override
    public void saveNewBlog(BlogRequest requestContent) throws RuntimeException {
        PromptRequest promptRequest = new PromptRequest(requestContent.getContent());
        ContentCheckResponse response = contentCheckerService.sendPrompt(promptRequest);

        if (response.isInappropriate()) {
            throw new RuntimeException(response.getModeration_result());
        }

        BlogEntries blogEntries = new BlogEntries();
        blogEntries.setContent(requestContent.getContent());

        blogRepository.save(blogEntries);
    }
}
