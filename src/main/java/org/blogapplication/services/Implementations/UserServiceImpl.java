package org.blogapplication.services.Implementations;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.blogapplication.dto.ChangePasswordRequest;
import org.blogapplication.dto.UserResponse;
import org.blogapplication.dto.UserUpdateRequest;
import org.blogapplication.entity.ImageEntity;
import org.blogapplication.entity.User;
import org.blogapplication.repository.UserRepository;
import org.blogapplication.services.ImageService;
import org.blogapplication.services.UserService;
import org.blogapplication.util.UserUtilityService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserUtilityService userUtilityService;
    private final ImageService imageService;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    

    @Override
    public void updateUserData(UserUpdateRequest request) {
        // get the logged user details
        User exitsUser = loggedUser();

        // update with comparing existing user details
        User updateUser = User.builder()
                .firstname(request.getFirstname() == null ? exitsUser.getFirstname() : request.getFirstname())
                .lastname(request.getLastname() == null ? exitsUser.getLastname() : request.getLastname())
                .email(request.getEmail() == null ? exitsUser.getEmail() : request.getEmail())
                .phoneNumber(request.getPhoneNumber() == null ? exitsUser.getPhoneNumber() : request.getPhoneNumber())
                .build();

        // save in database
        userRepository.save(updateUser);
    }

    @Override
    public void updateProfileImage(MultipartFile image) {
        // get the logged user
        User existUser = loggedUser();

        try {
            ImageEntity uploadImage;

            if (existUser.getProfileImage() != null && existUser.getProfileImage().get("id") != null) {
                // replace with exiting image
                String imageId = existUser.getProfileImage().get("id");
                uploadImage = imageService.replaceImage(imageId, image);
            } else {
                // upload a new image
                uploadImage = imageService.uploadImage(image);
            }

            Map<String, String> profileImageMap = new HashMap<>();
            profileImageMap.put("id", uploadImage.getId());
            profileImageMap.put("url", uploadImage.getImageUrl());

            existUser.setProfileImage(profileImageMap);
            userRepository.save(existUser);
        } catch (IOException e) {
            log.error("Error while updating profile image: ", e);
            throw new UsernameNotFoundException("Failed to update profile image");
        }
    }

    @Override
    public void deleteAccount() {
        // get the logged user details
        userRepository.delete(loggedUser());
    }

    @Override
    public boolean validatePassword(String rawPassword) {
        return passwordEncoder.matches(rawPassword, loggedUser().getPassword());
    }

    @Override
    public void changePassword(ChangePasswordRequest request) {
        User exitsUser = loggedUser();

        // if old password is validated
        if (validatePassword(request.getOldPassword())) {
            exitsUser.setPassword(passwordEncoder.encode(request.getNewPassword()));
            userRepository.save(exitsUser);

            // after save new password send password reset email with logged user email
            sendPasswordResetEmail(loggedUser().getEmail());
        } else {
            throw new UsernameNotFoundException("Old Password Not Valid");
        }
    }

    @Override
    public void sendPasswordResetEmail(String email) {
        String username = loggedUser().getFirstname() + " " + loggedUser().getLastname();
        emailService.sendPasswordResetEmail(email, username);
    }

    @Override
    public void resetPassword(String email, String newPassword) {
           User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

    user.setPassword(passwordEncoder.encode(newPassword));
    userRepository.save(user);
    }

    @Override
    public void followUser(String followerId, String targetUserId) {

    }

    @Override
    public void unfollowUser(String followerId, String targetUserId) {

    }

    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Override
    public List<UserResponse> getFollowers(String userId) {
        return List.of();
    }

    @Override
    public List<UserResponse> getFollowing(String userId) {
        return List.of();
    }

    private User loggedUser() {
        return userRepository.findByEmail(userUtilityService.getLoggedUserName()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
