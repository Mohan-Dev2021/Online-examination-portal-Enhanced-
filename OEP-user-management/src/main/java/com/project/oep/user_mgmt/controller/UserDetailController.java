package com.project.oep.user_mgmt.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.project.oep.auth_service.utils.Constants;
import com.project.oep.dtos.LoginRequest;
import com.project.oep.dtos.LoginResponse;
import com.project.oep.dtos.SignUpResponse;
import com.project.oep.models.Staff;
import com.project.oep.models.Student;
import com.project.oep.user_mgmt.service.UserDetailService;

import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/online-examination-portal")
@Validated
@Slf4j
public class UserDetailController {

	@Autowired
	private UserDetailService userDetailService;

	/**
	 * Authentication gateway named login using Spring core and web config security
	 *
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) throws Exception {
		log.info("Login security gateway called by - {}", request.getEmailId());
		return ResponseEntity.status(HttpStatus.OK)
				.body(userDetailService.login(request.getEmailId(), request.getPassword()));
	}

	/**
	 * Registration api for trainer who has admin role to do authenticated matters
	 * in this application
	 *
	 * @param requestTrainer
	 * @return
	 */
	@PostMapping("/staff-signup")
	public ResponseEntity<SignUpResponse> staffSignUp(@Valid @RequestBody Staff requestTrainer) {
		log.info("Trainer registration service called by - {}", requestTrainer.getName());
		return ResponseEntity.status(HttpStatus.CREATED).body(userDetailService.staffSignUp(requestTrainer));
	}

	/**
	 * Get all trainer by their names in Sort response [Secured by Spring security]
	 * 
	 * @param name
	 * @return
	 */
	@Secured(value = Constants.HAS_ROLE)
	@GetMapping("/staff/{name}")
	public ResponseEntity<List<Staff>> getTrainerByName(@Valid @PathVariable(required = true) String name) {
		return ResponseEntity.status(HttpStatus.OK).body(userDetailService.getTrainerByName(name));
	}

	@Secured(value = Constants.HAS_ROLE)
	@GetMapping("/student/{userName}")
	public ResponseEntity<Student> getStudentsByUserName(@Valid @PathVariable(required = true) String userName) {
		return ResponseEntity.status(HttpStatus.OK).body(userDetailService.getStudentsByUserName(userName));
	}

	@Secured(value = Constants.HAS_ROLE)
	@PutMapping("/student/permission")
	public ResponseEntity<String> updateStudentPermissionsByUserName(@Valid @RequestBody Student updatedStudent) {
		return ResponseEntity.status(HttpStatus.ACCEPTED)
				.body(userDetailService.updateStudentPermissionsByUserName(updatedStudent));
	}

	/**
	 * SignUp for Student who is going to attend the online examinations
	 *
	 * @param requestStudent
	 * @return
	 */
	@PostMapping("/student-signup")
	public ResponseEntity<SignUpResponse> studentSignUp(@Valid @RequestBody Student requestStudent) {
		log.info("Student registration service called by - {}", requestStudent.getName());
		return ResponseEntity.status(HttpStatus.CREATED).body(userDetailService.studentSignUp(requestStudent));
	}
}
