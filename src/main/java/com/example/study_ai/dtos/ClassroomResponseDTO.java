package com.example.study_ai.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record ClassroomResponseDTO(Long id,
                                   String name,
                                   String description,
                                   @JsonProperty("dateOfCreation")
                                   LocalDateTime createdAt) {
}
