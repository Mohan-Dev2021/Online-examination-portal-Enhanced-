package com.project.oep.exam_mgmt.rest_client;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.project.oep.dtos.LoginRequest;
import com.project.oep.dtos.LoginResponse;
import com.project.oep.models.Student;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class UserManagementRestClient {

	/**
	 * Initializing login client component for token generation
	 */
	@Autowired
	private LoginClient loginClient;

	/**
	 * Base url of user management module
	 */
	@Value("${oep.user-management.api}")
	private String userManagementBaseUrl;

	/**
	 * Admin credentails username from the properties
	 */
	@Value("${oep.admin.username}")
	private String adminUsername;

	/**
	 * Admin credentails password from the properties
	 */
	@Value("${oep.admin.password}")
	private String adminPassword;

	/**
	 * Audience property for authentication
	 */
	@Value("${jwt.secret.token}")
	private String audience;

	/**
	 * Mapping api urls od student apis
	 */
	private static final String STUDENT_BY_USERNAME = "/student/";
	private static final String STUDENT_PERMISSION = "/student/permission";

	/**
	 * Method to get the student details by username using rest call
	 * 
	 * @param userName
	 * @return
	 */
	public Student getStudentsByUserName(String userName) {
		String url = new StringBuilder().append(userManagementBaseUrl).append(STUDENT_BY_USERNAME).append(userName)
				.toString();
		log.info("Api url load data by username:{}", url);
		HttpHeaders headers = new HttpHeaders();
		LoginResponse loginresponse = loginClient.login(new LoginRequest(adminUsername, adminPassword));
		log.info("Login rest call reponse:{}", loginresponse);
		headers.add("Authorization", "Bearer " + loginresponse.getToken());
		headers.add("audience", audience);
		log.info("Headers before going to rest call:{}", headers);
		ResponseEntity<Student> studentResponse = this.getRestClientResponse(url, HttpMethod.GET,
				new HttpEntity<>(headers), Student.class);
		log.info("Student api response:{}", studentResponse);
		return ObjectUtils.isNotEmpty(studentResponse.getBody()) ? studentResponse.getBody() : null;

	}

	/**
	 * Method to update the permission for student to all the exam
	 * 
	 * @param updatedStudent
	 * @return
	 */
	public Student updateStudentPermissionsByUserName(Student updatedStudent) {
		String url = new StringBuilder().append(userManagementBaseUrl).append(STUDENT_PERMISSION).toString();
		log.info("Student perimitting api url:{}", url);
		HttpHeaders headers = new HttpHeaders();
		LoginResponse loginresponse = loginClient.login(new LoginRequest(adminUsername, adminPassword));
		log.info("Login api rest call response :{}", loginresponse);
		headers.add("Authorization", "Bearer " + loginresponse.getToken());
		headers.add("audience", audience);
		log.info("Headers before requesting rest call:{}", headers);
		ResponseEntity<Student> permissionResponse = this.getRestClientResponse(url, HttpMethod.PUT,
				new HttpEntity<Student>(updatedStudent, headers), Student.class);
		log.info("Api response :{}", permissionResponse);
		return ObjectUtils.isNotEmpty(permissionResponse.getBody()) ? permissionResponse.getBody() : null;
	}

	/**
	 * Generic method to pull the data from the other module by rest call way using
	 * generics and rest template bean
	 * 
	 * @param <T>
	 * @param url
	 * @param method
	 * @param entity
	 * @param responseType
	 * @return
	 */
	private <T> ResponseEntity<T> getRestClientResponse(String url, HttpMethod method, HttpEntity<?> entity,
			Class<T> responseType) {
		ResponseEntity<T> profileResponse = new RestTemplate().exchange(url, method, entity, responseType);
		return profileResponse;
	}

}
