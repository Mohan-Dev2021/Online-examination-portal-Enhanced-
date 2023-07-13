package com.project.oep.exam_mgmt.rest_client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.project.oep.dtos.LoginRequest;
import com.project.oep.dtos.LoginResponse;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class LoginClient {

	/**
	 * Base url of user management module
	 */
	@Value("${oep.user-management.api}")
	private String userManagementBaseUrl;

	/**
	 * Method to generate the login token from the user management system (using
	 * hardcoded credentials in properties)
	 * 
	 * @param request
	 * @return
	 */
	public LoginResponse login(LoginRequest request) {
		String url = new StringBuilder().append(userManagementBaseUrl).append("/login").toString();
		log.info("Login api url :{}", url);
		HttpEntity<LoginRequest> credentailsrequest = new HttpEntity<>(request);
		log.info("Login api request :{}", credentailsrequest);
		ResponseEntity<LoginResponse> loginResponse = new RestTemplate().exchange(url, HttpMethod.POST,
				credentailsrequest, LoginResponse.class);
		log.info("Login api response :{}", loginResponse);
		return loginResponse.getBody();
	}

}
