package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Member;
import com.mycompany.myapp.domain.enums.LikeResult;
import com.mycompany.myapp.web.dto.CommentRequestDto;

public interface CommentService {
    void createComment(CommentRequestDto.CreateCommentDto request, Member member);
    LikeResult toggleCommentLike(Long commentId, Member member);
    void deleteComment(Long commentId, Member member);
}
