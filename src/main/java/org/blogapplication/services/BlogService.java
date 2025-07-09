package org.blogapplication.services;


import org.blogapplication.dto.BlogRequest;
import org.blogapplication.dto.BlogResponse;
import org.springframework.http.HttpStatusCode;

import java.util.List;

public interface BlogService {

    List<BlogResponse> getAllLoggedUseBlogs();

    BlogResponse createBlog(BlogRequest blogRequest);

    List<BlogResponse> getAllBlogs();

    void deleteBlog(String id);

    BlogResponse getBlogById(String id);

    BlogResponse update(String id, BlogRequest request);
}
