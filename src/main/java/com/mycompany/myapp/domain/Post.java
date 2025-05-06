package com.mycompany.myapp.domain;

import com.mycompany.myapp.domain.base.BaseEntity;
import com.mycompany.myapp.domain.enums.Category;
import lombok.*;
import org.locationtech.jts.geom.Point;

import javax.persistence.*;
import java.util.List;

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
}
