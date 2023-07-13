package com.project.oep.dtos;

import jakarta.annotation.Nonnull;
import lombok.Data;

@Data
public class AnswerRequest {

	@Nonnull
	private Long questionId;
	
	@Nonnull
	private Long answerId;
}
