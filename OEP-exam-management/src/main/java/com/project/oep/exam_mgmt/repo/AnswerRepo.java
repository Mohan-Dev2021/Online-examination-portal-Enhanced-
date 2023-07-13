package com.project.oep.exam_mgmt.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.project.oep.models.Answer;

@EnableJpaRepositories
public interface AnswerRepo extends JpaRepository<Answer, Long> {
}
