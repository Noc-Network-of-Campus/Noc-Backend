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

import javax.validation.Valid;

/**
 * 신고(Report) 관련 요청을 처리하는 REST 컨트롤러
 * - 게시글 또는 댓글 신고 API 제공
 */
@Api(tags = "신고 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/report")
public class ReportController extends BaseController {

    private final MemberService memberService;
    private final ReportService reportService;

    /**
     * 게시글 또는 댓글에 대한 신고 등록
     *
     * @param request 신고 요청 DTO (신고 대상 ID, 유형, 사유 포함)
     * @return 신고 성공 응답 (statusCode: 200)
     */
    @ApiOperation(value = "신고하기 API")
    @ApiResponse(code = 200, message = "신고하기 성공")
    @PostMapping
    public ResponseEntity createReport(@Valid @RequestBody ReportRequestDto request){
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
