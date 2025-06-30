package org.blogapplication.services;


import org.blogapplication.dto.BlogRequest;
import org.blogapplication.dto.BlogResponse;

import java.util.List;

public interface BlogService {

    BlogResponse createBlog(BlogRequest blogRequest);

    List<BlogResponse> getAllBlogs();

    Void deleteBlog(String id);
}
