package com.project.oep.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

	@NotBlank(message = "Email is mandatory")
	private String emailId;

	@NotBlank(message = "password cannot be mandatory")
	private String password;
}
