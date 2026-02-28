package com.example.study_ai.repositories;

import com.example.study_ai.domain.user.Classroom;
import com.example.study_ai.domain.user.Subject;
import com.example.study_ai.dtos.SubjectResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubjectRepository extends JpaRepository<Subject, Long> {

    List<Subject> findByClassroomId(Long classroomId);

    List<Subject> findByClassroom(Classroom classroom);

    Optional<SubjectResponseDTO> findByIdAndClassroom(Long subjectId, Classroom classroom);
}
