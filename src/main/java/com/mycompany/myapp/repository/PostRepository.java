package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Post;
import com.mycompany.myapp.domain.enums.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository  extends JpaRepository<Post, Long> {

    Page<Post> findByCategory(Category category, Pageable pageable);

    @Query(value = "SELECT *, ST_Distance_Sphere(location, ST_GeomFromText(:point, 4326)) AS distance " +
            "FROM post " +
            "WHERE (:category IS NULL OR category = :category) " +
            "AND ST_Distance_Sphere(location, ST_GeomFromText(:point, 4326)) <= :radius",
            nativeQuery = true)
    List<Post> findByCategoryAndLocation(
            @Param("category") String category,
            @Param("point") String pointWKT,
            @Param("radius") double radius);
}
