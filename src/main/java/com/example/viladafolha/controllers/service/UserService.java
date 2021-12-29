package com.example.viladafolha.controllers.service;

import com.example.viladafolha.model.InhabitantDao;
import com.example.viladafolha.model.UserSpringSecurity;
import com.example.viladafolha.model.transport.InhabitantDTO;
import com.example.viladafolha.model.transport.JwtDTO;
import com.example.viladafolha.util.JWTUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    private InhabitantDao inhabitantDao;
    private JWTUtil jwtUtil;

    public UserService(InhabitantDao inhabitantDao, JWTUtil jwtUtil) {
        this.inhabitantDao = inhabitantDao;
        this.jwtUtil = jwtUtil;
    }

    public InhabitantDTO getInhabitant(String email) {
        Optional<InhabitantDTO> optional = inhabitantDao.getByEmail(email);
        return optional.orElse(null);
    }

    public JwtDTO generateToken(String email) {
        return jwtUtil.generateToken(email);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = getInhabitant(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new UserSpringSecurity(user.getEmail(), user.getPassword(), user.getRoles());
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
