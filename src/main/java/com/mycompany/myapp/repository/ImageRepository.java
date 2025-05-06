package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
