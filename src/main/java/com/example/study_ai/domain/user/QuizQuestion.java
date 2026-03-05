    package com.example.study_ai.domain.user;

    import jakarta.persistence.*;
    import lombok.AllArgsConstructor;
    import lombok.Getter;
    import lombok.NoArgsConstructor;
    import lombok.Setter;
    import jakarta.persistence.Id;

    @Entity
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public class QuizQuestion {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne
        @JoinColumn(name = "quiz_id")
        private Quiz quiz;

        @Column(length = 1000)
        private String questionText;

        private String optionA;
        private String optionB;
        private String optionC;
        private String optionD;

        private Integer correctIndex;
        private Integer selectedIndex;

        private Boolean isCorrect;


        public QuizQuestion(
                Quiz quiz,
                String questionText,
                String optionA,
                String optionB,
                String optionC,
                String optionD,
                Integer correctIndex
        ) {
            this.quiz = quiz;
            this.questionText = questionText;
            this.optionA = optionA;
            this.optionB = optionB;
            this.optionC = optionC;
            this.optionD = optionD;
            this.correctIndex = correctIndex;
        }

    }
