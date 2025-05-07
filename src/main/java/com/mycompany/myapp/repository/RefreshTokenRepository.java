package com.mycompany.myapp.repository;

import org.springframework.data.repository.CrudRepository;

import com.mycompany.myapp.domain.RefreshToken;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {

}
