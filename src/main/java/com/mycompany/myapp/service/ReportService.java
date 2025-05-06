package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Member;
import com.mycompany.myapp.web.dto.ReportRequestDto;

public interface ReportService {
    void createReport(ReportRequestDto request, Member member);
}
