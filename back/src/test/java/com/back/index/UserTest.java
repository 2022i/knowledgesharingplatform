package com.back.index;

import com.back.repository.UserRepository;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class UserTest {
    @Resource
    private UserRepository userRepository;
    @Test
    void initializeUsers() {
        List<User> users = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            User user = new User();
            user.setId(i);
            user.setUsername("user" + i);
            user.setEmail("user" + i + "@example.com");
            user.setPassword("password" + i);
            users.add(user);
            userRepository.saveAll(users);
        }
        assertEquals(100, users.size());
        for (int i = 0; i < 100; i++) {
            User user = users.get(i);
            assertEquals(i + 1, user.getId());
            assertEquals("user" + (i + 1), user.getUsername());
            assertEquals("user" + (i + 1) + "@example.com", user.getEmail());
            assertEquals("password" + (i + 1), user.getPassword());
        }
    }
}