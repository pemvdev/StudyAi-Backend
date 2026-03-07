package com.example.study_ai.dtos.quiz;

public record QuizResultDTO(
                            Double score,
                            int correctAnswers,
                            int totalQuestions) {
}
