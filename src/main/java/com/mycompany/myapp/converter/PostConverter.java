package com.mycompany.myapp.converter;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.domain.enums.Category;
import com.mycompany.myapp.web.dto.CommentResponseDto;
import com.mycompany.myapp.web.dto.PostRequestDto;
import com.mycompany.myapp.web.dto.PostResponseDto;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class PostConverter {

    private final CommentConverter commentConverter;

    public Post toPost(PostRequestDto.CreatePostRequest request, Category category, Point location, Member member){
        return Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .category(category)
                .location(location)
                .member(member)
                .commentCount(0)
                .likeCount(0)
                .build();
    }

    public PostDocument toPostDocument(Post post){
                return PostDocument.builder()
                        .id(post.getId())
                        .title(post.getTitle())
//                        .content(post.getContent())
//                        .category(post.getCategory().name())
//                        .createdAt(post.getCreatedAt())
                        .build();
    }

    public PostDocument toPostDoc(String  title, Long postId){
        return PostDocument.builder()
                .id(postId)
                .title(title)
//                        .content(post.getContent())
//                        .category(post.getCategory().name())
//                        .createdAt(post.getCreatedAt())
                .build();
    }

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

    // 본문 앞 50글자 요약
    private String getPreviewContent(String content) {
        if (content == null) return "";
        return content.length() <= 50 ? content : content.substring(0, 50) + "...";
    }

    // sort_order == 0인 이미지의 url 반환
    private String getThumbnailImageUrl(Post post) {
        if (post.getImages() == null) return null;

        return post.getImages().stream()
                .filter(image -> image.getSortOrder() == 0)
                .findFirst()
                .map(Image::getImageUrl)
                .orElse(null);
    }

    public PostResponseDto.PostDetailDto toPostDetailDto(Post post, Member member) {
        List<String> imageUrls = post.getImages().stream()
                .map(Image::getImageUrl)
                .collect(Collectors.toList());

        List<CommentResponseDto.CommentDetailDto> commentDtos = post.getComments().stream()
                .map(comment -> commentConverter.toCommentDetailDto(comment, member))
                .collect(Collectors.toList());

        return PostResponseDto.PostDetailDto.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .nickname(member.getNickname())
                .content(post.getContent())
                .likeCount(post.getLikeCount())
                .commentCount(post.getCommentCount())
                .category(post.getCategory().name())
                .images(imageUrls)
                .isMyPost(post.getMember().getId().equals(member.getId()))
                .isLiked(post.getPostLikes().stream()
                        .anyMatch(like -> like.getMember().getId().equals(member.getId())))
                .comments(commentDtos)
                .createdAt(post.getCreatedAt())
                .build();
    }

    public PostLike toPostLike(Post post, Member member){
        return PostLike.builder()
                .post(post)
                .member(member)
                .build();
    }
}

