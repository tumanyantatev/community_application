package com.application.community.authentication;

import com.application.community.models.JwtToken;
import com.application.community.models.User;
import com.application.community.repositories.JwtTokenRepository;
import com.application.community.repositories.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private static final long VALIDITY_IN_MILLISECONDS = 60 * 60 * 1000;
    private static String SECRET_KEY = "secret_key";

    @Autowired
    private JwtTokenRepository jwtTokenRepository;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    protected void init() {
        SECRET_KEY = Base64.getEncoder().encodeToString(SECRET_KEY.getBytes());
    }

    public String createToken(String username) {
        Claims claims = Jwts.claims().setSubject(username);
        Date now = new Date();
        Date validity = new Date(now.getTime() + VALIDITY_IN_MILLISECONDS);

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
        JwtToken entity = new JwtToken();
        entity.setToken(token);
        jwtTokenRepository.saveAndFlush(entity);
        return token;
    }

    public boolean validateToken(String token) throws JwtException, IllegalArgumentException {
        Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
        return true;
    }

    public boolean isTokenPresentInDB(String token) {
        JwtToken foundToken = jwtTokenRepository.findByToken(token);
        return foundToken != null;
    }

    public UserDetails getUserDetails(String token) {
        String userName = getUsername(token);
        User user = userRepository.findByUsername(userName);
        UserDetails userDetails = new AuthenticationUser(userName, user.getPassword());
        return userDetails;
    }

    public String getUsername(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getSubject();
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = getUserDetails(token);
        return new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
    }
}

