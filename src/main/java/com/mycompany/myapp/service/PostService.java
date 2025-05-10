package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Member;
import com.mycompany.myapp.domain.enums.Category;
import com.mycompany.myapp.domain.enums.LikeResult;
import com.mycompany.myapp.domain.enums.SortType;
import com.mycompany.myapp.web.dto.PostRequestDto;
import com.mycompany.myapp.web.dto.PostResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 게시글(Post) 관련 비즈니스 로직을 처리하는 서비스 인터페이스
 * - 게시글 생성, 수정, 삭제
 * - 커서 기반 및 오프셋 기반 조회
 * - 검색, 좋아요, 사용자 게시글 조회 등 포함
 */
public interface PostService {

    /**
     * 카테고리 및 정렬 기준에 따라 게시글을 커서 기반 조회
     *
     * @param category 카테고리 (null이면 전체)
     * @param sort 정렬 기준 (최신순 or 좋아요순)
     * @param lastPostId 마지막 게시글 ID (커서)
     * @param size 페이지 크기
     * @return 게시글 목록
     */
    List<PostResponseDto.SimplePostDto> getPostsByCategoryWithCursor(Category category, SortType sort, Long lastPostId, Integer size);

    /**
     * 카테고리 및 정렬 기준에 따라 게시글을 페이지 기반(offset) 조회
     *
     * @param category 카테고리
     * @param sort 정렬 기준
     * @param page 페이지 번호 (1부터 시작)
     * @param size 페이지 크기
     * @return 게시글 목록
     */
    List<PostResponseDto.SimplePostDto> getPostsByCategoryWithOffest(Category category, SortType sort, Integer page, Integer size);

    /**
     * 게시글 삭제 (작성자 본인만 가능)
     *
     * @param postId 게시글 ID
     * @param member 현재 로그인한 사용자
     */
    void deletePost(Long postId, Member member);

    /**
     * 게시글 상세 정보 조회
     *
     * @param postId 게시글 ID
     * @param member 현재 로그인한 사용자
     * @return 게시글 상세 정보 DTO
     */
    PostResponseDto.PostDetailDto getPostDetail(Long postId, Member member);

    /**
     * 게시글에 대한 좋아요 토글
     *
     * @param postId 게시글 ID
     * @param member 현재 로그인한 사용자
     * @return 좋아요 결과 (LIKED or UNLIKED)
     */
    LikeResult togglePostLike(Long postId, Member member);

    /**
     * 게시글 수정
     *
     * @param postId 게시글 ID
     * @param member 현재 로그인한 사용자
     * @param request 수정 요청 DTO
     */
    void updatePost(Long postId, Member member, PostRequestDto.UpdatePostDto request);

    /**
     * 게시글 생성 (이미지, 위치 포함)
     *
     * @param request 게시글 생성 요청 DTO
     * @param images 이미지 파일 리스트
     * @param member 현재 로그인한 사용자
     */
    void createPost(PostRequestDto.CreatePostRequest request, List<MultipartFile> images, Member member);

    /**
     * 게시글 제목으로 검색 (커서 기반)
     *
     * @param keyword 검색 키워드
     * @param lastPostId 마지막 게시글 ID (커서)
     * @param size 페이지 크기
     * @return 검색 결과 게시글 목록
     */
    List<PostResponseDto.SimplePostDto> searchByTitleWithCursor(String keyword, Long lastPostId, Integer size);

    /**
     * 로그인한 사용자가 작성한 게시글을 최신순 조회 (커서 기반)
     *
     * @param postId 마지막 게시글 ID (커서)
     * @param size 페이지 크기
     * @param member 현재 로그인한 사용자
     * @return 본인이 작성한 게시글 목록
     */
    List<PostResponseDto.SimplePostDto> getMyPosts(Long postId, Integer size, Member member);
}
