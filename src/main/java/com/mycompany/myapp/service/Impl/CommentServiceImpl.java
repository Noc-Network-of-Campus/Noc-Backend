package com.mycompany.myapp.service.Impl;

import com.mycompany.myapp.converter.CommentConverter;
import com.mycompany.myapp.domain.Comment;
import com.mycompany.myapp.domain.CommentLike;
import com.mycompany.myapp.domain.Member;
import com.mycompany.myapp.domain.Post;
import com.mycompany.myapp.domain.enums.CommentLikeResult;
import com.mycompany.myapp.repository.CommentLikeRepository;
import com.mycompany.myapp.repository.CommentRepository;
import com.mycompany.myapp.repository.PostRepository;
import com.mycompany.myapp.service.CommentService;
import com.mycompany.myapp.web.dto.CommentRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final CommentConverter commentConverter;
    private final CommentLikeRepository commentLikeRepository;

    @Override
    @Transactional
    public void createComment(CommentRequestDto.CreateCommentDto request, Member member) {
        // 게시글 존재 여부 확인
        Post post = postRepository.findById(request.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        Comment parent = null;

        // 대댓글인 경우 부모 댓글 유효성 검사
        if (request.getParentCommentId() != null) {
            parent = commentRepository.findById(request.getParentCommentId())
                    .orElseThrow(() -> new IllegalArgumentException("부모 댓글이 존재하지 않습니다."));

            // 1-depth까지만 허용
            if (parent.getParentComment() != null){
                throw new IllegalArgumentException("댓글은 1-depth까지만 작성할 수 있습니다.");
            }
        }

        // 댓글 엔티티 생성 및 저장
        Comment comment = commentConverter.toCreateComment(member, post, parent, request.getContent());
        commentRepository.save(comment);

        // Post 댓글 수 증가
        post.increaseCommentCount();
    }

    @Override
    @Transactional
    public CommentLikeResult toggleCommentLike(Long commentId, Member member){
        // 댓글 존재 여부 확인
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));

        // 댓글 좋아요 여부 확인
        Optional<CommentLike> existingLike = commentLikeRepository.findByCommentAndMember(comment, member);

        // 좋아요 눌렀으면 취소
        if (existingLike.isPresent()) {
            commentLikeRepository.delete(existingLike.get());
            comment.decreaseLikeCount();
            return CommentLikeResult.UNLIKED;
        } else {
            // 아닌 경우 좋아요
            CommentLike like = commentConverter.toCommentLike(comment, member);
            commentLikeRepository.save(like);
            comment.increaseLikeCount();
            return CommentLikeResult.LIKED;
        }
    }
}

