package com.example.study_ai.controllers;

import com.example.study_ai.domain.user.Topic;
import com.example.study_ai.domain.user.User;
import com.example.study_ai.dtos.CreateTopicRequestDTO;
import com.example.study_ai.dtos.TopicResponseDTO;
import com.example.study_ai.repositories.SubjectRepository;
import com.example.study_ai.repositories.UserRepository;
import com.example.study_ai.services.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/classrooms/{classroomId}/subjects/{subjectId}/topics")
public class TopicController {

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TopicService topicService;


    @PostMapping
    public ResponseEntity<TopicResponseDTO> createTopic(
            @PathVariable Long subjectId,
            @RequestBody CreateTopicRequestDTO request,
            Authentication authentication
    ) {
        Long userId = Long.valueOf(authentication.getName());

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Topic topic = topicService.createTopic(subjectId, request, user);

        TopicResponseDTO response = new TopicResponseDTO(
                topic.getId(),
                topic.getName(),
                topic.getContent()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<TopicResponseDTO>> getAllTopics (@PathVariable Long subjectId,
                                                                Authentication authentication){
        return ResponseEntity.ok(
                topicService.getTopicsBySubject(subjectId, authentication));
    }

    @PutMapping("/{topicId}")
    public ResponseEntity<TopicResponseDTO> updateTopic(
            @PathVariable Long subjectId,
            @PathVariable Long topicId,
            @RequestBody CreateTopicRequestDTO request,
            Authentication authentication
    ) {
        return ResponseEntity.ok(
                topicService.updateTopic(
                        subjectId,
                        topicId,
                        request,
                        authentication
                )
        );
    }

    @DeleteMapping("/{topicId}")
    public ResponseEntity<Void> deleteTopic(
            @PathVariable Long subjectId,
            @PathVariable Long topicId,
            Authentication authentication
    ) {
        topicService.deleteTopic(subjectId  , topicId, authentication);
        return ResponseEntity.noContent().build();
    }



}
