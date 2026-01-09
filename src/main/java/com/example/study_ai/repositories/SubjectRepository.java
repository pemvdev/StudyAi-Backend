package com.example.study_ai.repositories;

import com.example.study_ai.domain.user.Classroom;
import com.example.study_ai.domain.user.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubjectRepository extends JpaRepository<Subject, Long> {

    List<Subject> findByClassroomId(Long classroomId);

    List<Subject> findByClassroom(Classroom classroom);
}
