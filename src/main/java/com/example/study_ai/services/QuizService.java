package com.example.study_ai.services;

import com.example.study_ai.domain.user.Quiz;
import com.example.study_ai.domain.user.QuizQuestion;
import com.example.study_ai.domain.user.Topic;
import com.example.study_ai.dtos.quiz.AIQuestionDTO;
import com.example.study_ai.dtos.quiz.QuestionResponseDTO;
import com.example.study_ai.dtos.quiz.QuizResponseDTO;
import com.example.study_ai.repositories.QuizQuestionRepository;
import com.example.study_ai.repositories.QuizRepository;
import com.example.study_ai.repositories.TopicRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuizService {
    private final TopicRepository topicRepository;
    private final QuizRepository quizRepository;
    private final QuizQuestionRepository quizQuestionRepository;
    private final AIService aiService;

    public QuizService(
            TopicRepository topicRepository,
            QuizRepository quizRepository,
            QuizQuestionRepository quizQuestionRepository,
            AIService aiService
    ) {
        this.topicRepository = topicRepository;
        this.quizRepository = quizRepository;
        this.quizQuestionRepository = quizQuestionRepository;
        this.aiService = aiService;
    }

    public QuizResponseDTO createQuiz(List<Long> topicIds, Long userId) throws Exception {

        List<Topic> topics = topicRepository.findAllById(topicIds);

        String content = topics.stream()
                .map(Topic::getContent)
                .collect(Collectors.joining("\n\n"));

        content = limitContent(content);

        List<AIQuestionDTO> aiQuestions =
                aiService.generateQuiz(content);

        Quiz quiz = new Quiz(userId);

        quiz = quizRepository.save(quiz);

        List<QuizQuestion> savedQuestions = new ArrayList<>();

        for (AIQuestionDTO dto : aiQuestions) {

            QuizQuestion question = new QuizQuestion(
                    quiz,
                    dto.question(),
                    dto.options().get(0),
                    dto.options().get(1),
                    dto.options().get(2),
                    dto.options().get(3),
                    dto.correctIndex()
            );

            savedQuestions.add(question);
        }

        quizQuestionRepository.saveAll(savedQuestions);

        return mapToResponse(quiz, savedQuestions);
    }

    private String limitContent(String content) {
        return content.length() > 5000
                ? content.substring(0, 5000)
                : content;
    }

    private QuizResponseDTO mapToResponse(
            Quiz quiz,
            List<QuizQuestion> questions
    ) {

        List<QuestionResponseDTO> questionResponses =
                questions.stream()
                        .map(q -> new QuestionResponseDTO(
                                q.getId(),
                                q.getQuestionText(),
                                List.of(
                                        q.getOptionA(),
                                        q.getOptionB(),
                                        q.getOptionC(),
                                        q.getOptionD()
                                )
                        ))
                        .toList();

        return new QuizResponseDTO(
                quiz.getId(),
                questionResponses
        );
    }

    public List<QuizResponseDTO> findByUserId(Long userId) {

        List<Quiz> quizzes = quizRepository.findByUserId(userId);

        return quizzes.stream()
                .map(quiz -> {

                    List<QuizQuestion> questions =
                            quizQuestionRepository.findByQuizId(quiz.getId());

                    return mapToResponse(quiz, questions);

                })
                .toList();
    }

    public QuizResponseDTO getQuizById(Long id) {

        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Quiz não encontrado"));

        List<QuizQuestion> questions =
                quizQuestionRepository.findByQuizId(id);

        return mapToResponse(quiz, questions);
    }
}
