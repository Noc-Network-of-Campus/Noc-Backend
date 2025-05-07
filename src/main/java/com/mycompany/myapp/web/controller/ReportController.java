package com.mycompany.myapp.web.controller;

import com.mycompany.myapp.domain.Member;
import com.mycompany.myapp.exception.CustomExceptions;
import com.mycompany.myapp.exception.ResponseMessage;
import com.mycompany.myapp.exception.StatusCode;
import com.mycompany.myapp.service.MemberService;
import com.mycompany.myapp.service.ReportService;
import com.mycompany.myapp.web.controller.base.BaseController;
import com.mycompany.myapp.web.dto.ReportRequestDto;
import com.mycompany.myapp.web.dto.base.DefaultRes;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "신고 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/report")
public class ReportController extends BaseController {

    private final MemberService memberService;
    private final ReportService reportService;

    @ApiOperation(value = "신고하기 API")
    @ApiResponse(code = 200, message = "신고하기 성공")
    @PostMapping
    public ResponseEntity createReport(@RequestBody ReportRequestDto request){
        try {
            logger.info("Received request: method={}, path={}, description={}", "POST", "/api/report", "신고하기 API");

            Member member = memberService.getCurrentMember();
            reportService.createReport(request, member);

            return new ResponseEntity( DefaultRes.res(StatusCode.OK, ResponseMessage.CREATE_REPORT_SUCCESS), HttpStatus.OK);
        } catch (CustomExceptions.testException e) {
            return handleApiException(e, HttpStatus.BAD_REQUEST);
        }
    }
}
