package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
