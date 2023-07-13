package com.project.oep.models;

import lombok.Data;

import java.time.LocalDate;

import com.project.oep.auth_service.utils.QuestionType;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Data
public class QuestionVo {

	private Long questionId;

	private String description;

	private String subjectId;

	@Enumerated(EnumType.STRING)
	private QuestionType questionType;

	private LocalDate date;

	private Boolean IsAnswered;

}
