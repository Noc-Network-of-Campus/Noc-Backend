package com.mycompany.myapp.web.controller;

import com.mycompany.myapp.domain.Member;
import com.mycompany.myapp.domain.enums.LikeResult;
import com.mycompany.myapp.exception.ResponseMessage;
import com.mycompany.myapp.exception.StatusCode;
import com.mycompany.myapp.repository.MemberRepository;
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

@Api(tags = "댓글 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController extends BaseController {
    private final CommentService commentService;
    private final MemberService memberService;

    @ApiOperation(value = "댓글 작성 API")
    @ApiResponse(code = 200, message = "댓글 작성 성공")
    @PostMapping("/post/{postId}/comments")
    public ResponseEntity createComment(@RequestBody CommentRequestDto.CreateCommentDto request, @PathVariable Long postId){
        try {
            logger.info("Received request: method={}, path={}, description={}", "POST", "/api/post/{postId}/comments", "댓글 작성 API");

            Member member = memberService.getCurrentMember();
            commentService.createComment(request, postId, member);

            return new ResponseEntity( DefaultRes.res(StatusCode.OK, ResponseMessage.CREATE_COMMENT_SUCCESS), HttpStatus.OK);
        } catch (CustomExceptions.testException e) {
            return handleApiException(e, HttpStatus.BAD_REQUEST);
        }
    }

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