package com.mycompany.myapp.web.controller;

import com.mycompany.myapp.domain.Member;
import com.mycompany.myapp.domain.PostDocument;
import com.mycompany.myapp.domain.enums.Category;
import com.mycompany.myapp.domain.enums.LikeResult;
import com.mycompany.myapp.domain.enums.SortType;
import com.mycompany.myapp.exception.CustomExceptions;
import com.mycompany.myapp.exception.ResponseMessage;
import com.mycompany.myapp.exception.StatusCode;
import com.mycompany.myapp.repository.MemberRepository;
import com.mycompany.myapp.repository.PostSearchRepository;
import com.mycompany.myapp.service.MemberService;
import com.mycompany.myapp.service.PostIndexer;
import com.mycompany.myapp.service.PostService;
import com.mycompany.myapp.web.controller.base.BaseController;
import com.mycompany.myapp.web.dto.PostRequestDto;
import com.mycompany.myapp.web.dto.PostResponseDto;
import com.mycompany.myapp.web.dto.base.DefaultRes;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Api(tags = "게시글 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post")
public class PostController extends BaseController {

    private final MemberService memberService;
    private final PostService postService;
    private final PostSearchRepository postSearchRepository;
    private final PostIndexer postIndexer;

    @ApiOperation(value = "카테고리별 게시글 조회 API", notes = "카테고리, 정렬 방식, 페이지 번호, 페이지 크기를 기준으로 게시글을 조회합니다.")
    @ApiResponse(code = 200, message = "카테고리별 게시글 불러오기 성공")
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

            Member member = memberService.getCurrentMember();

            List<PostResponseDto.SimplePostDto> res = postService.getPostsByCategory(category, sort, page, size);

            return new ResponseEntity( DefaultRes.res(StatusCode.OK, ResponseMessage.READ_POST_LIST_SUCCESS, res), HttpStatus.OK);
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

            Member member = memberService.getCurrentMember();
            postService.deletePost(postId, member);

            return new ResponseEntity( DefaultRes.res(StatusCode.OK, ResponseMessage.DELETE_POST_SUCCESS), HttpStatus.OK);
        } catch (CustomExceptions.testException e) {
            return handleApiException(e, HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value = "게시글 상세 조회 API")
    @ApiResponse(code = 200, message = "게시글 상세 조회 성공")
    @GetMapping("/{postId}")
    public ResponseEntity getPost(@PathVariable Long postId){
        try {
            logger.info("Received request: method={}, path={}, description={}", "GET", "/api/post/{post-id}", "게시글 상세 조회 API");

            Member member = memberService.getCurrentMember();

            PostResponseDto.PostDetailDto res = postService.getPostDetail(postId, member);

            return new ResponseEntity( DefaultRes.res(StatusCode.OK, ResponseMessage.READ_POST_SUCCESS, res), HttpStatus.OK);
        } catch (CustomExceptions.testException e) {
            return handleApiException(e, HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value = "게시글 좋아요/취소 API")
    @ApiResponse(code = 200, message = "게시글 좋아요/취소 성공")
    @PostMapping("/{postId}/like")
    public ResponseEntity togglePostLike(@PathVariable Long postId){
        try {
            logger.info("Received request: method={}, path={}, description={}", "POST", "/api/post/{post-id}/like", "게시글 좋아요/취소 API");

            Member member = memberService.getCurrentMember();
            LikeResult result = postService.togglePostLike(postId, member);

            String message = (result == LikeResult.LIKED)
                    ? ResponseMessage.POST_LIKE_SUCCESS
                    : ResponseMessage.POST_UNLIKE_SUCCESS;

            return new ResponseEntity( DefaultRes.res(StatusCode.OK, message), HttpStatus.OK);
        } catch (CustomExceptions.testException e) {
            return handleApiException(e, HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value = "게시글 수정 API")
    @ApiResponse(code = 200, message = "게시글 수정 성공")
    @PutMapping("/{postId}")
    public ResponseEntity updatePost(@PathVariable Long postId, @RequestBody PostRequestDto.UpdatePostDto request){
        try {
            logger.info("Received request: method={}, path={}, description={}", "PUT", "/api/post/{post-id}", "게시글 수정 API");

            Member member = memberService.getCurrentMember();
            postService.updatePost(postId, member, request);

            return new ResponseEntity( DefaultRes.res(StatusCode.OK, ResponseMessage.UPDATE_POST_SUCCESS), HttpStatus.OK);
        } catch (CustomExceptions.testException e) {
            return handleApiException(e, HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value = "게시글 작성 API")
    @ApiResponse(code = 200, message = "게시글 작성 성공")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity createPost(@ModelAttribute PostRequestDto.CreatePostRequest request,
                                     @RequestPart(value = "images", required = false) List<MultipartFile> images){
        try {
            logger.info("Received request: method={}, path={}, description={}", "POST", "/api/post", "게시글 작성 API");

            Member member = memberService.getCurrentMember();

            postService.createPost(request, images, member);

            return new ResponseEntity( DefaultRes.res(StatusCode.OK, ResponseMessage.CREATE_POST_SUCCESS), HttpStatus.OK);
        } catch (CustomExceptions.testException e) {
            return handleApiException(e, HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value = "게시글 검색 API")
    @ApiResponse(code = 200, message = "게시글 검색 성공")
    @GetMapping("/search")
    public ResponseEntity searchPosts(@ApiParam(value = "검색 키워드", example = "제목", required = true)
                                          @RequestParam String keyword,

                                      @ApiParam(value = "페이지 번호", example = "1", required = true)
                                          @RequestParam Integer page,

                                      @ApiParam(value = "한 페이지당 게시글 수", example = "10", required = true)
                                          @RequestParam Integer size){
        try {
            logger.info("Received request: method={}, path={}, description={}", "GET", "/api/post/search?keyword={keyword}", "게시글 검색 API");

            //elasticsearch 마이그레이션
            // postIndexer.migrateToElasticsearch();

            Member member = memberService.getCurrentMember();

            List<PostResponseDto.SimplePostDto> res = postService.searchByTitle(keyword, page, size);

            return new ResponseEntity( DefaultRes.res(StatusCode.OK, ResponseMessage.SEARCH_POST_SUCCESS, res), HttpStatus.OK);
        } catch (CustomExceptions.testException e) {
            return handleApiException(e, HttpStatus.BAD_REQUEST);
        }
    }
}
