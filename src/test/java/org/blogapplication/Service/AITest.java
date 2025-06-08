package org.blogapplication.Service;

import org.blogapplication.dto.BlogRequest;
import org.blogapplication.services.BlogService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AITest {

    @Autowired
    private BlogService blogService;

    @Test
    void serviceTest() {
        BlogRequest  blogRequest = new BlogRequest();
        blogRequest.setContent("Hello World bitch");

        blogService.saveNewBlog(blogRequest);
    }


}
