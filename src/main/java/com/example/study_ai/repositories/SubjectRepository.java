package com.example.study_ai.repositories;

import com.example.study_ai.domain.user.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubjectRepository extends JpaRepository<Subject, Long> {

}
