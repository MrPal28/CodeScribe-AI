package org.blogapplication.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.blogapplication.dto.BlogRequest;
import org.blogapplication.dto.BlogResponse;
import org.blogapplication.services.BlogService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/blog")
@RequiredArgsConstructor
public class BlogController {

    private final BlogService blogService;

    @PostMapping("/add-new-blog")
    public ResponseEntity<BlogResponse> addNewBlog(@RequestBody BlogRequest requestContent) {
        try {
            BlogResponse response = blogService.createBlog(requestContent);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (RuntimeException e) {
            log.error("Runtime error on add new blog: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            log.error("Server error on add new blog: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/get-blog-details/{id}")
    public ResponseEntity<BlogResponse> getBlogDetails(@PathVariable String id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(blogService.getBlogById(id));
        } catch (RuntimeException e) {
            log.error("Runtime error on get blog details: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            log.error("Server error on get blog details: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/delete-blog/{blogId}")
    public ResponseEntity<Void> deleteBlog(@PathVariable String blogId) {
        try {
            blogService.deleteBlog(blogId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (RuntimeException e) {
            log.error("Runtime error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            log.error("Server error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
