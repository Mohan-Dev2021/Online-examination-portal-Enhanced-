package com.project.oep.user_mgmt.security;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException.Forbidden;
import org.springframework.web.filter.OncePerRequestFilter;

import com.project.oep.auth_service.utils.RawConstants;
import com.project.oep.user_mgmt.serviceImpl.UserDetailsServiceImpl;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

	@Autowired
	private UserDetailsServiceImpl jwtUserDetailsService;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Value("${jwt.secret.token}")
	private String audienceValue;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		final String requestTokenHeader = request.getHeader(RawConstants.AUTHORIZATION);
		String username = null;
		String jwtToken = null;
// JWT Token is in the form "Bearer token". Remove Bearer word and get
// only the Token
		if (requestTokenHeader != null && requestTokenHeader.startsWith(RawConstants.BEARER)) {
			try {
				this.audienceExceptionHandler(request, response);
			} catch (Exception e1) {
				logger.error(e1.toString());
			}
			jwtToken = requestTokenHeader.substring(7);
			try {
				username = jwtTokenUtil.getUsernameFromToken(jwtToken);
			} catch (IllegalArgumentException e) {
				System.out.println("Unable to get JWT Token");
			} catch (ExpiredJwtException e) {
				System.out.println("JWT Token has expired");
			}
		} else {
			logger.warn("JWT Token does not begin with Bearer String");
		}
// Once we get the token validate it.
		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(username);
// if token is valid configure Spring Security to manually set
// authentication
			if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				usernamePasswordAuthenticationToken
						.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
// After setting the Authentication in the context, we specify
// that the current user is authenticated. So it passes the
// Spring Security Configurations successfully.
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			}
		}
		chain.doFilter(request, response);
	}

	/**
	 * Method to validate the request header's audience
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	private void audienceExceptionHandler(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String audience = request.getHeader(RawConstants.AUDIENCE);
		if (StringUtils.isNotEmpty(audience)) {
			Boolean value = audience.matches(audienceValue);
			if (!value) {
				try {
					response.sendError(HttpServletResponse.SC_FORBIDDEN, RawConstants.AUDIENCE_ERROR);
					logger.error("Forbidden - Audience required");
				} catch (Forbidden e) {
					throw new Exception(e);
				}
			}
		} else {
			response.sendError(HttpServletResponse.SC_FORBIDDEN, RawConstants.AUDIENCE_HEADER_ERROR);
			logger.error("Forbidden - Audience header required");
		}
	}
}