package com.application.community.services;

import com.application.community.authentication.AuthenticationUser;
import com.application.community.models.User;
import com.application.community.repositories.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    public List<User> findUsers(String searchString, Integer page, Integer size, String sortAttribute) {
        return userRepository.find(searchString, PageRequest.of(page, size, Sort.by(sortAttribute)));
    }

    public User getById(Long id) {
        return userRepository.getOne(id);
    }

    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.saveAndFlush(user);
    }

    public User updateUser(Long id, User user) {
        Optional<User> eUser = userRepository.findById(id);
        if (eUser.isPresent()) {
            User existingUser = eUser.get();
            BeanUtils.copyProperties(user, existingUser, "userId");
            return userRepository.saveAndFlush(existingUser);
        } else {
            throw new IllegalArgumentException(String.format("User with id %d not found", id));
        }
    }

    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password. UserService"/*, HttpStatus.UNAUTHORIZED*/);
        }

        AuthenticationUser authUser = new AuthenticationUser(user.getUsername(), user.getPassword());

        return authUser;
    }
}
