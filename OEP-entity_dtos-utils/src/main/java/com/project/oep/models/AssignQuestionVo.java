package com.project.oep.models;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

import com.project.oep.auth_service.utils.QuestionType;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
public class AssignQuestionVo {

	@NotNull(message = "questionId can't be empty")
	private Long questionId;

	private Boolean IsAnswered;

	private String trainerName;

	private String location;

	@NotBlank(message = "technology can't be empty")
	private String technology;

	@NotBlank(message = "question description can't be empty")
	private String questionDescription;

	@NotBlank(message = "subject can't be empty")
	private String subject;

	@Enumerated(EnumType.STRING)
	private QuestionType questionType;

	private String answerKey;

	private List<Answer> answerVo;

	private LocalDateTime time;

}
