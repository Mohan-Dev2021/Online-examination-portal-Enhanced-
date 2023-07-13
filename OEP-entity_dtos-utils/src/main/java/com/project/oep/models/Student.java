package com.project.oep.models;

import com.project.oep.auth_service.utils.Role;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "student")
public class Student {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotBlank(message = "Name can't be Empty")
	private String name;

	@NotBlank(message = "trainer name can't be Empty")
	private String trainerUserName;

	// @NotBlank(message = "technology is mandatory")
	private String technology;

	// @NotBlank(message = "Mention your location")
	private String location;

	// @NotBlank(message = "Contact can't be Empty")
	private String contact;

	private String domain;

	private int passOutYear;

	// @NotBlank(message = "mention your qualification")
	private String education;

	@Enumerated(EnumType.STRING)
	private Role role;

	// @NotBlank(message = "Mention your unique user name")
	private String userName;

	// @NotBlank(message = "password must not be Empty")
	private String password;

	private Boolean isAllowedForExam;
}
