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

    /**
     * 게시글 작성 요청 DTO를 기반으로 Post 엔티티 생성
     *
     * @param request 게시글 작성 요청 DTO
     * @param category 카테고리 (ENUM)
     * @param location 위치 정보 (Point)
     * @param member 작성자
     * @return Post 엔티티
     */
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

    /**
     * 게시글(Post)을 Elasticsearch 문서(PostDocument)로 변환
     *
     * @param post Post 엔티티
     * @return PostDocument 객체
     */
    public PostDocument toPostDocument(Post post){
                return PostDocument.builder()
                        .id(post.getId())
                        .title(post.getTitle())
//                        .content(post.getContent())
//                        .category(post.getCategory().name())
//                        .createdAt(post.getCreatedAt())
                        .build();
    }

    /**
     * 게시글 ID와 제목만으로 Elasticsearch 문서 생성
     * (예: 제목만 수정된 경우 업데이트 용도)
     *
     * @param title 수정된 제목
     * @param postId 게시글 ID
     * @return PostDocument 객체
     */
    public PostDocument toPostDoc(String  title, Long postId){
        return PostDocument.builder()
                .id(postId)
                .title(title)
//                        .content(post.getContent())
//                        .category(post.getCategory().name())
//                        .createdAt(post.getCreatedAt())
                .build();
    }

    /**
     * Post 엔티티를 간단한 게시글 DTO로 변환
     * (리스트용)
     *
     * @param post Post 엔티티
     * @return SimplePostDto 응답 DTO
     */
    public PostResponseDto.SimplePostDto toSimplePostDto(Post post) {
        return PostResponseDto.SimplePostDto.builder()
                .postId(post.getId())
                .category(post.getCategory())
                .title(post.getTitle())
                .content(getPreviewContent(post.getContent()))
                .likeCount(post.getLikeCount())
                .commentCount(post.getCommentCount())
                .imageUrl(getThumbnailImageUrl(post))
                .createdAt(post.getCreatedAt())
                .build();
    }

    /**
     * 게시글 본문의 앞 50자만 잘라 요약 형태로 반환
     *
     * @param content 원본 본문
     * @return 요약된 문자열
     */
    // 본문 앞 50글자 요약
    private String getPreviewContent(String content) {
        if (content == null) return "";
        return content.length() <= 50 ? content : content.substring(0, 50) + "...";
    }

    /**
     * 게시글의 대표 이미지 URL을 추출
     * (sortOrder == 0인 이미지를 대표로 사용)
     *
     * @param post Post 엔티티
     * @return 썸네일 이미지 URL (없으면 null)
     */
    // sort_order == 0인 이미지의 url 반환
    private String getThumbnailImageUrl(Post post) {
        if (post.getImages() == null) return null;

        return post.getImages().stream()
                .filter(image -> image.getSortOrder() == 0)
                .findFirst()
                .map(Image::getImageUrl)
                .orElse(null);
    }

    /**
     * 게시글 상세 응답 DTO로 변환
     *
     * @param post 게시글
     * @param member 현재 요청한 사용자
     * @return PostDetailDto
     */
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

    /**
     * 좋아요 클릭 시 사용할 PostLike 엔티티 생성
     *
     * @param post 게시글
     * @param member 사용자
     * @return PostLike 엔티티
     */
    public PostLike toPostLike(Post post, Member member){
        return PostLike.builder()
                .post(post)
                .member(member)
                .build();
    }
}

