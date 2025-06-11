package org.blogapplication.services;


import org.blogapplication.dto.BlogRequest;
import org.blogapplication.dto.BlogResponse;

public interface BlogService {

    BlogResponse createBlog(BlogRequest blogRequest);
}
