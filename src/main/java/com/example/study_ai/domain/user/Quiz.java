package com.example.study_ai.domain.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.Id;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Double score;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL)
    private List<QuizQuestion> questions = new ArrayList<>();

    public Quiz(Long userId) {
        this.userId = userId;
    }

}
