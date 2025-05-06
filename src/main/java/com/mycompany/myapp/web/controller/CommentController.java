package com.mycompany.myapp.web.controller;

import com.mycompany.myapp.domain.Member;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.mycompany.myapp.exception.CustomExceptions;
import com.mycompany.myapp.web.dto.base.DefaultRes;

@Api(tags = "명함첩 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController extends BaseController {
    private final CommentService commentService;
    private final MemberRepository memberRepository;

    @ApiOperation(value = "Create Comment API")
    @ApiResponse(code = 200, message = "댓글 작성 성공")
    @PostMapping
    public ResponseEntity createComment(@RequestBody CommentRequestDto.CreateCommentDto request){
        try {
            logger.info("Received request: method={}, path={}, description={}", "POST", "/api/comments", "Create Comment API");

            // 임시 : 닉네임으로 유저 조회 (추후 JWT 기반 인증 연동 예정)
            Member member = memberRepository.getByNickname("오리난쟁이");
            commentService.createComment(request, member);

            return new ResponseEntity( DefaultRes.res(StatusCode.OK, ResponseMessage.CREATE_COMMENT_SUCCESS), HttpStatus.OK);
        } catch (CustomExceptions.testException e) {
            return handleApiException(e, HttpStatus.BAD_REQUEST);
        }
    }

}