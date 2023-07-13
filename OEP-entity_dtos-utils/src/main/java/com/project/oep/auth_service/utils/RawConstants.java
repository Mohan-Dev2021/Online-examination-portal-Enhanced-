package com.project.oep.auth_service.utils;

public interface RawConstants {

	public static final String SUCCESS = "SUCCESS";
	public static final String SUCCESS_CODE = "200";
	public static final String DELETE = " - Delete Successfully";
	public static final String HOST = "http://localhost:8080/authentication/activate?activationCode=";
	public static final String EMAIL_ID_QUERY_PARAM = "&emailId=";
	public static final String FORCE_PASSWORD = "Force";
	public static final String TEMP_PASSWORD = "Kindly check your mail to find temporary password";
	public static final String ACTIVATE_USER = "  Account Activated Successfully";
	public static final String ACTIVATE_ACCOUNT = "Activate your account";
	public static final String RESET_PASSWORD = " Reset your password";
	public static final String UPDATED = " - Updated successfully";
	public static final String TEST_MAIL = "springbootbackend2021@gmail.com";
	public static final String REGISTERED_SUCCESSFULLY = "Successfully registered";
	public static final String AUDIENCE = "audience";
	public static final String AUTHORIZATION = "Authorization";
	public static final String BEARER = "Bearer ";

	// Log Errors
	public static final String USER_NOT_FOUND = "Requesting user not found";
	public static final String VALUES_NOT_FOUND = "Please enter the credentials";
	public static final String USER_ALREADY_EXISTS = "User already exists";
	public static final String SECURITY_UR = "hasAnyAuthority('USER')";
	public static final String SECURITY_AD = "hasAnyAuthority('ADMIN')";
	public static final String SECURITY_AD_UR = "hasAnyAuthority('ADMIN','USER')";
	public static final String AUDIENCE_ERROR = "Forbidden - Audience required";
	public static final String AUDIENCE_HEADER_ERROR = "Forbidden - Audience header required";
}
