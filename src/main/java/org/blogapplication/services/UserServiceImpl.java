package org.blogapplication.services;

import lombok.RequiredArgsConstructor;
import org.blogapplication.dto.UserDTO;
import org.blogapplication.entity.User;
import org.blogapplication.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;


    @Override
    public void saveNewUser(UserDTO request) {
        User user = new User();

        user.setName(request.getName());
        userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(userRepository.findAll());
    }
}
