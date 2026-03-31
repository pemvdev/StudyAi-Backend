package com.example.study_ai.controllers;

import com.example.study_ai.domain.user.User;
import com.example.study_ai.services.StorageService;
import com.example.study_ai.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    StorageService storageService;

    @Autowired
    UserService userService;


    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser(Authentication authentication) {
        Long id = Long.valueOf(authentication.getName());

        User user = userService.findById(id);

        return ResponseEntity.ok(user);
    }


    @PostMapping("/me/profile-picture")
    public ResponseEntity<User> uploadProfilePicture(
            Authentication authentication,
            @RequestParam("file") MultipartFile file) throws IOException {

        Long userId = Long.valueOf(authentication.getName());

        User user = userService.uploadProfilePicture(userId, file);

        return ResponseEntity.ok(user);
    }






}
