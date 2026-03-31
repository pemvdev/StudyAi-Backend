package com.example.study_ai.services;

import com.example.study_ai.domain.user.User;
import com.example.study_ai.repositories.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final StorageService storageService;

    public UserService(UserRepository userRepository, StorageService storageService) {
        this.userRepository = userRepository;
        this.storageService = storageService;
    }

    public User uploadProfilePicture(Long userId, MultipartFile file) throws IOException {

        User user = userRepository.findById(userId)
                .orElseThrow();

        String path = storageService.save(file);

        user.setProfilePicture(path);

        return userRepository.save(user);
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }
}
