package com.example.study_ai.repositories;

import com.example.study_ai.domain.user.Classroom;
import com.example.study_ai.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClassroomRepository extends JpaRepository<Classroom, Long> {

    List<Classroom> findByUser(User user);

    List<Classroom> findByUserId(Long userId);

}
