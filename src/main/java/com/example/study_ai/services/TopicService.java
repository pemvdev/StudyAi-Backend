package com.example.study_ai.services;

import com.example.study_ai.domain.user.Classroom;
import com.example.study_ai.domain.user.Subject;
import com.example.study_ai.domain.user.Topic;
import com.example.study_ai.domain.user.User;
import com.example.study_ai.dtos.CreateSubjectRequestDTO;
import com.example.study_ai.dtos.CreateTopicRequestDTO;
import com.example.study_ai.dtos.SubjectResponseDTO;
import com.example.study_ai.dtos.TopicResponseDTO;
import com.example.study_ai.repositories.SubjectRepository;
import com.example.study_ai.repositories.TopicRepository;
import com.example.study_ai.repositories.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Service
public class TopicService {

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private UserRepository userRepository;

    public Topic createTopic(Long subjectId,
                             CreateTopicRequestDTO request,
                             User user) {

        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        if (!subject.getClassroom().getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Forbidden");
        }

        Topic topic = new Topic();
        topic.setName(request.name());
        topic.setContent(request.content());
        topic.setSubject(subject);

        return topicRepository.save(topic);
    }

    public List<TopicResponseDTO> getTopicsBySubject(Long subjectId,
                                                     Authentication authentication
                                                     ){
        Subject subject = subjectRepository.
                findById(subjectId)
                .orElseThrow(() -> new RuntimeException("Subject not found"));


        Long authenticatedUserId = Long.parseLong(authentication.getName());

        return topicRepository
                .findBySubjectId(subject.getId())
                .stream()
                .map(topic -> new TopicResponseDTO(
                        topic.getId(),
                        topic.getName(),
                        topic.getContent()
                )).toList();
    }

    public TopicResponseDTO updateTopic(
            Long subjectId,
            Long topicId,
            CreateTopicRequestDTO request,
            Authentication authentication
    ) {
        Long userId = Long.valueOf(authentication.getName());

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        Topic topic = topicRepository.findById(topicId)
                        .orElseThrow(() -> new RuntimeException("Topic Not found!"));

        topic.setName(request.name());
        topicRepository.save(topic);

        return new TopicResponseDTO(
                topic.getId(),
                topic.getName(),
                topic.getContent());
    }

    public void deleteTopic(
            Long subjectId,
            Long topicId,
            Authentication authentication
    ){
        Long userId = Long.valueOf(authentication.getName());

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        Topic topic = topicRepository.findById(topicId)
                        .orElseThrow(() -> new RuntimeException("Topic not found!"));


        topicRepository.delete(topic);
    }

}
