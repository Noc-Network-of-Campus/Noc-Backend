package com.mycompany.myapp.web.controller;

import com.mycompany.myapp.domain.Member;
import com.mycompany.myapp.domain.enums.Category;
import com.mycompany.myapp.domain.enums.SortType;
import com.mycompany.myapp.exception.CustomExceptions;
import com.mycompany.myapp.exception.ResponseMessage;
import com.mycompany.myapp.exception.StatusCode;
import com.mycompany.myapp.repository.MemberRepository;
import com.mycompany.myapp.service.PostService;
import com.mycompany.myapp.web.controller.base.BaseController;
import com.mycompany.myapp.web.dto.PostResponseDto;
import com.mycompany.myapp.web.dto.base.DefaultRes;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "게시글 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post")
public class PostController extends BaseController {

    private final MemberRepository memberRepository;
    private final PostService postService;

    @ApiOperation(value = "카테고리별 게시글 조회 API", notes = "카테고리, 정렬 방식, 페이지 번호, 페이지 크기를 기준으로 게시글을 조회합니다.")
    @ApiResponse(code = 200, message = "댓글 작성 성공")
    @GetMapping("/list")
    public ResponseEntity getPostListByCategory(@ApiParam(value = "카테고리. 없으면 전체 게시글", example = "FOOD")
                                                    @RequestParam(required = false) Category category,
                                                @ApiParam(value = "정렬 방식 (LIKE, LATEST). 기본값은 LATEST", example = "LATEST")
                                                    @RequestParam(required = false, defaultValue = "LATEST") SortType sort,
                                                @ApiParam(value = "페이지 번호", example = "1", required = true)
                                                    @RequestParam Integer page,
                                                @ApiParam(value = "한 페이지당 게시글 수", example = "10", required = true)
                                                    @RequestParam Integer size){
        try {
            logger.info("Received request: method={}, path={}, description={}", "GET", "/api/post/list", "카테고리별 게시글 조회 API");

            // 임시 : 닉네임으로 유저 조회 (추후 JWT 기반 인증 연동 예정)
            Member member = memberRepository.getByNickname("오리난쟁이");

            List<PostResponseDto.SimplePostDto> res = postService.getPostsByCategory(category, sort, page, size);

            return new ResponseEntity( DefaultRes.res(StatusCode.OK, ResponseMessage.READ_POST_SUCCESS, res), HttpStatus.OK);
        } catch (CustomExceptions.testException e) {
            return handleApiException(e, HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value = "게시글 삭제 API")
    @ApiResponse(code = 200, message = "게시글 삭제 성공")
    @DeleteMapping("/{postId}")
    public ResponseEntity deletePost(@PathVariable Long postId){
        try {
            logger.info("Received request: method={}, path={}, description={}", "DELETE", "/api/post/{post-id}", "게시글 삭제 API");

            // 임시 : 닉네임으로 유저 조회 (추후 JWT 기반 인증 연동 예정)
            Member member = memberRepository.getByNickname("오리난쟁이");
            postService.deletePost(postId, member);

            return new ResponseEntity( DefaultRes.res(StatusCode.OK, ResponseMessage.DELETE_POST_SUCCESS), HttpStatus.OK);
        } catch (CustomExceptions.testException e) {
            return handleApiException(e, HttpStatus.BAD_REQUEST);
        }
    }
}
