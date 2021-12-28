package com.example.viladafolha.controllers.rest;

import com.example.viladafolha.controllers.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/auth")
public class AuthenticationRest {
    @Autowired
    private UserService userService;


}
