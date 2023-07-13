package com.project.oep.exception.rest_advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import com.project.oep.exception.custom_dtos.ErrorResponse;
import com.project.oep.exception.custom_dtos.OEPCustomException;

/**
 * Global exception controller which is responsible to handle all the exception
 * handling of the application
 * 
 * @author mohanlal.s
 *
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(OEPCustomException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorResponse resourceNotFound(OEPCustomException ex) {
		ErrorResponse response = new ErrorResponse();
		if (ex.getResponseDetail() == null) {
			response.setStatus(400);
			response.setMessage(ex.getCustomMessage());
			response.setUiErrorKey("error_resource_exists_error");
			return response;
		}
		response.setStatus(Integer.parseInt(ex.getResponseDetail().getCode()));
		response.setMessage(ex.getResponseDetail().getMessage());
		response.setUiErrorKey(ex.getResponseDetail().getUiErrorKey());
		return response;
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseBody
	// public Map<String, String>
	// handleValidationExceptions(MethodArgumentNotValidException ex) {
	public ErrorResponse handleValidationExceptions(MethodArgumentNotValidException ex) {

		// Map<String, String> errors = new HashMap<>();

		ErrorResponse response = new ErrorResponse();

		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();

			response.setStatus(400);
			response.setMessage(fieldName + " - " + errorMessage);
			response.setUiErrorKey("error_validation_failure");

			// errors.put(fieldName, errorMessage);
		});
		return response;
	}

	@ExceptionHandler(InternalError.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorResponse internalException(OEPCustomException ex) {
		ErrorResponse response = new ErrorResponse();
		response.setStatus(Integer.parseInt(ex.getResponseDetail().getCode()));
		response.setMessage(ex.getResponseDetail().getMessage());
		response.setUiErrorKey(ex.getResponseDetail().getUiErrorKey());
		return response;
	}

	// MissingServletRequestParameterException for handle request paramater missing
	// exception
	@ExceptionHandler(MissingServletRequestParameterException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorResponse missingServletRequestException(MissingServletRequestParameterException ex) {
		ErrorResponse response = new ErrorResponse();
		response.setStatus(400);
		response.setMessage("Parameter missing exception");
		response.setUiErrorKey("error_validation_failure");
		return response;
	}

	// HttpMessageNotReadable request missing Exception handled
	@ExceptionHandler(HttpMessageNotReadableException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorResponse missingRequestBodyException(HttpMessageNotReadableException ex) {

		ErrorResponse response = new ErrorResponse();
		response.setStatus(400);
		response.setMessage("Request missing exception");
		response.setUiErrorKey("request_missing_error");
		return response;
	}

//	// ValidationException the validation error handled
//	@ExceptionHandler(ValidationException.class)
//	@ResponseStatus(HttpStatus.BAD_REQUEST)
//	@ResponseBody
//	public ErrorResponse emptyRequestException(ValidationException ex) {
//		ErrorResponse response = new ErrorResponse();
//		response.setStatus(400);
//		response.setMessage("Input validation exception");
//		response.setUiErrorKey("validation_error");
//		return response;
//	}

//	// Entity not null exception handled
//	@ExceptionHandler(InvalidDataAccessApiUsageException.class)
//	@ResponseStatus(HttpStatus.BAD_REQUEST)
//	@ResponseBody
//	public AppResponse<ErrorResponse> entityNotNullException(InvalidDataAccessApiUsageException ex) {
//		ErrorResponse response = new ErrorResponse();
//		response.setStatus(404);
//		response.setMessage("Entity must not be null");
//		response.setUiErrorKey("entity_null_exception");
//		return new AppResponse<>(HttpStatus.NOT_FOUND, 404, response);
//	}

//	// SQL Integrity Exception DB Error handled
//	@ExceptionHandler(SQLIntegrityConstraintViolationException.class)
//	@ResponseStatus(HttpStatus.BAD_REQUEST)
//	@ResponseBody
//	public AppResponse<ErrorResponse> foreignKeyConstraintException(SQLIntegrityConstraintViolationException ex) {
//		ErrorResponse response = new ErrorResponse();
//		response.setStatus(400);
//		response.setMessage("Cannot update or delete the entity");
//		response.setUiErrorKey("integrity_constraint_exception");
//		return new AppResponse<>(HttpStatus.BAD_REQUEST, 400, response);
//	}

	// Handled the UnAuthorised Exception
	@ExceptionHandler(HttpClientErrorException.Forbidden.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	@ResponseBody
	public ErrorResponse emptyDataAccessException(HttpClientErrorException.Forbidden ex) {
		ErrorResponse response = new ErrorResponse();
		response.setStatus(403);
		response.setMessage("Didn't have sufficient authorization to access for further");
		response.setUiErrorKey("unknown_access_identified");
		return response;
	}
}