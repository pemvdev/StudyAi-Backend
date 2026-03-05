package com.example.study_ai.dtos.quiz;

import java.util.List;

public record AIQuestionDTO(Long questionId,
                            String question,
                            List<String> options,
                            Integer correctIndex) {
}
