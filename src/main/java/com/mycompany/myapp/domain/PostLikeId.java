package com.mycompany.myapp.domain;

import lombok.*;
import java.io.Serializable;

/**
 * - post_id와 member_id의 복합 키 클래스
 * - 한 사용자는 하나의 게시글에 한 번만 좋아요를 누를 수 있도록 제한
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class PostLikeId implements Serializable {
    private Long post;
    private Long member;
}
