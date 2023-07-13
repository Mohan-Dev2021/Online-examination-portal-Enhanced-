package com.project.oep.auth_service.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {

	STUDENT("student"), STAFF("staff");

	private final String role;
}
