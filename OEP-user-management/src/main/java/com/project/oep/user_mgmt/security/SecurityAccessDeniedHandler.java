package com.project.oep.user_mgmt.security;

import java.io.IOException;
import java.io.Serializable;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SecurityAccessDeniedHandler implements AccessDeniedHandler, Serializable {

	private static final long serialVersionUID = -7858869558953243875L;

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (ObjectUtils.isNotEmpty(auth)) {
			log.warn("User: " + auth.getName() + " attempted to access the protected URL: " + request.getRequestURI());
		}
//		response.sendRedirect(request.getContextPath() + "/accessDenied");
		response.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden - Access denied");
		log.error("Forbidden - Access denied");
	}

//	@Override
//	public void commence(HttpServletRequest request, HttpServletResponse response,
//			AuthenticationException authException) throws IOException, ServletException {
//		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Restricted - Unauthorized");
//	}
}