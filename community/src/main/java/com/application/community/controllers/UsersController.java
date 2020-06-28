package com.application.community.controllers;

import com.application.community.models.User;
import com.application.community.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UsersController {

    private static final Integer DEFAULT_PAGE_SIZE = 20;
    private static final String DEFAULT_SORT_ATTRIBUTE = "userId";

    @Autowired
    private UserService userService;

    @GetMapping
    @RequestMapping("{id}")
    public User getUser(@PathVariable Long id) {
        return userService.getById(id);
    }

    @GetMapping
    public List<User> findUsers(@RequestParam(value = "searchString", required = false) String searchString,
                                @RequestParam(value = "page", required = false) Integer page,
                                @RequestParam(value = "size", required = false) Integer size,
                                @RequestParam(value = "sortAttribute", required = false) String sortAttribute) {
        searchString = searchString == null ? "" : searchString;
        page = page == null ? 0 : page;
        size = size == null ? DEFAULT_PAGE_SIZE : size;
        sortAttribute = sortAttribute == null ? DEFAULT_SORT_ATTRIBUTE  : sortAttribute;
        return userService.findUsers(searchString, page, size, sortAttribute);
    }

    @PostMapping
    public User createUser(@RequestBody final User user) {
        return userService.createUser(user);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        return userService.updateUser(id, user);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
    }
}
