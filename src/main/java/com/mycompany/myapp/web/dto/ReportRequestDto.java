package com.mycompany.myapp.web.dto;

import com.mycompany.myapp.domain.enums.ReportTargetType;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Getter;

@Getter
public class ReportRequestDto {
    @ApiParam(name = "Target Type ID", value = "신고 대상 타입 입력(POST or COMMENT)", required = true)
    @ApiModelProperty(example = "COMMENT")
    private ReportTargetType targetType;

    @ApiParam(name = "Target ID", value = "신고 대상 아이디 입력", required = true)
    @ApiModelProperty(example = "1")
    private Long targetId;

    @ApiParam(name = "Reason", value = "신고 사유 입력", required = true)
    @ApiModelProperty(example = "신고 사유입니다.")
    private String reason;
}
