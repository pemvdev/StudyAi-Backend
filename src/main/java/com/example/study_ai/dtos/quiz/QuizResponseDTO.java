package com.example.study_ai.dtos.quiz;

import java.util.List;

public record QuizResponseDTO(Long id,
                              List<QuestionResponseDTO> questions) {
}
