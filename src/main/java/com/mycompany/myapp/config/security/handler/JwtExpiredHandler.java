package com.mycompany.myapp.config.security.handler;

import org.springframework.security.core.AuthenticationException;

public class JwtExpiredHandler extends AuthenticationException {
	public JwtExpiredHandler(String msg) {
		super(msg);
	}
}
