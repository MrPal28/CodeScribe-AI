package org.blogapplication.services;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.blogapplication.api.response.PromptRequest;
import org.blogapplication.dto.BlogDTO;
import org.blogapplication.entity.Blog;
import org.blogapplication.model.ContentCheckResponse;
import org.blogapplication.repository.BlogRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BlogServiceImpl implements BlogService {


    private final BlogRepository blogRepository;


    @Override
    public void saveNewBlog(BlogDTO requestContent) throws RuntimeException {
        PromptRequest promptRequest = new PromptRequest(requestContent.getContent());
        ContentCheckerService apiResponse = new ContentCheckerService();
        ContentCheckResponse response = apiResponse.sendPrompt(promptRequest);

        if (response.isInappropriate()) {
            throw new RuntimeException(response.getModeration_result());
        }

        Blog blog = new Blog();
        blog.setContent(requestContent.getContent());

        blogRepository.save(blog);
    }
}
