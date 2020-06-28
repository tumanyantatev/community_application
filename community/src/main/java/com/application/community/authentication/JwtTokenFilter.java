package com.application.community.authentication;

import io.jsonwebtoken.JwtException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtTokenFilter extends GenericFilterBean {
    private static final String AUTHORIZATION = "Authorization";
    private static final String INVALID_JWT_TOKEN = "Invalid JWT token";

    private final JwtTokenProvider jwtTokenProvider;

    public JwtTokenFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        String token = request.getHeader(AUTHORIZATION);
        if (token != null) {
            if (!jwtTokenProvider.isTokenPresentInDB(token)) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, INVALID_JWT_TOKEN);
                throw new UsernameNotFoundException(INVALID_JWT_TOKEN);
            }
            try {
                jwtTokenProvider.validateToken(token) ;
            } catch (JwtException | IllegalArgumentException e) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, INVALID_JWT_TOKEN);
                throw new UsernameNotFoundException(INVALID_JWT_TOKEN);
            }
            Authentication auth = jwtTokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        filterChain.doFilter(req, res);
    }
}
