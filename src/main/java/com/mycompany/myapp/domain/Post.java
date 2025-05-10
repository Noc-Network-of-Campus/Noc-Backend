package com.mycompany.myapp.domain;

import com.mycompany.myapp.domain.base.BaseEntity;
import com.mycompany.myapp.domain.enums.Category;
import lombok.*;
import org.locationtech.jts.geom.Point;

import javax.persistence.*;
import java.util.List;

/**
 * 사용자가 작성한 게시글(Post)을 나타내는 엔티티
 * - 제목, 본문, 카테고리, 위치정보 포함
 * - 댓글, 이미지, 좋아요 등의 연관 엔티티와 연계됨
 * - 댓글 수 및 좋아요 수는 직접 관리
 */
@Entity
@Getter @Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Member member;

    @Column(nullable = false)
    private String title;

    @Lob
    private String content;

    @Enumerated(EnumType.STRING)
    private Category category;

    /** 게시글 작성 위치 (위도/경도 정보) */
    @Column(columnDefinition = "POINT")
    private Point location;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostLike> postLikes;

    private Integer commentCount;
    private Integer likeCount;

    public void increaseCommentCount() {
        if (this.commentCount == null) {
            this.commentCount = 1;
        } else {
            this.commentCount += 1;
        }
    }

    public void decreaseCommentCount(){
        if (this.commentCount > 0) {
            this.commentCount--;
        }
    }

    public void increaseLikeCount() {
        this.likeCount = (this.likeCount == null) ? 1 : this.likeCount + 1;
    }

    public void decreaseLikeCount() {
        this.likeCount = (this.likeCount == null || this.likeCount <= 0) ? 0 : this.likeCount - 1;
    }

    public void updatePost(String title, String content, Category category) {
        this.title = title;
        this.content = content;
        this.category = category;
    }
}
