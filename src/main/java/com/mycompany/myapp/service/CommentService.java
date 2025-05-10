package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Member;
import com.mycompany.myapp.domain.enums.LikeResult;
import com.mycompany.myapp.web.dto.CommentRequestDto;

/**
 * 댓글 관련 서비스 인터페이스
 */
public interface CommentService {

    /**
     * 댓글 또는 대댓글을 생성
     *
     * @param request 댓글 생성 요청 DTO
     * @param postId 댓글이 작성될 게시글 ID
     * @param member 현재 로그인한 사용자
     */
    void createComment(CommentRequestDto.CreateCommentDto request, Long postId, Member member);

    /**
     * 댓글에 대한 좋아요 토글
     * 이미 눌려있으면 취소하고, 없으면 좋아요 등록
     *
     * @param commentId 댓글 ID
     * @param member 현재 로그인한 사용자
     * @return 좋아요 처리 결과 (LIKED or UNLIKED)
     */
    LikeResult toggleCommentLike(Long commentId, Member member);

    /**
     * 댓글을 삭제 처리 (soft delete)
     *
     * @param commentId 삭제할 댓글 ID
     * @param member 요청자 (작성자 본인인지 확인 필요)
     */
    void deleteComment(Long commentId, Member member);
}
