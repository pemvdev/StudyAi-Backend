package com.example.study_ai.dtos.quiz;

import java.util.List;

public record CreateQuizRequestDTO(List<Long> topicIds) {
}
