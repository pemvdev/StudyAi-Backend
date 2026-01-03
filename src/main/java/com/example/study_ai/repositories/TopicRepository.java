package com.example.study_ai.repositories;

import com.example.study_ai.domain.user.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopicRepository extends JpaRepository<Topic, Long> {


}
