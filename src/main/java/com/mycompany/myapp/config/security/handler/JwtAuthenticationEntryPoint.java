package com.mycompany.myapp.config.security.handler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
	private final ObjectMapper objectMapper = new ObjectMapper();
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException authException) throws IOException, ServletException {
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType("application/json;charset=UTF-8");

		Map<String, String> res = new HashMap<>();
		res.put("statusCode", "error");
		res.put("responseMessage", authException.getMessage());

		response.getWriter().write(objectMapper.writeValueAsString(res));
		response.flushBuffer();
	}
}
