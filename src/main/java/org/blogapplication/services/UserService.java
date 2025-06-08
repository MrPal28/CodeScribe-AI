package org.blogapplication.services;

import org.blogapplication.dto.ChangePasswordRequest;
import org.blogapplication.dto.UserRequest;
import org.blogapplication.dto.UserResponse;
import org.blogapplication.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.lang.String;
import java.util.List;

@Service
public interface UserService {

    /**
     * this function for admin controller
     *
     * @param request request containing the user details that's can store the database
     */
    void saveNewUser(UserRequest request);

    /**
     * this function for admin controller
     *
     * @return listOfusers
     * this function return all users with their data
     */
    List<User> getAllUsers();


    void updateUserData();


    void updateProfileImage(String userId, MultipartFile image);


    void deleteAccount(String userId);

    boolean validatePassword(String userId, String rawPassword);

    void changePassword(String userId, ChangePasswordRequest request);

    void sendPasswordResetEmail(String email);

    void resetPassword(String token, String newPassword);


    void followUser(String followerId, String targetUserId);

    void unfollowUser(String followerId, String targetUserId);

    List<UserResponse> getFollowers(String userId);

    List<UserResponse> getFollowing(String userId);
}
