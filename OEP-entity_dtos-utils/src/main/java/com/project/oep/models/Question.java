package com.project.oep.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import com.project.oep.auth_service.utils.QuestionType;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "questionD")
public class Question {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotBlank(message = "question can't be blank")
	private String description;

	private String subjectId;

	@NotBlank(message = "subject is mandatory")
	private String subject;

	@Enumerated(EnumType.STRING)
	private QuestionType questionType;

	private LocalDate date;

	private Boolean IsAnswered;

	private String answerKey;

}
