package com.project.oep.exam_mgmt.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.project.oep.auth_service.utils.Constants;
import com.project.oep.exam_mgmt.service.QuestionService;
import com.project.oep.models.AssignQuestionVo;
import com.project.oep.models.Question;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/online-examination-portal")
@Validated
@Slf4j
public class QuestionController {

	@Autowired
	private QuestionService questionService;

	/**
	 * Add questions to exam portal by trainer(admin) [ Secured by Spring security]
	 *
	 * @param question
	 * @return
	 */
	@Secured(value = Constants.HAS_ROLE)
	@PostMapping("/question")
	public ResponseEntity<Question> addQuestion(@Valid @RequestBody Question question) {
		log.warn("Authenticated access to add question");
		return ResponseEntity.status(HttpStatus.CREATED).body(questionService.addQuestion(question));
	}

	/**
	 * Api for remove the question from the database [Secured by Spring security]
	 *
	 * @param id
	 * @return
	 */
	@Secured(value = Constants.HAS_ROLE)
	@DeleteMapping("/question")
	public ResponseEntity<String> removeQuestion(@Valid @RequestParam(required = true) Long id) {
		log.warn("Question removal api has been called");
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(questionService.removeQuestion(id));
	}

	/**
	 * Create the question paper to trainees by the trainer (admin) [Secured by
	 * spring security]
	 *
	 * @param question
	 * @return
	 */
	@Secured(value = Constants.HAS_ROLE)
	@PostMapping("/examination/set-question-paper")
	public ResponseEntity<AssignQuestionVo> setQuestionDetails(@Valid @RequestBody AssignQuestionVo question) {
		log.warn("Authenticated access occurred");
		return ResponseEntity.status(HttpStatus.CREATED).body(questionService.setQuestionDetails(question));
	}

	/**
	 * Get all questions from repo with answers [Secured by Spring security]
	 *
	 * @param pageNo
	 * @param offset
	 * @return
	 */
	@Secured(value = Constants.HAS_ROLE)
	@GetMapping("/questions")
	public ResponseEntity<Page<Question>> allQuestionsWithAnswer(
			@RequestParam(required = false, defaultValue = "0") Integer pageNo,
			@RequestParam(required = false, defaultValue = "10") Integer offset) {
		log.info("All questions from repo by user requesting page - {},size-{}", pageNo, offset);
		return ResponseEntity.status(HttpStatus.OK).body(questionService.allQuestionsWithAnswer(pageNo, offset));
	}
}
