package com.example.study_ai.controllers;

import com.example.study_ai.domain.user.Classroom;
import com.example.study_ai.domain.user.User;
import com.example.study_ai.dtos.ClassroomResponseDTO;
import com.example.study_ai.dtos.CreateClassroomRequestDTO;
import com.example.study_ai.dtos.UpdateClassroomRequestDTO;
import com.example.study_ai.repositories.UserRepository;
import com.example.study_ai.services.ClassroomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/classrooms")
@RequiredArgsConstructor
public class ClassroomController {

    private final ClassroomService classroomService;
    private final UserRepository userRepository;

    @GetMapping("/user")
    public ResponseEntity<List<Classroom>> getByUser(Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        return ResponseEntity.ok(classroomService.findByUserId(userId));
    }

    @PostMapping
    public ResponseEntity<ClassroomResponseDTO> createClassroom(
            @RequestBody CreateClassroomRequestDTO request,
            Authentication authentication
    ) {
        Long userId = Long.valueOf(authentication.getName());

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Classroom classroom =
                classroomService.createClassRoom(request, user);

        ClassroomResponseDTO response = new ClassroomResponseDTO(
                classroom.getId(),
                classroom.getName(),
                classroom.getDescription(),
                classroom.getCreatedAt()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Classroom> updateClassroom(
            @PathVariable Long id,
            @Valid @RequestBody UpdateClassroomRequestDTO request,
            Authentication authentication){
        return ResponseEntity.ok(
                classroomService.updateClassroom(id, request, authentication));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClassroom(
            @PathVariable Long id,
            Authentication authentication){
        classroomService.deleteClassroom(id, authentication);
        return ResponseEntity.noContent().build();
    }



}
