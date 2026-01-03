package com.example.study_ai.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ResponseDTO(@JsonProperty("name") String name,
                          @JsonProperty("token") String token) { }
