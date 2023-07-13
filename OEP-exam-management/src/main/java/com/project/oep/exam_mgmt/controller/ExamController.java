package com.project.oep.exam_mgmt.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.project.oep.dtos.AnswerRequest;
import com.project.oep.dtos.RequestExam;
import com.project.oep.exam_mgmt.service.ExamService;
import com.project.oep.models.Answer;
import com.project.oep.models.Question;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

@RestController
@RequestMapping("/online-examination-portal")
@Validated
@Slf4j
public class ExamController {

	@Autowired
	private ExamService examService;

	/**
	 * Get all the questions which is asked for examination [Secured by Spring
	 * security]
	 *
	 * @return
	 */
	@GetMapping("/all-questions")
	public ResponseEntity<List<Question>> loadQuestions(@Valid @RequestParam(required = true) String traineeUsername) {
		log.info("Questions from repo");
		return ResponseEntity.status(HttpStatus.OK).body(examService.loadQuestions(traineeUsername));
	}

	/**
	 * Api which is start the examination and by trainees
	 *
	 * @param request
	 * @return
	 */
	@PostMapping("/validate-answer/question")
	public ResponseEntity<String> questions(@Valid @RequestBody AnswerRequest request) {
		log.warn("Examination process started");
		return ResponseEntity.status(HttpStatus.OK).body(examService.questions(request));
	}

	/**
	 * Load the option answer by questionId to answer the question
	 *
	 * @param questionId
	 * @return
	 */
	@GetMapping("/load-options")
	public ResponseEntity<List<Answer>> loadOptions(@Valid @RequestParam(required = true) Long questionId,
			@RequestParam @NotBlank(message = "Username shouldn't be empty") String traineeUsername) {
		log.warn("Options loaded apis started");
		return ResponseEntity.status(HttpStatus.OK).body(examService.loadOptions(questionId, traineeUsername));
	}

	/**
	 * Permit student before attending the actual examination
	 * 
	 * @param requestedTrainee
	 * @return
	 */
	@PutMapping("/permission")
	public ResponseEntity<String> permitForExam(@Valid @RequestBody RequestExam requestedTrainee) {
		log.warn("Request to attend the examination...");
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(examService.permitForExam(requestedTrainee));
	}
}
