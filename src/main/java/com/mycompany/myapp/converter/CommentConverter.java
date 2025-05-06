package com.mycompany.myapp.converter;

import com.mycompany.myapp.domain.Comment;
import com.mycompany.myapp.domain.Member;
import com.mycompany.myapp.domain.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CommentConverter {
    public Comment toCreateComment(Member member, Post post, Comment parent, String content){
        return Comment.builder()
                .post(post)
                .member(member)
                .content(content)
                .parentComment(parent)
                .likeCount(0)
                .build();
    }
}
