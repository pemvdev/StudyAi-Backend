package com.example.study_ai.dtos.quiz;

import java.util.List;

public record QuestionResponseDTO(Long questionId,
                                  String question,
                                  List<String> options) {
}
