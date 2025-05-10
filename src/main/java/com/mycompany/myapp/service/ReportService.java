package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Member;
import com.mycompany.myapp.web.dto.ReportRequestDto;

/**
 * 신고(Report) 관련 비즈니스 로직을 처리하는 서비스 인터페이스입니다.
 */
public interface ReportService {

    /**
     * 게시글 또는 댓글에 대한 신고 생성
     *
     * @param request 신고 요청 정보 (대상 ID, 타입, 사유 포함)
     * @param member 현재 로그인한 사용자 (신고자)
     */
    void createReport(ReportRequestDto request, Member member);
}
