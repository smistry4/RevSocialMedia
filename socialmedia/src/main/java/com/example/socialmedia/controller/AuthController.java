package com.example.socialmedia.controller;

import javax.naming.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.socialmedia.entity.AppUsers;
import com.example.socialmedia.service.AppUserService;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {


    @Autowired
    private AppUserService appUserService;

    @PostMapping("/register")
    private ResponseEntity<String> register(@RequestBody AppUsers appUser) {
        String token = appUserService.registerUser(appUser);
        return ResponseEntity.status(200).header("Authorization", "Bearer "+token).body("User created");
    }

    @PostMapping("/login")
    private ResponseEntity<String> login(@RequestBody AppUsers appUser) throws AuthenticationException {
        String token = appUserService.loginUser(appUser);
        return ResponseEntity.status(200).header("Authorization", "Bearer " + token).body("Login successful");
    }

}
