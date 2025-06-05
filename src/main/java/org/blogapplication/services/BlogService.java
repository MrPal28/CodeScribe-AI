package org.blogapplication.services;


import org.blogapplication.dto.BlogDTO;

public interface BlogService {

    void saveNewBlog(BlogDTO requestContent) throws RuntimeException;
}
