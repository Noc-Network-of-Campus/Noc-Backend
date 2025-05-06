package com.mycompany.myapp.service.Impl;

import com.mycompany.myapp.converter.ReportConverter;
import com.mycompany.myapp.domain.Comment;
import com.mycompany.myapp.domain.Member;
import com.mycompany.myapp.domain.Post;
import com.mycompany.myapp.domain.Report;
import com.mycompany.myapp.domain.enums.ReportTargetType;
import com.mycompany.myapp.repository.CommentRepository;
import com.mycompany.myapp.repository.PostRepository;
import com.mycompany.myapp.repository.ReportRepository;
import com.mycompany.myapp.service.ReportService;
import com.mycompany.myapp.web.dto.ReportRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final ReportConverter reportConverter;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public void createReport(ReportRequestDto request, Member member){
        if (request.getTargetType() == ReportTargetType.POST) {
            if (!postRepository.existsById(request.getTargetId())) {
                throw new IllegalArgumentException("게시글이 존재하지 않습니다.");
            }
        } else if (request.getTargetType() == ReportTargetType.COMMENT) {
            if (!commentRepository.existsById(request.getTargetId())) {
                throw new IllegalArgumentException("댓글이 존재하지 않습니다.");
            }
        } else {
            throw new IllegalArgumentException("유효하지 않은 신고 대상입니다.");
        }

        Report report = reportConverter.toCreateReport(request, member);
        reportRepository.save(report);
    }
}
