package com.project.oep.user_mgmt.service;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;

import com.project.oep.dtos.LoginResponse;
import com.project.oep.dtos.SignUpResponse;
import com.project.oep.models.Staff;
import com.project.oep.models.Student;

import jakarta.validation.Valid;

public interface UserDetailService {

	LoginResponse login(String emailId, String password) throws Exception;

	SignUpResponse staffSignUp(@Valid Staff requestTrainer);

	List<Staff> getTrainerByName(String name);

	SignUpResponse studentSignUp(@Valid Student requestStudent);

	Student getStudentsByUserName(String userName);

	String updateStudentPermissionsByUserName(Student updatedStudent);

}
