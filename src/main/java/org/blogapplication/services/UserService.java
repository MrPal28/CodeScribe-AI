package org.blogapplication.services;

import org.blogapplication.dto.UserDTO;
import org.blogapplication.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    /** this function for public controller
     * @param request
     * request containing the user details that's can store the database
     */
    void saveNewUser(UserDTO request);

    /**
     * this function for admin controller
     * @return listOfusers
     * this function return all users with their data
     */
    List<User> getAllUsers();
}
