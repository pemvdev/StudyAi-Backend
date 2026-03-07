package com.example.study_ai.dtos.quiz;

import java.util.List;

public record QuizSubmissionDTO(Long quizId,
                                List<QuestionAnswerDTO> answers) {
}
