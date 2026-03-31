package com.example.study_ai.services;

import com.example.study_ai.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class StorageService {

    @Autowired
    UserRepository userRepository;

    public String save(MultipartFile file) throws IOException {

        String folder = "uploads/users/";
        String originalName = file.getOriginalFilename()
                .replaceAll("\\s+", "_"); // troca espaços por underscore

        String fileName = UUID.randomUUID() + "_" + originalName;

        Path uploadPath = Paths.get(folder);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(fileName);

        Files.write(filePath, file.getBytes());

        return "/uploads/users/" + fileName;
    }


}
