package com.example.study_ai.controllers;

import com.example.study_ai.domain.user.Classroom;
import com.example.study_ai.domain.user.Subject;
import com.example.study_ai.domain.user.User;
import com.example.study_ai.dtos.ClassroomResponseDTO;
import com.example.study_ai.dtos.CreateSubjectRequestDTO;
import com.example.study_ai.dtos.SubjectResponseDTO;
import com.example.study_ai.repositories.ClassroomRepository;
import com.example.study_ai.repositories.UserRepository;
import com.example.study_ai.services.SubjectService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/classrooms/{classroomId}/subjects")
public class SubjectController {

    private final SubjectService subjectService;

    @Autowired
    private  UserRepository userRepository;

    @Autowired
    private ClassroomRepository classroomRepository;

    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @PostMapping
    public ResponseEntity<SubjectResponseDTO> createSubject(
            @PathVariable Long classroomId,
            @RequestBody CreateSubjectRequestDTO request,
            Authentication authentication
    ) {
        Long userId = Long.valueOf(authentication.getName());

        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new RuntimeException("Classroom not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Subject subject =
                subjectService.createSubject(classroomId, request,  user);

        SubjectResponseDTO response = new SubjectResponseDTO(
                subject.getId(),
                subject.getName(),
                subject.getClassroom().getId()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{subjectId}")
    public ResponseEntity<SubjectResponseDTO> updateSubject(
            @PathVariable Long classroomId,
            @PathVariable Long subjectId,
            @RequestBody CreateSubjectRequestDTO request,
            Authentication authentication
    ) {
        return ResponseEntity.ok(
                subjectService.updateSubject(
                        classroomId,
                        subjectId,
                        request,
                        authentication
                )
        );
    }

    @GetMapping
    public ResponseEntity<List<SubjectResponseDTO>> list(
            @PathVariable Long classroomId,
            Authentication authentication
    ) {
        return ResponseEntity.ok(
                subjectService.listByClassroom(classroomId, authentication)
        );
    }

    @DeleteMapping("/{subjectId}")
    public ResponseEntity<Void> delete(
            @PathVariable Long classroomId,
            @PathVariable Long subjectId,
            Authentication authentication
    ) {
        subjectService.deleteSubject(classroomId, subjectId, authentication);
        return ResponseEntity.noContent().build();
    }


}
