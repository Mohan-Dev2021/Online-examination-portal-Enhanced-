package com.project.oep.exception.custom_dtos;

import com.project.oep.exception.enums.ErrorDetails;

import lombok.Data;

@Data
public class OEPCustomException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private ErrorDetails responseDetail;
	private String customMessage;

	public OEPCustomException(ErrorDetails responseDetail) {
		super();
		this.responseDetail = responseDetail;
	}

}
