package com.example.viladafolha.controllers.service;

import com.example.viladafolha.model.User;
import com.example.viladafolha.model.UserDao;
import com.example.viladafolha.model.UserSpringSecurity;
import com.example.viladafolha.util.JWTUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserService implements UserDetailsService {
    private UserDao userDAO;
    private JWTUtil jwtUtil;

    public UserService(UserDao userDAO, JWTUtil jwtUtil) {
        this.userDAO = userDAO;
        this.jwtUtil = jwtUtil;
    }

    public User getUser(String email) {
        return userDAO.getUser(email);
    }

    public String generateToken(User user) {
        return jwtUtil.generateToken(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = getUser(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new UserSpringSecurity(user.getEmail(), user.getPassword(), new ArrayList<>());
    }

    public Authentication getAuthentication(String token) {
        var userDetails = loadUserByUsername(jwtUtil.getSubject(token));
        return new UsernamePasswordAuthenticationToken(
                userDetails,
                "",
                userDetails.getAuthorities()
        );
    }


    public JWTUtil getJwtUtil() {
        return jwtUtil;
    }
}
