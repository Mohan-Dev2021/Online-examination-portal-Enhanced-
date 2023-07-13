package com.project.oep.exam_mgmt.serviceImpl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.oep.auth_service.utils.Constants;
import com.project.oep.dtos.AnswerRequest;
import com.project.oep.dtos.RequestExam;
import com.project.oep.exam_mgmt.repo.AnswerRepo;
import com.project.oep.exam_mgmt.repo.QuestionRepo;
import com.project.oep.exam_mgmt.rest_client.UserManagementRestClient;
import com.project.oep.exam_mgmt.service.ExamService;
import com.project.oep.exception.custom_dtos.OEPCustomException;
import com.project.oep.exception.enums.ErrorDetails;
import com.project.oep.models.Answer;
import com.project.oep.models.Question;
import com.project.oep.models.Student;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ExamServiceImpl implements ExamService {

	@Autowired
	private QuestionRepo questionRepo;

	@Autowired
	private AnswerRepo answerRepo;

//	@Autowired
//	private UserManagementFeignClient userFeignClient;

	@Autowired
	private UserManagementRestClient userRestClient;

	@Override
	public List<Question> loadQuestions(String traineeUsername) {
		Student existTrainee = userRestClient.getStudentsByUserName(traineeUsername);// userClient.getStudentsByUserName(traineeName);
		log.info("Trainee found.....");
		if (!existTrainee.getIsAllowedForExam()) {
			log.error("trainee didn't have permission to attend the exam");
			throw new OEPCustomException(ErrorDetails.DONT_ALLOW_FOR_EXAM);
		}
		List<Question> questions = questionRepo.findAll().stream().filter(qn -> qn.getIsAnswered() == false).sorted()
				.collect(Collectors.toList());
		log.info("Unanswered question from repo");
		return questions;
	}

	@Override
	public String questions(AnswerRequest answer) {
		Answer findCorrectAnswer = answerRepo.findById(answer.getAnswerId()).get();
		if (answer.getQuestionId().equals(findCorrectAnswer.getQuestionId())) {
			if (findCorrectAnswer.getIsCorrectAnswer()) {
				return Constants.RIGHT_ANSWER;
			}
		}
		return Constants.WRONG_ANSWER;
	}

	@Override
	public List<Answer> loadOptions(Long questionId, String traineeUsername) {
		if (StringUtils.isNotEmpty(traineeUsername)) {
			Student existTrainee = userRestClient.getStudentsByUserName(traineeUsername);// userClient.getStudentsByUserName(traineeName);
			log.info("Trainee found.....");
			if (!existTrainee.getIsAllowedForExam()) {
				log.error("trainee didn't have permission to attend the exam");
				throw new OEPCustomException(ErrorDetails.DONT_ALLOW_FOR_EXAM);
			}
		}
		Answer findOne = new Answer();
		findOne.setQuestionId(questionId);
		Example<Answer> id = Example.of(findOne);
		answerRepo.findAll(id).forEach(answer -> {
			answer.setIsCorrectAnswer(
					Boolean.valueOf(StandardCharsets.UTF_8.encode(answer.getIsCorrectAnswer().toString()).toString()));
		});
		return answerRepo.findAll(id);
	}

	@Override
	public String permitForExam(RequestExam requestedTrainee) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		Student existTrainee = userRestClient.getStudentsByUserName(requestedTrainee.getUserName());// userClient.getStudentsByUserName(requestedTrainee.getUserName());
		if (ObjectUtils.isNotEmpty(existTrainee)) {
			if (requestedTrainee.getUserName().equalsIgnoreCase(existTrainee.getUserName())) {
				if (passwordEncoder.matches(requestedTrainee.getPassword(), existTrainee.getPassword())) {
					userRestClient.updateStudentPermissionsByUserName(existTrainee);// userClient.updateStudentPermissionsByUserName(existTrainee);
					return existTrainee.getName() + "- Trainee Allowed to attend the Exam";
				} else {
					log.error("Passwords didn't matches");
					throw new OEPCustomException(ErrorDetails.PASSWORD_DIDNT_MATCHED);
				}
			}
		}
		return "Oops....!!Something went wrong";
	}
}
