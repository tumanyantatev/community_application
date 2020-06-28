package com.application.community.services;


import com.application.community.controllers.UsersController;
import com.application.community.models.User;
import com.application.community.repositories.UserRepository;
import com.application.community.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.application.community.helper.UsersHelper.getUser;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UsersTest {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsersController usersController;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void deleteAllUsers() {
        userRepository.deleteAll();
    }

    @Test
    public void testCreateUser() {
        User user = getUser(1);
        User createdUser = usersController.createUser(user);
        validateUser(createdUser, user);
    }

    @Test
    public void testUpdateUser() {
        User user = getUser(1);
        User updateUser = getUser(2);

        User createdUser = usersController.createUser(user);
        validateUser(createdUser, user);
        User updatedUser = usersController.updateUser(createdUser.getUserId(), updateUser);
        validateUser(updatedUser, updatedUser);
    }

    @Test
    public void testGetAllUsersNoParam() {
        final int numberOdUsers = 5;
        for (int i = 1; i <= numberOdUsers; ++i) {
            User user = getUser(i);
            usersController.createUser(user);
        }

        List<User> users = usersController.findUsers(null, null, null, null);
        assertEquals(numberOdUsers, users.size());
    }

    @Test
    public void testGetAllUsersByNameParam() {
        final int numberOdUsers = 10;
        for (int i = 1; i <= numberOdUsers; ++i) {
            User user = getUser(i);
            usersController.createUser(user);
        }

        List<User> users = usersController.findUsers("1", null, null, null);
        assertEquals(2, users.size());
    }

    @Test
    public void testGetAllUsersByPageParam() {
        final int numberOfUsers = 10;
        final int pageSize = 5;
        for (int i = 1; i <= numberOfUsers; ++i) {
            User user = getUser(i);
            usersController.createUser(user);
        }

        List<User> users = usersController.findUsers(null, 0, pageSize, null);
        assertEquals(pageSize, users.size());
        for (int i = 1; i <= 5; ++i) {
            User expectedUser = getUser(i);
            validateUser(users.get(i - 1), expectedUser);
        }

        users = usersController.findUsers(null, 1, pageSize, null);
        assertEquals(pageSize, users.size());
        for (int i = 6; i <= 10; ++i) {
            User expectedUser = getUser(i);
            validateUser(users.get(i - pageSize - 1), expectedUser);
        }
    }

    @Test
    @Transactional
    public void testGetAllUsersSortedByName() {
        final int numberOfUsers = 10;
        for (int i = numberOfUsers - 1; i >= 0; --i) {
            User user = getUser(i);
            usersController.createUser(user);
        }

        List<User> users = usersController.findUsers(null, null, null, "firstName");
        assertEquals(numberOfUsers, users.size());
        for (int i = 0; i < numberOfUsers; ++i) {
            User expectedUser = getUser(i);
            validateUser(users.get(i), expectedUser);
        }
    }

    @Test
    public void testDeleteUser() {
        User user1 = getUser(1);
        usersController.createUser(user1);
        User user2 = getUser(2);
        usersController.createUser(user2);

        List<User> users = usersController.findUsers(null, null, null, null);
        assertEquals(2, users.size());
        usersController.deleteUser(user1.getUserId());
        users = usersController.findUsers(null, null, null, null);
        assertEquals(1, users.size());
        User user = users.get(0);
        validateUser(user, user2);
    }

    private void validateUser(User createdUser, User expectedUser) {
        assertNotNull(createdUser);
        assertEquals(expectedUser.getFirstName(), createdUser.getFirstName());
        assertEquals(expectedUser.getLastName(), createdUser.getLastName());
        assertEquals(expectedUser.getUsername(), createdUser.getUsername());
        //assertTrue(passwordEncoder.matches(expectedUser.getPassword(), createdUser.getPassword()));
        //assertEquals(passwordEncoder.encode(expectedUser.getPassword()), createdUser.getPassword());
        assertEquals(expectedUser.getAbout(), createdUser.getAbout());
    }
}
