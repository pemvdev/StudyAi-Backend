package com.example.study_ai.controllers;

import com.example.study_ai.dtos.CreateSubjectRequestDTO;
import com.example.study_ai.dtos.SubjectResponseDTO;
import com.example.study_ai.services.SubjectService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/classrooms/{classroomId}/subjects")
public class SubjectController {

    private final SubjectService subjectService;

    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @PostMapping
    public ResponseEntity<SubjectResponseDTO> create(
            @PathVariable Long classroomId,
            @RequestBody @Valid CreateSubjectRequestDTO request,
            Authentication authentication
    ) {
        SubjectResponseDTO subject =
                subjectService.create(classroomId, request, authentication);

        return ResponseEntity.status(HttpStatus.CREATED).body(subject);
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
}
