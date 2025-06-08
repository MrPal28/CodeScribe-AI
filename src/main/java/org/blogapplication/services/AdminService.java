package org.blogapplication.services;

import org.blogapplication.dto.BlogResponse;
import org.blogapplication.dto.UserStatsResponse;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface AdminService {
    List<BlogResponse> getUserBlogs(String userId);

    UserStatsResponse getUserStats(String userId);

    void promoteToAdmin(String userId);

    void suspendUser(String userId);

    boolean isUserActive(String userId);
}
