package com.example.study_ai.services;

import com.example.study_ai.domain.user.Classroom;
import com.example.study_ai.domain.user.Subject;
import com.example.study_ai.domain.user.User;
import com.example.study_ai.dtos.CreateSubjectRequestDTO;
import com.example.study_ai.dtos.SubjectResponseDTO;
import com.example.study_ai.repositories.ClassroomRepository;
import com.example.study_ai.repositories.SubjectRepository;
import com.example.study_ai.repositories.UserRepository;
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
    private final UserRepository userRepository;


    public Subject createSubject(
            Long classroomId,
            @Valid CreateSubjectRequestDTO request,
            User user
    ) {
        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new RuntimeException("Classroom not found"));

        Subject subject = new Subject();
        subject.setName(request.name());
        subject.setClassroom(classroom);

        return subjectRepository.save(subject);
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

    public SubjectResponseDTO getSpecificSubject(Long classroomId,
                                                 Long subjectId,
                                                 Authentication authentication){
        Classroom classroom = classroomRepository
                .findById(classroomId)
                .orElseThrow(() -> new RuntimeException("Classroom not found"));

        Subject subject = subjectRepository
                .findById(subjectId)
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        Long authenticatedUserId = Long.parseLong(authentication.getName());

        if (!classroom.getUser().getId().equals(authenticatedUserId)) {
            throw new AccessDeniedException("You are not the owner of this classroom");
        }

        return subjectRepository
                .findByIdAndClassroom(subjectId, classroom)
                .orElseThrow(() -> new RuntimeException("Subject not found"));
    }

    public SubjectResponseDTO updateSubject(
            Long classroomId,
            Long subjectId,
            CreateSubjectRequestDTO request,
            Authentication authentication
    ) {
        Long userId = Long.valueOf(authentication.getName());

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new RuntimeException("Classroom not found"));

        if (!classroom.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Not allowed");
        }

        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        if (!subject.getClassroom().getId().equals(classroomId)) {
            throw new RuntimeException("Subject does not belong to this classroom");
        }

        subject.setName(request.name());
        subjectRepository.save(subject);

        return new SubjectResponseDTO(
                subject.getId(),
                subject.getName(),
                subject.getClassroom().getId()
        );
    }


    public void deleteSubject(
            Long classroomId,
            Long subjectId,
            Authentication authentication
    ){
        Long userId = Long.valueOf(authentication.getName());

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new RuntimeException("Classroom not found"));

        if (!classroom.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Not allowed");
        }

        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        if (!subject.getClassroom().getId().equals(classroom.getId())) {
            throw new RuntimeException("Subject does not belong to this classroom");
        }

        subjectRepository.delete(subject);
    }

}
