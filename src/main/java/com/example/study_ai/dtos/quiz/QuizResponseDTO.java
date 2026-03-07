package com.example.study_ai.dtos.quiz;

import java.time.LocalDateTime;
import java.util.List;

public record QuizResponseDTO(Long id,
                              List<QuestionResponseDTO> questions,
                              LocalDateTime createdAt,
                              Double score,
                              Integer totalQuestions) {
}
