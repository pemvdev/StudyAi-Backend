package com.example.study_ai.controllers;

import com.example.study_ai.dtos.quiz.CreateQuizRequestDTO;
import com.example.study_ai.dtos.quiz.QuizResponseDTO;
import com.example.study_ai.dtos.quiz.QuizResultDTO;
import com.example.study_ai.dtos.quiz.QuizSubmissionDTO;
import com.example.study_ai.services.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/quizzes")
public class QuizController {

    @Autowired
    QuizService quizService;

    @PostMapping
    public ResponseEntity<QuizResponseDTO> createQuiz(
            @RequestBody CreateQuizRequestDTO request,
            Authentication authentication
    ) throws Exception {

        Long userId = Long.valueOf(authentication.getName());

        QuizResponseDTO quiz =
                quizService.createQuiz(request.topicIds(), userId);

        return ResponseEntity.ok(quiz);
    }

    @GetMapping
    public ResponseEntity<List<QuizResponseDTO>> getAllQuizzes(Authentication authentication){
        Long userId = (Long) authentication.getPrincipal();

        return ResponseEntity.ok(quizService.findByUserId(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuizResponseDTO> getQuizById(@PathVariable Long id) {

        QuizResponseDTO quiz = quizService.getQuizById(id);

        return ResponseEntity.ok(quiz);
    }

    @PostMapping("/{id}/submit")
    public QuizResultDTO submitQuiz(
            @PathVariable Long id,
            Authentication authentication,
            @RequestBody QuizSubmissionDTO submission) {

        Long userId = (Long) authentication.getPrincipal();

        return quizService.submitQuiz(id, userId, submission);
    }
}
