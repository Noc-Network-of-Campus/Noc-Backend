package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Member;
import com.mycompany.myapp.domain.enums.CommentLikeResult;
import com.mycompany.myapp.web.dto.CommentRequestDto;

public interface CommentService {
    void createComment(CommentRequestDto.CreateCommentDto request, Member member);
    CommentLikeResult toggleCommentLike(Long commentId, Member member);
}
