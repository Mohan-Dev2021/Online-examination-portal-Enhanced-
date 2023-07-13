package com.project.oep.dtos;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Collection;

import com.project.oep.auth_service.utils.QuestionType;
import com.project.oep.models.Answer;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Data
public class QuestionResponse {

	private Long questionId;

	private Boolean IsAnswered;

	private String questionDescription;

	private String subject;

	@Enumerated(EnumType.STRING)
	private QuestionType questionType;

	private Collection<Answer> answerVo;

	private LocalDateTime time;
}
