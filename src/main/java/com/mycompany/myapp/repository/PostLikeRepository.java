package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    Optional<PostLike> findByPostAndMember(Post post, Member member);
}
