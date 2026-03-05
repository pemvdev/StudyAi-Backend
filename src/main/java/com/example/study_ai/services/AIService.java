package com.example.study_ai.services;

import com.example.study_ai.dtos.quiz.AIQuestionDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

@Service
public class AIService {

    @Value("${groq.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public List<AIQuestionDTO> generateQuiz(String topicContent) throws Exception {
        System.out.println("GROQ KEY: " + apiKey);

        String url = "https://api.groq.com/openai/v1/chat/completions";

        String prompt = """
        Based on the following study material:

        %s

        Generate 5 multiple choice questions.
        Each question must have 4 options.
        Return ONLY valid JSON in this format:

        [
          {
            "question": "...",
            "options": ["A", "B", "C", "D"],
            "correctIndex": 0
          }
        ]

        No markdown.
        No explanation.
        Only JSON.
    """.formatted(topicContent);

        Map<String, Object> body = Map.of(
                "model", "llama-3.1-8b-instant",
                "temperature", 0.3,
                "messages", List.of(
                        Map.of("role", "user", "content", prompt)
                )
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        // POST request
        ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

        Map<String, Object> responseBody = response.getBody();
        if (responseBody == null || !responseBody.containsKey("choices")) {
            throw new RuntimeException("No choices returned by the API");
        }

        // Pega o primeiro choice
        List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");
        String content = (String) ((Map<String, Object>) choices.get(0).get("message")).get("content");

        // Desserializa o JSON em List<AIQuestionDTO>
        ObjectMapper mapper = new ObjectMapper();
        List<AIQuestionDTO> questions = mapper.readValue(
                content.trim(), // remove espaços ou quebras de linha extras
                new TypeReference<List<AIQuestionDTO>>() {}
        );

        return questions;
    }
}
