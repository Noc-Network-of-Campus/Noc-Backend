package com.mycompany.myapp.config.security;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import com.mycompany.myapp.config.security.handler.CustomOAuth2SuccessHandler;
import com.mycompany.myapp.config.security.handler.JwtAccessDeniedHandler;
import com.mycompany.myapp.config.security.handler.JwtAuthenticationEntryPoint;
import com.mycompany.myapp.config.security.provider.JwtProvider;
import com.mycompany.myapp.service.CustomOAuth2UserService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	private final CustomOAuth2UserService customOAuth2UserService;
	private final CustomOAuth2SuccessHandler customOAuth2SuccessHandler;
	private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
	private final JwtProvider jwtProvider;

	@Bean
	public JwtFilter jwtFilter() {
		return new JwtFilter(jwtProvider, jwtAuthenticationEntryPoint);
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtFilter jwtFilter) throws Exception {
		http
			.cors(cors -> cors.configurationSource(new CorsConfigurationSource() {
				@Override
				public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
					CorsConfiguration config = new CorsConfiguration();
					config.addAllowedOrigin("http://localhost:3000");
					config.addAllowedOrigin("http://localhost:8080");
					config.addAllowedMethod("*");
					config.addAllowedHeader("*");
					config.setAllowCredentials(true);
					return config;
				}
			}))
			.csrf(AbstractHttpConfigurer::disable)
			.formLogin(AbstractHttpConfigurer::disable)
			.httpBasic(AbstractHttpConfigurer::disable)
			.sessionManagement((session) -> session
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.exceptionHandling(exception -> exception
				.authenticationEntryPoint(jwtAuthenticationEntryPoint)
				.accessDeniedHandler(jwtAccessDeniedHandler)
			)
			.authorizeHttpRequests(request -> request
				.antMatchers(
					"/swagger-ui.html",
					"/swagger-ui/**",
					"/v2/api-docs",
					"/v3/api-docs",
					"/swagger-resources/**",
					"/webjars/**",
					"/api/auth/reissue",
					"/api/map/**"
				).permitAll()
				.anyRequest().authenticated())
			.oauth2Login(oauth2 -> oauth2
				.userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
				.successHandler(customOAuth2SuccessHandler)
			)
			.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}
}
