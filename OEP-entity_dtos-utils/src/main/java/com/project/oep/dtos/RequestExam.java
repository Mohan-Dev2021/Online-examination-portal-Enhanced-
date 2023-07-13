package com.project.oep.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RequestExam {

	private String traineeName;

	@NotBlank(message = "username shouldn't be blank")
	private String userName;

	@NotBlank(message = "password shouldn't be blank")
	private String password;
}
