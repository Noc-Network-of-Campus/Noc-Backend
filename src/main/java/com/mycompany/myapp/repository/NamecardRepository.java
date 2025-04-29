package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.NameCard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NamecardRepository extends JpaRepository<NameCard, Long> {
}
