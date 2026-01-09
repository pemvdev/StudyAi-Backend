package com.example.study_ai.services;

import com.example.study_ai.domain.user.Classroom;
import com.example.study_ai.domain.user.Subject;
import com.example.study_ai.domain.user.User;
import com.example.study_ai.dtos.CreateSubjectRequestDTO;
import com.example.study_ai.dtos.SubjectResponseDTO;
import com.example.study_ai.repositories.ClassroomRepository;
import com.example.study_ai.repositories.SubjectRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubjectService {

    private final SubjectRepository subjectRepository;
    private final ClassroomRepository classroomRepository;


    public SubjectResponseDTO create(
            Long classroomId,
            @Valid CreateSubjectRequestDTO request,
            Authentication authentication
    ) {

        Classroom classroom = classroomRepository
                .findById(classroomId)
                .orElseThrow(() -> new RuntimeException("Classroom not found"));

        Long authenticatedUserId = Long.parseLong(authentication.getName());

        if (!classroom.getUser().getId().equals(authenticatedUserId)) {
            throw new AccessDeniedException("You are not the owner of this classroom");
        }

        Subject subject = new Subject();
        subject.setName(request.name());
        subject.setClassroom(classroom);

        Subject saved = subjectRepository.save(subject);

        return new SubjectResponseDTO(
                saved.getId(),
                saved.getName(),
                classroom.getId()
        );
    }

    public List<SubjectResponseDTO> listByClassroom(
            Long classroomId,
            Authentication authentication
    ) {

        Classroom classroom = classroomRepository
                .findById(classroomId)
                .orElseThrow(() -> new RuntimeException("Classroom not found"));

        Long authenticatedUserId = Long.parseLong(authentication.getName());

        if (!classroom.getUser().getId().equals(authenticatedUserId)) {
            throw new AccessDeniedException("You are not the owner of this classroom");
        }

        return subjectRepository
                .findByClassroom(classroom)
                .stream()
                .map(subject -> new SubjectResponseDTO(
                        subject.getId(),
                        subject.getName(),
                        classroom.getId()
                ))
                .toList();
    }

}
