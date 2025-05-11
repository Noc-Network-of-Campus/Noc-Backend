package com.mycompany.myapp.web.controller;

import com.mycompany.myapp.domain.Member;
import com.mycompany.myapp.domain.enums.LikeResult;
import com.mycompany.myapp.exception.ResponseMessage;
import com.mycompany.myapp.exception.StatusCode;
import com.mycompany.myapp.service.CommentService;
import com.mycompany.myapp.service.MemberService;
import com.mycompany.myapp.web.controller.base.BaseController;
import com.mycompany.myapp.web.dto.CommentRequestDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.mycompany.myapp.exception.CustomExceptions;
import com.mycompany.myapp.web.dto.base.DefaultRes;

import javax.validation.Valid;

/**
 * 댓글(Comment) 관련 기능을 제공하는 REST API 컨트롤러
 * - 댓글 등록, 삭제
 * - 댓글 좋아요/취소 토글 기능 포함
 */
@Api(tags = "댓글 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController extends BaseController {
    private final CommentService commentService;
    private final MemberService memberService;

    /**
     * 게시글에 댓글 또는 대댓글 작성
     *
     * @param request 댓글 작성 요청 DTO (내용, parentCommentId 포함 가능)
     * @param postId 댓글이 작성될 게시글 ID
     * @return 댓글 작성 성공 응답
     */
    @ApiOperation(value = "댓글 작성 API")
    @ApiResponse(code = 200, message = "댓글 작성 성공")
    @PostMapping("/post/{postId}/comments")
    public ResponseEntity createComment(@Valid @RequestBody CommentRequestDto.CreateCommentDto request, @PathVariable Long postId){
        try {
            logger.info("Received request: method={}, path={}, description={}", "POST", "/api/post/{postId}/comments", "댓글 작성 API");

            Member member = memberService.getCurrentMember();
            commentService.createComment(request, postId, member);

            return new ResponseEntity( DefaultRes.res(StatusCode.OK, ResponseMessage.CREATE_COMMENT_SUCCESS), HttpStatus.OK);
        } catch (CustomExceptions.testException e) {
            return handleApiException(e, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 댓글에 대한 좋아요 또는 좋아요 취소 토글
     *
     * @param postId 댓글이 포함된 게시글 ID (URI 일관성용, 실질 사용 X)
     * @param commentId 좋아요를 토글할 댓글 ID
     * @return 좋아요/취소 결과 메시지 응답
     */
    @ApiOperation(value = "댓글 좋아요/취소 API")
    @ApiResponse(code = 200, message = "댓글 좋아요/취소 성공")
    @PostMapping("/post/{postId}/comments/{commentId}/like")
    public ResponseEntity toggleCommentLike(@PathVariable Long postId, @PathVariable Long commentId){
        try {
            logger.info("Received request: method={}, path={}, description={}", "POST", "/api/post/{postId}/comments/{commentId}/like", "댓글 좋아요/취소 API");

            Member member = memberService.getCurrentMember();
            LikeResult result = commentService.toggleCommentLike(commentId, member);

            String message = (result == LikeResult.LIKED)
                    ? ResponseMessage.COMMENT_LIKE_SUCCESS
                    : ResponseMessage.COMMENT_UNLIKE_SUCCESS;

            return new ResponseEntity( DefaultRes.res(StatusCode.OK, message), HttpStatus.OK);
        } catch (CustomExceptions.testException e) {
            return handleApiException(e, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 사용자가 작성한 댓글 삭제
     * - 작성자 본인만 삭제 가능
     *
     * @param postId 댓글이 포함된 게시글 ID (URI용, 실제 사용하지 않음)
     * @param commentId 삭제할 댓글 ID
     * @return 댓글 삭제 성공 응답
     */
    @ApiOperation(value = "댓글 삭제 API")
    @ApiResponse(code = 200, message = "댓글 삭제 성공")
    @DeleteMapping("/post/{postId}/comments/{commentId}")
    public ResponseEntity deleteComment(@PathVariable Long postId, @PathVariable Long commentId){
        try {
            logger.info("Received request: method={}, path={}, description={}", "DELETE", "/api/post/{postId}/comments/{commentId}", "댓글 삭제 API");

            Member member = memberService.getCurrentMember();
            commentService.deleteComment(commentId, member);

            return new ResponseEntity( DefaultRes.res(StatusCode.OK, ResponseMessage.DELETE_COMMENT_SUCCESS), HttpStatus.OK);
        } catch (CustomExceptions.testException e) {
            return handleApiException(e, HttpStatus.BAD_REQUEST);
        }
    }

}