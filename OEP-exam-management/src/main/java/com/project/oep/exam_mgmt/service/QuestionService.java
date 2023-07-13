package com.project.oep.exam_mgmt.service;

import org.springframework.data.domain.Page;

import com.project.oep.models.AssignQuestionVo;
import com.project.oep.models.Question;

public interface QuestionService {

	Question addQuestion(Question question);

	String removeQuestion(Long id);

	AssignQuestionVo setQuestionDetails(AssignQuestionVo question);

	Page<Question> allQuestionsWithAnswer(Integer pageNo, Integer offset);

}
