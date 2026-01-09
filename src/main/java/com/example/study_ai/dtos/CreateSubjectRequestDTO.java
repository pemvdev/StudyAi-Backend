package com.example.study_ai.dtos;

import jakarta.validation.constraints.NotBlank;

public record CreateSubjectRequestDTO(Long id,
                                      @NotBlank String name) {
}
