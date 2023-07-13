package com.project.oep.exam_mgmt.serviceImpl;

import com.project.oep.exam_mgmt.repo.AnswerRepo;
import com.project.oep.exam_mgmt.repo.QuestionRepo;
import com.project.oep.exam_mgmt.service.QuestionService;
import com.project.oep.exception.custom_dtos.OEPCustomException;
import com.project.oep.exception.enums.ErrorDetails;
import com.project.oep.models.Answer;
import com.project.oep.models.AssignQuestionVo;
import com.project.oep.models.Question;
import com.project.oep.models.Staff;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class QuestionServiceImpl implements QuestionService {

	@Autowired
	private QuestionRepo questionRepo;

	@Autowired
	private AnswerRepo answerRepo;

	@Override
	public Question addQuestion(Question question) {
		question.setDate(LocalDate.now());
		question.setIsAnswered(false);
		question.setSubjectId(generateSubjectId(question.getSubject()));
		question.setAnswerKey(answerKeyId(question));
		question.setQuestionType(question.getQuestionType());
		Question returnReponse = questionRepo.save(question);
		log.info("Question saved to repo");
		return returnReponse;
	}

	@Override
	public Page<Question> allQuestionsWithAnswer(Integer pageNo, Integer offset) {
		Pageable paging = PageRequest.of(pageNo, offset);
		log.info("Paging request to access the question for page - {},size-{}", paging.getPageNumber(),
				paging.getPageNumber());
		Page<Question> questions = questionRepo.findAll(paging);
		questions.stream().sorted(Comparator.comparing(Question::getId));
		log.info("Sorted and filtered question final response from repo");
		return questions;
	}

	private String generateSubjectId(String subject) {
		return subject.substring(0, 3) + (questionRepo.count() + 1);
	}

	private String answerKeyId(Question qn) {
		return "A" + questionRepo.count() + 1 + qn.getSubject();
	}

	@Override
	public String removeQuestion(Long id) {
		try {
			questionRepo.deleteById(id);
			return "Question removed";
		} catch (NullPointerException | HttpClientErrorException.NotFound ex) {
			log.error("Resource not found:{}", ex);
			throw new OEPCustomException(ErrorDetails.RESOURCE_NOT_FOUND);
		}
	}

	@Override
	public AssignQuestionVo setQuestionDetails(AssignQuestionVo question) {
		Staff setQuestionStaff = new Staff();
		AssignQuestionVo response = new AssignQuestionVo();
		Question findQuestion = questionRepo.findById(question.getQuestionId())
				.orElseThrow(() -> new OEPCustomException(ErrorDetails.RESOURCE_NOT_FOUND));
		setQuestionStaff.setName(question.getTrainerName());
		setQuestionStaff.setLocation(question.getLocation());
		setQuestionStaff.setTechnology(question.getTechnology());
		findQuestion.setDescription(findQuestion.getDescription());
		findQuestion.setSubject(question.getSubject());
		findQuestion.setQuestionType(findQuestion.getQuestionType());
		findQuestion.setSubjectId(findQuestion.getSubjectId());
		findQuestion.setDate(LocalDate.now());
		findQuestion.setIsAnswered(false);
		findQuestion.setAnswerKey(question.getAnswerKey());
		List<Answer> savedOptions = new ArrayList<>();
		Answer answerVo = new Answer();
		question.getAnswerVo().stream().forEach(answer -> {
			answer.setQuestionId(findQuestion.getId());
			answerVo.setAnswer(answer.getAnswer());
			answerVo.setIsCorrectAnswer(answer.getIsCorrectAnswer());
		});
		answerVo.setQuestionId(findQuestion.getId());
		savedOptions.addAll(question.getAnswerVo());
		questionRepo.save(findQuestion);
		List<Answer> options = answerRepo.saveAll(savedOptions);
		response.setQuestionId(findQuestion.getId());
		response.setTrainerName(setQuestionStaff.getName());
		response.setLocation(setQuestionStaff.getLocation());
		response.setTechnology(setQuestionStaff.getTechnology());
		response.setSubject(findQuestion.getSubject());
		response.setQuestionDescription(findQuestion.getDescription());
		response.setAnswerKey(findQuestion.getAnswerKey());
		response.setQuestionType(findQuestion.getQuestionType());
		response.setAnswerVo(options);
		response.setTime(LocalDateTime.now());
		response.setIsAnswered(findQuestion.getIsAnswered());
		return response;
	}
}
