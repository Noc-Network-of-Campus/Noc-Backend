package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Member;
import com.mycompany.myapp.domain.Post;
import com.mycompany.myapp.domain.enums.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository  extends JpaRepository<Post, Long> {

    // cursor 기반 페이징
    @Query("SELECT p FROM Post p " +
            "WHERE (:lastPostId IS NULL OR p.id < :lastPostId) " +
            "ORDER BY p.id DESC")
    List<Post> findByCursorLatest(@Param("lastPostId") Long lastPostId, Pageable pageable);

    @Query("SELECT p FROM Post p " +
            "WHERE p.category = :category AND (:lastPostId IS NULL OR p.id < :lastPostId) " +
            "ORDER BY p.id DESC")
    List<Post> findByCategoryAndCursorLatest(@Param("category") Category category, @Param("lastPostId") Long lastPostId, Pageable pageable);

    @Query("SELECT p FROM Post p " +
            "WHERE (:lastLikeCount IS NULL OR " +
            "       p.likeCount < :lastLikeCount OR " +
            "       (p.likeCount = :lastLikeCount AND p.id < :lastPostId)) " +
            "ORDER BY p.likeCount DESC, p.id DESC")
    List<Post> findByCursorLike(@Param("lastLikeCount") Integer lastLikeCount,
                                @Param("lastPostId") Long lastPostId,
                                Pageable pageable);

    @Query("SELECT p FROM Post p " +
            "WHERE p.category = :category AND " +
            "      (:lastLikeCount IS NULL OR " +
            "       p.likeCount < :lastLikeCount OR " +
            "       (p.likeCount = :lastLikeCount AND p.id < :lastPostId)) " +
            "ORDER BY p.likeCount DESC, p.id DESC")
    List<Post> findByCategoryAndCursorLike(@Param("category") Category category,
                                           @Param("lastLikeCount") Integer lastLikeCount,
                                           @Param("lastPostId") Long lastPostId,
                                           Pageable pageable);



    //offset 기반 페이징
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


    List<Post> findByMemberOrderByIdDesc(Member member, Pageable pageable);
    List<Post> findByMemberAndIdLessThanOrderByIdDesc(Member member, Long lastPostId, Pageable pageable);
}
