package org.blogapplication.services.Implementations;

import lombok.RequiredArgsConstructor;
import org.blogapplication.dto.ChangePasswordRequest;
import org.blogapplication.dto.UserResponse;
import org.blogapplication.repository.UserRepository;
import org.blogapplication.services.UserService;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    // private final UserRepository userRepository;

    @Override
    public void updateUserData() {

    }

    @Override
    public void updateProfileImage(String userId, MultipartFile image) {

    }

    @Override
    public void deleteAccount(String userId) {

    }

    @Override
    public boolean validatePassword(String userId, String rawPassword) {
        return false;
    }

    @Override
    public void changePassword(String userId, ChangePasswordRequest request) {

    }

    @Override
    public void sendPasswordResetEmail(String email) {

    }

    @Override
    public void resetPassword(String token, String newPassword) {

    }

    @Override
    public void followUser(String followerId, String targetUserId) {

    }

    @Override
    public void unfollowUser(String followerId, String targetUserId) {

    }

    @Override
    public List<UserResponse> getFollowers(String userId) {
        return List.of();
    }

    @Override
    public List<UserResponse> getFollowing(String userId) {
        return List.of();
    }
}
