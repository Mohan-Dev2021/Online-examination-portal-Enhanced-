package com.project.oep.exception.custom_dtos;

import lombok.Data;

/**
 * Custom response of exception handler
 * 
 * @author mohanlal.s
 *
 */
@Data
public class ErrorResponse {

	private int status;
	private String message;
	private String uiErrorKey;
}
