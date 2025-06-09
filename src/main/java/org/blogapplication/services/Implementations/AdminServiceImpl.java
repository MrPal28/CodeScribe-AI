package org.blogapplication.services.Implementations;

import lombok.RequiredArgsConstructor;
import org.blogapplication.dto.BlogResponse;
import org.blogapplication.dto.UserStatsResponse;
import org.blogapplication.entity.User;
import org.blogapplication.services.AdminService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    @Override
    public List<BlogResponse> getUserBlogs(String userId) {
        return List.of();
    }

    @Override
    public UserStatsResponse getUserStats(String userId) {
        return null;
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
    public List<User> getAllUsers() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllUsers'");
    }
}
