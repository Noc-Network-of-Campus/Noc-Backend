package com.mycompany.myapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.domain.Member;
import com.mycompany.myapp.repository.MemberRepository;
import com.mycompany.myapp.service.CommentService;
import com.mycompany.myapp.web.controller.CommentController;
import com.mycompany.myapp.web.dto.CommentRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(CommentController.class)
@AutoConfigureMockMvc
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    @MockBean
    private MemberRepository memberRepository;

    @Test
    void 댓글_작성_성공() throws Exception {
        // given
        CommentRequestDto.CreateCommentDto request = new CommentRequestDto.CreateCommentDto();
        request.setPostId(1L);
        request.setContent("댓글 내용입니다");
        request.setParentCommentId(null);

        Member mockMember = Member.builder()
                .id(1L)
                .nickname("오리난쟁이")
                .email("duck@example.com")
                .build();

        given(memberRepository.getByNickname("오리난쟁이")).willReturn(mockMember);

        // when & then
        mockMvc.perform(post("/api/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseMessage").value("댓글 작성 성공"));
    }
}


