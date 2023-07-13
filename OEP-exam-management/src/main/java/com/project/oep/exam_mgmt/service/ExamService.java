package com.project.oep.exam_mgmt.service;

import java.util.List;

import com.project.oep.dtos.AnswerRequest;
import com.project.oep.dtos.RequestExam;
import com.project.oep.models.Answer;
import com.project.oep.models.Question;

import jakarta.validation.constraints.NotBlank;

public interface ExamService {

	List<Question> loadQuestions(String traineeUsername);

	String questions(AnswerRequest request);

	List<Answer> loadOptions(Long questionId,
			@NotBlank(message = "Username shouldn't be empty") String traineeUsername);

	String permitForExam(RequestExam requestedTrainee);

}
