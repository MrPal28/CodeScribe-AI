package org.blogapplication.Service;

import org.blogapplication.entity.User;
import org.blogapplication.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DataTest {

    @Autowired
    private UserRepository userRepository;


    @Test
    void saveData(){
        User user = new User();

        user.setFirstname("Team");
        user.setLastname("Davis");
        user.setEmail("gamil@gamil.com");
        user.setPassword("gamil");
        user.setPhoneNumber("gamil");
        userRepository.save(user);
    }
}
