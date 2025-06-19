package org.blogapplication.services.Implementations;

import lombok.RequiredArgsConstructor;
import org.blogapplication.dto.BlogResponse;
import org.blogapplication.dto.UserResponse;
import org.blogapplication.entity.User;
import org.blogapplication.repository.UserRepository;
import org.blogapplication.services.AdminService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final UserRepository userRepository;


    @Override
    public List<BlogResponse> getUserBlogs(String userId) {
        return List.of();
    }


    @Override
    public void promoteToAdmin(String userId) {

    }

    @Override
    public void suspendUser(String userId) {

    }

    @Override
    public boolean isUserActive(String userId) {
        return false;
    }

    @Override
    public List<UserResponse> getAllUsers() {
        List<User> allUsers = userRepository.findAll();
        return allUsers.stream().map(user -> new UserResponse(
                user.getId(),
                user.getFirstname(),
                user.getLastname(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getRole()
        )).collect(Collectors.toList());
    }
}
