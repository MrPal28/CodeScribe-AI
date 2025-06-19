package org.blogapplication.services;

import org.blogapplication.dto.BlogResponse;
import org.blogapplication.dto.UserResponse;
import org.blogapplication.entity.User;

import java.util.List;


public interface AdminService {
    List<BlogResponse> getUserBlogs(String userId);

    void promoteToAdmin(String userId);

    void suspendUser(String userId);

    boolean isUserActive(String userId);

    /**
     * this function for admin controller
     *
     * @return list Of users
     * this function return all users with their data
     */
    List<UserResponse> getAllUsers();
}
