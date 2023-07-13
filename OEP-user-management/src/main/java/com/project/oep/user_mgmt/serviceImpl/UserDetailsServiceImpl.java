package com.project.oep.user_mgmt.serviceImpl;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.oep.auth_service.utils.Constants;
import com.project.oep.auth_service.utils.Role;
import com.project.oep.dtos.LoginResponse;
import com.project.oep.dtos.SignUpResponse;
import com.project.oep.exception.custom_dtos.OEPCustomException;
import com.project.oep.exception.enums.ErrorDetails;
import com.project.oep.models.Staff;
import com.project.oep.models.Student;
import com.project.oep.user_mgmt.repo.StaffRepo;
import com.project.oep.user_mgmt.repo.StudentRepo;
import com.project.oep.user_mgmt.security.JwtTokenUtil;
import com.project.oep.user_mgmt.service.UserDetailService;

import java.util.*;

@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService, UserDetailService {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private StaffRepo staffRepo;

	@Autowired
	private StudentRepo studentRepo;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	public UserDetails loadUserByUsername(String emailId) throws UsernameNotFoundException {
		Staff trainer = this.getTrainer(emailId);
		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority("admin"));
		return new User(trainer.getEmail(), trainer.getPassword(), authorities);
	}

	private void authenticate(String username, String password) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
			log.info("User authenticated");
		} catch (DisabledException e) {
			log.error("Error occured from security - {} ", e.toString());
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			log.error("Error occured from security by credentials - {} ", e.toString());
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}

	@Override
	public LoginResponse login(String emailId, String password) throws Exception {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		Staff user = this.getTrainer(emailId);
		if (passwordEncoder.matches(password, user.getPassword())) {
			authenticate(emailId, password);
			return getLoginResponse(user);
		}
		log.error("Error occured from user repo");
		throw new OEPCustomException(ErrorDetails.USER_NOT_FOUND);
	}

	private LoginResponse getLoginResponse(Staff user) {
		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority(Constants.ROLE));
		Map<String, Object> claims = new HashMap<>();
		claims.put("name", user.getName());
		claims.put("role", Constants.ROLE);
		final String token = jwtTokenUtil.generateToken(new User(user.getEmail(), user.getPassword(), authorities),
				claims);
		LoginResponse response = new LoginResponse();
		response.setToken(token);
		response.setUserName(user.getName());
		response.setEmailId(user.getEmail());
		response.setRole(Role.STAFF.getRole());
		return response;
	}

	public Staff getTrainer(String emailId) {
		try {
			Optional<Staff> existingUser = staffRepo.findByEmail(emailId);
			if (existingUser.isPresent()) {
				return existingUser.get();
			}
			throw new OEPCustomException(ErrorDetails.USER_NOT_FOUND);
		} catch (Exception e) {
			throw new OEPCustomException(ErrorDetails.USER_NOT_FOUND);
		}
	}

	@Override
	public SignUpResponse staffSignUp(Staff requestTrainer) {
		Optional<Staff> userRequest = staffRepo.findByEmail(requestTrainer.getEmail());
		if (userRequest.isPresent()) {
			log.error("Already exist record from trainer repo - {} ", userRequest.toString());
			throw new OEPCustomException(ErrorDetails.USER_EXIST);
		}
		// Encoding password
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		Staff user = new Staff();
		user.setName(requestTrainer.getName());
		user.setUserName(requestTrainer.getUserName());
		user.setContact(requestTrainer.getContact());
		user.setEmail(requestTrainer.getEmail());
		user.setTechnology(requestTrainer.getTechnology());
		user.setLocation(requestTrainer.getLocation());
		user.setPassword(passwordEncoder.encode(requestTrainer.getPassword()));
		user.setRole(Role.STAFF);
		staffRepo.save(user);
		log.info("Trainer saved to Db successfully - {} ", user.toString());
		return signUpResponseForTrainer(user);
	}

	private SignUpResponse signUpResponseForTrainer(Staff user) {
		return new SignUpResponse(user.getEmail(), user.getName(), Constants.REGISTERED_SUCCESSFULLY,
				user.getRole().equals(Role.STAFF) ? Role.STAFF : Role.STUDENT);
	}

	@Override
	public SignUpResponse studentSignUp(Student requestTrainee) {
		// Encoding password
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

		Student user = new Student();
		user.setName(requestTrainee.getName());
		user.setContact(requestTrainee.getContact());
		user.setDomain(requestTrainee.getDomain());
		user.setEducation(requestTrainee.getEducation());
		user.setLocation(requestTrainee.getLocation());
		user.setPassOutYear(requestTrainee.getPassOutYear());
		user.setTechnology(requestTrainee.getTechnology());
		user.setUserName(requestTrainee.getUserName());
		user.setPassword(passwordEncoder.encode(requestTrainee.getPassword()));
		user.setTrainerUserName(requestTrainee.getTrainerUserName());
		user.setRole(Role.STUDENT);
		user.setIsAllowedForExam(false);
		studentRepo.save(user);
		log.info("Trainee saved to Db - {} ", user.toString());
		return signUpResponseForTrainee(user);
	}

	private SignUpResponse signUpResponseForTrainee(Student user) {
		return new SignUpResponse(user.getUserName(), user.getName(), Constants.REGISTERED_SUCCESSFULLY,
				user.getRole().equals(Role.STAFF) ? Role.STAFF : Role.STUDENT);
	}

	@Override
	public List<Staff> getTrainerByName(String name) {
		return staffRepo.findAll(Sort.by(Sort.Direction.ASC, "name"));
	}

	@Override
	public Student getStudentsByUserName(String userName) {
		Student studentByName = studentRepo.findByUserName(userName);
		if (ObjectUtils.isNotEmpty(studentByName)) {
			return studentByName;
		} else {
			throw new OEPCustomException(ErrorDetails.USER_NOT_FOUND);
		}
	}

	@Override
	public String updateStudentPermissionsByUserName(Student updatedStudent) {
		Student studentByUserName = this.getStudentsByUserName(updatedStudent.getUserName());
		studentByUserName.setIsAllowedForExam(true);
		studentRepo.save(studentByUserName);
		log.info("Got permission to attend the exam");
		return Constants.PERMISSION_UPDATED_SUCCESSFULLY;
	}
}
