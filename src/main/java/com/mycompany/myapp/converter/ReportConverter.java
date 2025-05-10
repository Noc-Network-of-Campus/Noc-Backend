package com.mycompany.myapp.converter;

import com.mycompany.myapp.domain.Member;
import com.mycompany.myapp.domain.Report;
import com.mycompany.myapp.web.dto.ReportRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ReportConverter {

    /**
     * ReportRequestDto와 현재 로그인한 사용자 정보를 기반으로 신고(Report) 엔티티를 생성
     *
     * @param request 신고 요청 DTO
     * @param member 신고자 (현재 로그인한 사용자)
     * @return 저장 가능한 Report 엔티티
     */
    public Report toCreateReport(ReportRequestDto request, Member member){
        return Report.builder()
                .reporter(member)
                .targetType(request.getTargetType())
                .targetId(request.getTargetId())
                .reason(request.getReason())
                .build();
    }
}
