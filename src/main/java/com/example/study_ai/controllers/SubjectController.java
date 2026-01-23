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
            @RequestBody CreateSubjectRequestDTO request,
            Authentication authentication
    ) {
        SubjectResponseDTO subject =
                subjectService.create(classroomId, request, authentication);

        return ResponseEntity.status(HttpStatus.CREATED).body(subject);
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
