package com.mycompany.myapp.domain.Impl;

import lombok.*;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostLikeId implements Serializable {

    private Long memberId;
    private Long postId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PostLikeId)) return false;
        PostLikeId that = (PostLikeId) o;
        return Objects.equals(memberId, that.memberId) &&
                Objects.equals(postId, that.postId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(memberId, postId);
    }
}
