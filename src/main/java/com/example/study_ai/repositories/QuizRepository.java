package com.example.study_ai.repositories;

import com.example.study_ai.domain.user.Quiz;
import com.example.study_ai.dtos.quiz.QuizResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
    List<Quiz> findByUserId(Long userId);
}
