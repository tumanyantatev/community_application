package com.application.community.helper;

import com.application.community.models.User;

public class UsersHelper {
    private static final String FIRST_NAME_PREFIX = "MyFirstName";
    private static final String LAST_NAME_PREFIX = "MyLastName";
    private static final String USERNAME_PREFIX = "MyUsername";
    private static final String PASSWORD_PREFIX = "MyPassword";
    private static final String ABOUT_PREFIX = "About Me";

    public static User getUser(int i) {
        User user = new User();
        String firstName = FIRST_NAME_PREFIX + i;
        String lastName = LAST_NAME_PREFIX + i;
        String username = USERNAME_PREFIX + i;
        String password = PASSWORD_PREFIX + i;
        String about = ABOUT_PREFIX + i;

        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUsername(username);
        user.setPassword(password);
        user.setAbout(about);
        return user;
    }
}
