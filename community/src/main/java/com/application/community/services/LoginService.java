package com.application.community.services;

import com.application.community.authentication.JwtTokenProvider;
import com.application.community.models.JwtToken;
import com.application.community.models.User;
import com.application.community.repositories.JwtTokenRepository;
import com.application.community.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenRepository jwtTokenRepository;

    public String login(String username, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            User user = userRepository.findByUsername(username);
            if (user == null) {
                throw new UsernameNotFoundException("Invalid username");
            }
            return jwtTokenProvider.createToken(username);

        } catch (AuthenticationException e) {
            throw new UsernameNotFoundException("Invalid username");
        }
    }

    public boolean logout(String token) {
        jwtTokenRepository.delete(new JwtToken(token));
        return true;
    }
}
