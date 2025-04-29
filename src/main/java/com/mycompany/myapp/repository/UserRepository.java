package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository  extends JpaRepository<User, Long> {
    User getByPhoneNumber(String phoneNumber);
}
