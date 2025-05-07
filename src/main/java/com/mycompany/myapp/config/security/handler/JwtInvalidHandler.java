package com.mycompany.myapp.config.security.handler;

import org.springframework.security.core.AuthenticationException;

public class JwtInvalidHandler extends AuthenticationException {
	public JwtInvalidHandler(String msg) {
		super(msg);
	}
}
