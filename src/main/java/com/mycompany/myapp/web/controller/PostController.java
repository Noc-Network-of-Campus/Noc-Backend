package com.mycompany.myapp.web.controller;

import com.mycompany.myapp.domain.Member;
import com.mycompany.myapp.domain.enums.Category;
import com.mycompany.myapp.domain.enums.LikeResult;
import com.mycompany.myapp.domain.enums.SortType;
import com.mycompany.myapp.exception.CustomExceptions;
import com.mycompany.myapp.exception.ResponseMessage;
import com.mycompany.myapp.exception.StatusCode;
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

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 게시글(Post) 관련 REST API를 제공하는 컨트롤러
 * - 게시글 등록, 수정, 삭제, 상세 조회
 * - 카테고리별 조회 (커서 기반 / 오프셋 기반)
 * - 좋아요 토글
 * - 게시글 검색 (Elasticsearch 기반)
 * - 내 게시글 목록 조회
 */
@Api(tags = "게시글 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post")
public class PostController extends BaseController {

    private final MemberService memberService;
    private final PostService postService;
    private final PostSearchRepository postSearchRepository;
    private final PostIndexer postIndexer;

    /**
     * 카테고리 및 정렬 기준에 따라 게시글을 커서 기반 조회
     *
     * @param category 카테고리 (null 시 전체)
     * @param sort 정렬 방식 (LIKE or LATEST)
     * @param lastPostId 마지막 게시글 ID (커서)
     * @param size 한 페이지당 조회할 게시글 수
     * @return 게시글 목록 + nextCursor, hasNext 포함
     */
    @ApiOperation(value = "카테고리별 게시글 조회 API(cursor 기반)", notes = "커서 기반 페이징으로 구현")
    @ApiResponse(code = 200, message = "카테고리별 게시글 불러오기 성공")
    @GetMapping("/list/cursor")
    public ResponseEntity getPostsByCategoryWithCursor(
            @ApiParam(value = "카테고리. 없으면 전체 게시글", example = "FOOD") @RequestParam(required = false) Category category,
            @ApiParam(value = "정렬 방식 (LIKE, LATEST). 기본값은 LATEST", example = "LATEST") @RequestParam(required = false, defaultValue = "LATEST") SortType sort,
            @ApiParam(value = "마지막 게시글 ID", example = "100") @RequestParam(required = false) Long lastPostId,
            @ApiParam(value = "한 페이지당 게시글 수", example = "10", required = true) @RequestParam Integer size){
        try {
            logger.info("Received request: method={}, path={}, description={}", "GET", "/api/post/list/cursor", "카테고리별 게시글 조회 API");

            Member member = memberService.getCurrentMember();

            List<PostResponseDto.SimplePostDto> postList = postService.getPostsByCategoryWithCursor(category, sort, lastPostId, size);
            Long nextCursor = (postList.isEmpty()) ? null : postList.get(postList.size() - 1).getPostId();

            Map<String, Object> res = new HashMap<>();
            res.put("posts", postList);
            res.put("nextCursor", nextCursor);
            res.put("hasNext", postList.size() == size);

            return new ResponseEntity( DefaultRes.res(StatusCode.OK, ResponseMessage.READ_POST_LIST_SUCCESS, res), HttpStatus.OK);
        } catch (CustomExceptions.testException e) {
            return handleApiException(e, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 카테고리 및 정렬 기준에 따라 게시글을 페이지 기반(offset) 조회
     *
     * @param category 카테고리
     * @param sort 정렬 기준
     * @param page 페이지 번호 (1부터 시작)
     * @param size 페이지 크기
     * @return 게시글 리스트
     */
    @ApiOperation(value = "카테고리별 게시글 조회 API(offset 기반)", notes = "Offset 기반 페이징으로 구현")
    @ApiResponse(code = 200, message = "카테고리별 게시글 불러오기 성공")
    @GetMapping("/list/page")
    public ResponseEntity getPostsByCategoryWithOffset(@ApiParam(value = "카테고리. 없으면 전체 게시글", example = "FOOD")
                                                    @RequestParam(required = false) Category category,
                                                @ApiParam(value = "정렬 방식 (LIKE, LATEST). 기본값은 LATEST", example = "LATEST")
                                                    @RequestParam(required = false, defaultValue = "LATEST") SortType sort,
                                                @ApiParam(value = "페이지 번호", example = "1", required = true)
                                                    @RequestParam Integer page,
                                                @ApiParam(value = "한 페이지당 게시글 수", example = "10", required = true)
                                                    @RequestParam Integer size){
        try {
            logger.info("Received request: method={}, path={}, description={}", "GET", "/api/post/list/page", "카테고리별 게시글 조회 API");

            Member member = memberService.getCurrentMember();

            List<PostResponseDto.SimplePostDto> res = postService.getPostsByCategoryWithOffest(category, sort, page, size);

            return new ResponseEntity( DefaultRes.res(StatusCode.OK, ResponseMessage.READ_POST_LIST_SUCCESS, res), HttpStatus.OK);
        } catch (CustomExceptions.testException e) {
            return handleApiException(e, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 게시글 삭제 (작성자 본인만 가능)
     *
     * @param postId 삭제할 게시글 ID
     * @return 삭제 성공 메시지 응답
     */
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

    /**
     * 게시글 상세 정보 조회
     *
     * @param postId 게시글 ID
     * @return 게시글 상세 DTO 응답
     */
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

    /**
     * 게시글에 대한 좋아요 토글
     * - 좋아요가 되어 있으면 취소
     * - 안 되어 있으면 좋아요 처리
     *
     * @param postId 게시글 ID
     * @return 처리 결과 메시지 (POST_LIKE_SUCCESS or POST_UNLIKE_SUCCESS)
     */
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

    /**
     * 게시글 수정
     *
     * @param postId 게시글 ID
     * @param request 수정 요청 DTO
     * @return 수정 성공 메시지
     */
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

    /**
     * 게시글 생성 (이미지 업로드 포함)
     *
     * @param request 게시글 생성 요청 DTO (제목, 내용, 카테고리 등)
     * @param images 첨부 이미지 리스트 (nullable)
     * @return 생성 성공 메시지
     */
    @ApiOperation(value = "게시글 작성 API")
    @ApiResponse(code = 200, message = "게시글 작성 성공")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity createPost(@Valid @ModelAttribute PostRequestDto.CreatePostRequest request,
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

    /**
     * 게시글을 키워드 기반 검색 (Elasticsearch)
     *
     * @param keyword 검색 키워드 (제목 또는 작성자명)
     * @param lastPostId 커서 (null이면 첫 페이지)
     * @param size 페이지 크기
     * @return 검색된 게시글 리스트 + nextCursor + hasNext 포함
     */
    @ApiOperation(value = "게시글 검색 API")
    @ApiResponse(code = 200, message = "게시글 검색 성공")
    @GetMapping("/search")
    public ResponseEntity searchPosts(@ApiParam(value = "검색 키워드", example = "제목", required = true)
                                          @RequestParam String keyword,
                                      @ApiParam(value = "마지막 게시글 ID", example = "100")
                                          @RequestParam(required = false) Long lastPostId,
                                      @ApiParam(value = "한 페이지당 게시글 수", example = "10", required = true)
                                          @RequestParam Integer size){
        try {
            logger.info("Received request: method={}, path={}, description={}", "GET", "/api/post/search?keyword={keyword}", "게시글 검색 API");

            //elasticsearch 마이그레이션
            // postIndexer.migrateToElasticsearch();

            Member member = memberService.getCurrentMember();

            List<PostResponseDto.SimplePostDto> result = postService.searchByTitleWithCursor(keyword, lastPostId, size);
            Long nextCursor = result.isEmpty() ? null : result.get(result.size() - 1).getPostId();

            Map<String, Object> res = new HashMap<>();
            res.put("posts", result);
            res.put("nextCursor", nextCursor);
            res.put("hasNext", result.size() == size);

            return new ResponseEntity( DefaultRes.res(StatusCode.OK, ResponseMessage.SEARCH_POST_SUCCESS, res), HttpStatus.OK);
        } catch (CustomExceptions.testException e) {
            return handleApiException(e, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 현재 로그인한 사용자가 작성한 게시글 목록을 커서 기반으로 조회
     *
     * @param lastPostId 마지막 게시글 ID (커서)
     * @param size 페이지 크기
     * @return 본인 게시글 목록 + nextCursor + hasNext 포함
     */
    @ApiOperation(value = "내 게시글 조회 API", notes = "커서 기반 페이징으로 구현")
    @ApiResponse(code = 200, message = "내 게시글 불러오기 성공")
    @GetMapping("/my")
    public ResponseEntity getMyPostsByCursor(
            @ApiParam(value = "마지막 게시글 ID", example = "100") @RequestParam(required = false) Long lastPostId,
            @ApiParam(value = "페이지 크기", example = "10", required = true) @RequestParam Integer size){
        try {
            logger.info("Received request: method={}, path={}, description={}", "GET", "/api/post/my", "카테고리별 게시글 조회 API");

            Member member = memberService.getCurrentMember();

            List<PostResponseDto.SimplePostDto> postList = postService.getMyPosts(lastPostId, size, member);
            Long nextCursor = (postList.isEmpty()) ? null : postList.get(postList.size() - 1).getPostId();

            Map<String, Object> res = new HashMap<>();
            res.put("posts", postList);
            res.put("nextCursor", nextCursor);
            res.put("hasNext", postList.size() == size);

            return new ResponseEntity( DefaultRes.res(StatusCode.OK, ResponseMessage.READ_MY_POSTS_SUCCESS, res), HttpStatus.OK);
        } catch (CustomExceptions.testException e) {
            return handleApiException(e, HttpStatus.BAD_REQUEST);
        }
    }
}
