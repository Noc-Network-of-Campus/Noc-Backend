package com.mycompany.myapp.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.mycompany.myapp.domain.RefreshToken;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {
	Optional<RefreshToken> findByRefreshToken(String refreshToken);
}
