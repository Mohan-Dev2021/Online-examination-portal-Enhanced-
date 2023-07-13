package com.project.oep.auth_service.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum QuestionType {

	SINGLE_CHOICE (1), MULTIPLE_CHOICE(2);
	
	private Integer code;
}
