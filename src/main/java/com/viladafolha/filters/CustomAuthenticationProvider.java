package com.viladafolha.filters;

import com.viladafolha.controllers.service.InhabitantService;
import com.viladafolha.model.UserSpringSecurity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final InhabitantService inhabitantService;
    private PasswordEncoder encoder;

    public CustomAuthenticationProvider(InhabitantService inhabitantService) {
        this.inhabitantService = inhabitantService;
        encoder = inhabitantService.getEncoder();
    }


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();
        UserSpringSecurity user;
        try {
            user = (UserSpringSecurity) inhabitantService.loadUserByUsername(email);
            if (encoder.matches(password, user.getPassword())) {
                throw new BadCredentialsException("invalid login details");
            }
        } catch (UsernameNotFoundException e) {
            throw new BadCredentialsException("invalid login details");
        }
        return new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass == UsernamePasswordAuthenticationToken.class;
    }
}
