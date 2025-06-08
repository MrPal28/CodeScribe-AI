package org.blogapplication.services;


import org.blogapplication.dto.BlogRequest;

public interface BlogService {

    void saveNewBlog(BlogRequest requestContent) throws RuntimeException;
}
