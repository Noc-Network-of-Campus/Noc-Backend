package com.mycompany.myapp.converter;

import com.mycompany.myapp.domain.Member;
import com.mycompany.myapp.domain.Report;
import com.mycompany.myapp.web.dto.ReportRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ReportConverter {

    public Report toCreateReport(ReportRequestDto request, Member member){
        return Report.builder()
                .reporter(member)
                .targetType(request.getTargetType())
                .targetId(request.getTargetId())
                .reason(request.getReason())
                .build();
    }
}
