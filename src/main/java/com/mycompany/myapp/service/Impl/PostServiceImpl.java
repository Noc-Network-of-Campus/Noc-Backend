package com.mycompany.myapp.service.Impl;

import com.mycompany.myapp.converter.PostConverter;
import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.domain.enums.Category;
import com.mycompany.myapp.domain.enums.LikeResult;
import com.mycompany.myapp.domain.enums.SortType;
import com.mycompany.myapp.exception.CustomExceptions;
import com.mycompany.myapp.exception.ResponseMessage;
import com.mycompany.myapp.exception.StatusCode;
import com.mycompany.myapp.repository.ImageRepository;
import com.mycompany.myapp.repository.PostLikeRepository;
import com.mycompany.myapp.repository.PostRepository;
import com.mycompany.myapp.repository.PostSearchRepository;
import com.mycompany.myapp.service.PostService;
import com.mycompany.myapp.util.S3Uploader;
import com.mycompany.myapp.web.dto.PostRequestDto;
import com.mycompany.myapp.web.dto.PostResponseDto;
import com.mycompany.myapp.web.dto.base.DefaultRes;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final PostConverter postConverter;
    private final PostLikeRepository postLikeRepository;
    private final S3Uploader s3Uploader;
    private final ImageRepository imageRepository;
    private final PostSearchRepository postSearchRepository;

    @Override
    public List<PostResponseDto.SimplePostDto> getPostsByCategoryWithCursor(Category category, SortType sort, Long lastPostId, Integer size){
        if (size < 1) throw new IllegalArgumentException("size는 1 이상이어야 합니다.");

        List<Post> posts;

        Pageable pageable = PageRequest.of(0, size); // 커서 기반 페이징은 항상 0 페이지

        if (sort == SortType.LIKE) {
            Integer lastLikeCount = null;

            if (lastPostId != null) {
                Post lastPost = postRepository.findById(lastPostId).orElse(null);
                if (lastPost == null) return Collections.emptyList();
                lastLikeCount = lastPost.getLikeCount();
            }

            posts = (category == null)
                    ? postRepository.findByCursorLike(lastLikeCount, lastPostId, pageable)
                    : postRepository.findByCategoryAndCursorLike(category, lastLikeCount, lastPostId, pageable);
        } else {
            posts = (category == null)
                    ? postRepository.findByCursorLatest(lastPostId, pageable)
                    : postRepository.findByCategoryAndCursorLatest(category, lastPostId, pageable);
        }

        return posts.stream()
                .map(postConverter::toSimplePostDto)
                .collect(Collectors.toList());
    }

    // 카테고리 + 정렬 기준에 따라 게시글 목록을 페이징 조회
    @Override
    public List<PostResponseDto.SimplePostDto> getPostsByCategoryWithOffest(Category category, SortType sort, Integer page, Integer size){
        if (page < 1) throw new IllegalArgumentException("페이지는 1부터 시작합니다.");
        if (size < 1) throw new IllegalArgumentException("size는 1 이상이어야 합니다.");

        Pageable pageable;

        // 정렬 기준에 따라 Pageable 생성
        if (sort == SortType.LIKE) {
            pageable = PageRequest.of(page - 1, size,
                    Sort.by(Sort.Direction.DESC, "likeCount")
                            .and(Sort.by(Sort.Direction.DESC, "createdAt")));
        } else {
            pageable = PageRequest.of(page - 1, size,
                    Sort.by(Sort.Direction.DESC, "createdAt"));
        }

        // category가 null이면 전체 조회, 아니면 해당 카테고리만 조회
        return (category == null
                ? postRepository.findAll(pageable)
                : postRepository.findByCategory(category, pageable)
        ).stream()
                .map(postConverter::toSimplePostDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deletePost(Long postId, Member member){
        // 게시글 존재 여부 확인
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        if (!post.getMember().equals(member)) {
            throw new CustomExceptions.UnauthorizedAccessException("삭제 권한이 없습니다.");
        }
        postRepository.delete(post);

        // ES 삭제
        postSearchRepository.deleteById(postId);
    }

    @Override
    public PostResponseDto.PostDetailDto getPostDetail(Long postId, Member member){
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
        return postConverter.toPostDetailDto(post, member);
    }

    @Override
    @Transactional
    public LikeResult togglePostLike(Long postId, Member member){
        // 게시글 존재 여부 확인
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        // 게시글 좋아요 여부 확인
        Optional<PostLike> existingLike = postLikeRepository.findByPostAndMember(post, member);

        // 좋아요 눌렀으면 취소
        if (existingLike.isPresent()) {
            postLikeRepository.delete(existingLike.get());
            post.decreaseLikeCount();
            return LikeResult.UNLIKED;
        } else {
            // 아닌 경우 좋아요
            PostLike like = postConverter.toPostLike(post, member);
            postLikeRepository.save(like);
            post.increaseLikeCount();
            return LikeResult.LIKED;
        }
    }

    @Override
    @Transactional
    public void updatePost(Long postId, Member member, PostRequestDto.UpdatePostDto request){
        // 게시글 존재 여부 확인
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        // 게시글 수정 권한 확인
        if (!post.getMember().getId().equals(member.getId())) {
            throw new SecurityException("게시글 수정 권한이 없습니다.");
        }

        // null 값이면 기존 값 유지
        String title = request.getTitle() != null ? request.getTitle() : post.getTitle();
        String content = request.getContent() != null ? request.getContent() : post.getContent();

        // null이면 Category.FREE, 아니면 Enum 변환
        Category category = request.getCategory() != null
                ? Category.valueOf(request.getCategory())
                : Category.FREE;

        post.updatePost(title, content, category);

        // ES 업데이트
        PostDocument doc = postConverter.toPostDoc(title, post.getId());
        try {
            postSearchRepository.save(doc);
        } catch (Exception e) {
        }
    }

    @Override
    @Transactional
    public void createPost(PostRequestDto.CreatePostRequest request, List<MultipartFile> images, Member member) {
        // 카테고리 null 이면 FREE
        Category category = request.getCategory() != null
                ? Category.valueOf(request.getCategory())
                : Category.FREE;

        if (request.getLatitude() == null || request.getLongitude() == null) {
            throw new IllegalArgumentException("위도/경도 값이 누락되었습니다.");
        }

        // Point 생성
        Point location = new GeometryFactory(new PrecisionModel(), 4326)
                .createPoint(new Coordinate(request.getLongitude(), request.getLatitude()));
        location.setSRID(4326);

        Post post = postConverter.toPost(request, category, location, member);
        postRepository.save(post);

        PostDocument document = postConverter.toPostDocument(post);
        try {
            postSearchRepository.save(document);
        } catch (Exception e) {
        }

        // 이미지 저장 (순서대로)
        if (images != null && !images.isEmpty()) {
            List<Image> imageEntities = new ArrayList<>();
            for (int i = 0; i < images.size(); i++) {
                String imageUrl = s3Uploader.upload(images.get(i), "post"); // S3 업로드
                Image image = Image.builder()
                        .post(post)
                        .imageUrl(imageUrl)
                        .sortOrder(i)
                        .build();
                imageEntities.add(image);
            }
            imageRepository.saveAll(imageEntities);
        }
    }

    @Override
    public List<PostResponseDto.SimplePostDto> searchByTitleWithCursor(String keyword, Long lastPostId, Integer size) {
        if (size < 1) throw new IllegalArgumentException("size는 1 이상이어야 합니다.");

        // 최신순 정렬을 위해 id DESC 조건
        Pageable pageable = PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, "id"));

        Page<PostDocument> pageDocs;

        if (lastPostId == null) {
            pageDocs = postSearchRepository.findByTitleContaining(keyword, pageable);
        } else {
            pageDocs = postSearchRepository.findByTitleContainingAndIdLessThan(keyword, lastPostId, pageable);
        }

        List<Long> postIds = pageDocs.getContent().stream()
                .map(PostDocument::getId)
                .collect(Collectors.toList());

        List<Post> posts = postRepository.findAllById(postIds);
        Map<Long, Post> postMap = posts.stream().collect(Collectors.toMap(Post::getId, p -> p));

        return postIds.stream()
                .map(postMap::get)
                .map(postConverter::toSimplePostDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PostResponseDto.SimplePostDto> getMyPosts(Long lastPostId, Integer size, Member member){
        if (size < 1) throw new IllegalArgumentException("size는 1 이상이어야 합니다.");

        Pageable pageable = PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, "id"));

        List<Post> posts = (lastPostId == null)
                ? postRepository.findByMemberOrderByIdDesc(member, pageable)
                : postRepository.findByMemberAndIdLessThanOrderByIdDesc(member, lastPostId, pageable);

        return posts.stream()
                .map(postConverter::toSimplePostDto)
                .collect(Collectors.toList());
    }
}
