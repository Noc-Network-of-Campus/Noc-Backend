package com.mycompany.myapp.converter;

import com.mycompany.myapp.domain.Image;
import com.mycompany.myapp.domain.Post;
import com.mycompany.myapp.web.dto.PostResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class PostConverter {

    public PostResponseDto.SimplePostDto toSimplePostDto(Post post) {
        return PostResponseDto.SimplePostDto.builder()
                .postId(post.getId())
                .category(post.getCategory())
                .title(post.getTitle())
                .content(getPreviewContent(post.getContent()))
                .likeCount(post.getLikeCount())
                .commentCount(post.getCommentCount())
                .thumbnailImageUrl(getThumbnailImageUrl(post))
                .createdAt(post.getCreatedAt())
                .build();
    }

    // 본문 앞 20글자 요약
    private String getPreviewContent(String content) {
        if (content == null) return "";
        return content.length() <= 50 ? content : content.substring(0, 50) + "...";
    }

    // sort_order == 1인 이미지의 url 반환
    private String getThumbnailImageUrl(Post post) {
        if (post.getImages() == null) return null;

        return post.getImages().stream()
                .filter(image -> image.getSortOrder() == 1)
                .findFirst()
                .map(Image::getImageUrl)
                .orElse(null);
    }
}

