package com.example.study_ai.services;

import com.example.study_ai.domain.user.Classroom;
import com.example.study_ai.domain.user.User;
import com.example.study_ai.dtos.CreateClassroomRequestDTO;
import com.example.study_ai.dtos.UpdateClassroomRequestDTO;
import com.example.study_ai.exceptions.ClassroomNotFoundException;
import com.example.study_ai.repositories.ClassroomRepository;
import com.example.study_ai.repositories.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClassroomService {

    private final ClassroomRepository classroomRepository;

    private final UserRepository userRepository;


    public List<Classroom> getAllClassrooms(Authentication authentication) {

        Long userId = (Long) authentication.getPrincipal();

        return classroomRepository.findAllByUser_Id(userId);
    }


    public Classroom createClassRoom(CreateClassroomRequestDTO request, User user){
        Classroom classroom = new Classroom();
        classroom.setName(request.name());
        classroom.setDescription(request.description());
        classroom.setUser(user);
        classroom.setCreatedAt(request.createdAt());

        return classroomRepository.save(classroom);
    }

    public Classroom updateClassroom(Long id,
                                     @Valid UpdateClassroomRequestDTO request,
                                     Authentication authentication) {
        Classroom classroom = classroomRepository.findById(id)
                .orElseThrow(ClassroomNotFoundException::new);

        Long authenticatedUserId = (Long) authentication.getPrincipal();

        if (!classroom.getUser().getId().equals(authenticatedUserId)) {
            throw new AccessDeniedException("You are not the owner");
        }

        classroom.setName(request.name());
        classroom.setDescription(request.description());

        return classroomRepository.save(classroom);
    }


    public void deleteClassroom(Long id, Authentication authentication) {

        Long userId = Long.valueOf(authentication.getName());

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Classroom classroom = classroomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Classroom not found"));

        if (!classroom.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Not allowed");
        }

        classroomRepository.delete(classroom);
    }
}
