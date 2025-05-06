package com.mycompany.myapp.web.controller;

import com.mycompany.myapp.domain.Member;
import com.mycompany.myapp.domain.enums.CommentLikeResult;
import com.mycompany.myapp.exception.ResponseMessage;
import com.mycompany.myapp.exception.StatusCode;
import com.mycompany.myapp.repository.MemberRepository;
import com.mycompany.myapp.service.CommentService;
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

@Api(tags = "댓글 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController extends BaseController {
    private final CommentService commentService;
    private final MemberRepository memberRepository;

    @ApiOperation(value = "댓글 작성 API")
    @ApiResponse(code = 200, message = "댓글 작성 성공")
    @PostMapping
    public ResponseEntity createComment(@RequestBody CommentRequestDto.CreateCommentDto request){
        try {
            logger.info("Received request: method={}, path={}, description={}", "POST", "/api/comments", "댓글 작성 API");

            // 임시 : 닉네임으로 유저 조회 (추후 JWT 기반 인증 연동 예정)
            Member member = memberRepository.getByNickname("오리난쟁이");
            commentService.createComment(request, member);

            return new ResponseEntity( DefaultRes.res(StatusCode.OK, ResponseMessage.CREATE_COMMENT_SUCCESS), HttpStatus.OK);
        } catch (CustomExceptions.testException e) {
            return handleApiException(e, HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value = "댓글 좋아요/취소 API")
    @ApiResponse(code = 200, message = "댓글 좋아요/취소 성공")
    @PostMapping("/{commentId}/like")
    public ResponseEntity toggleCommentLike(@PathVariable Long commentId){
        try {
            logger.info("Received request: method={}, path={}, description={}", "POST", "/api/comments/{comment-id}/like", "댓글 좋아요/취소 API");

            // 임시 : 닉네임으로 유저 조회 (추후 JWT 기반 인증 연동 예정)
            Member member = memberRepository.getByNickname("오리난쟁이");
            CommentLikeResult result = commentService.toggleCommentLike(commentId, member);

            String message = (result == CommentLikeResult.LIKED)
                    ? ResponseMessage.COMMENT_LIKE_SUCCESS
                    : ResponseMessage.COMMENT_UNLIKE_SUCCESS;

            return new ResponseEntity( DefaultRes.res(StatusCode.OK, message), HttpStatus.OK);
        } catch (CustomExceptions.testException e) {
            return handleApiException(e, HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value = "댓글 삭제 API")
    @ApiResponse(code = 200, message = "댓글 삭제 성공")
    @DeleteMapping("/{commentId}")
    public ResponseEntity deleteComment(@PathVariable Long commentId){
        try {
            logger.info("Received request: method={}, path={}, description={}", "DELETE", "/api/comments/{comment-id}", "댓글 삭제 API");

            // 임시 : 닉네임으로 유저 조회 (추후 JWT 기반 인증 연동 예정)
            Member member = memberRepository.getByNickname("오리난쟁이");
            commentService.deleteComment(commentId, member);

            return new ResponseEntity( DefaultRes.res(StatusCode.OK, ResponseMessage.DELETE_COMMENT_SUCCESS), HttpStatus.OK);
        } catch (CustomExceptions.testException e) {
            return handleApiException(e, HttpStatus.BAD_REQUEST);
        }
    }

}