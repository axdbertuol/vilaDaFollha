package com.example.viladafolha.controllers.rest;

import com.example.viladafolha.controllers.service.UserService;
import com.example.viladafolha.model.CredentialsDTO;
import com.example.viladafolha.model.User;
import com.example.viladafolha.model.UserSpringSecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/auth")
public class AuthenticationRest {
    @Autowired
    private UserService userService;

    @PostMapping(path = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> login(@RequestBody CredentialsDTO credentialsDTO){
        UserSpringSecurity uss = (UserSpringSecurity) userService.loadUserByUsername(credentialsDTO.getEmail());
        String token = userService.generateToken(new User(uss.getUsername(), uss.getPassword()));
        return new ResponseEntity<>(token, HttpStatus.OK);
    }
}
