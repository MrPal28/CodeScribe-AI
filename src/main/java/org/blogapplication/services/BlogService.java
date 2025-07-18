package org.blogapplication.services;


import org.blogapplication.dto.BlogRequest;
import org.blogapplication.dto.BlogResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BlogService {

    BlogResponse createBlog(BlogRequest blogRequest, MultipartFile file) ;

    List<BlogResponse> getAllBlogs();

    Void deleteBlog(String id);
}
