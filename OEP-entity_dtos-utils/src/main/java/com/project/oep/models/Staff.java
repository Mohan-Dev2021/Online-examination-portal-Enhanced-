package com.project.oep.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.project.oep.auth_service.utils.Role;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "staff")
public class Staff {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotBlank(message = "Name can't be Empty")
	private String name;

	@NotBlank(message = "technology is  mandatory")
	private String technology;

	@NotBlank(message = "Mention your location")
	private String location;

	@NotBlank(message = "Enter your contact")
	@Pattern(regexp = "[0-9]+", message = "allows only numeric values")
	private String contact;

	@Email(message = "Email is not valid", regexp = "^[A-Za-z0-9+_.-]+@(.+)$")
	@NotBlank(message = "Email is  mandatory")
	private String email;

	@Enumerated(EnumType.STRING)
	private Role role;

	@NotBlank(message = "Mention your unique user name")
	private String userName;

	@NotBlank(message = "Password is mandatory")
	@JsonProperty(access = Access.WRITE_ONLY) 
	private String password;

}
