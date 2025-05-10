package com.mycompany.myapp.web.dto;

import com.mycompany.myapp.domain.enums.ReportTargetType;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 신고 요청에 사용되는 DTO
 * - 게시글 또는 댓글을 신고할 때 사용
 */
@Getter
public class ReportRequestDto {

    /**
     * 신고 대상의 타입
     * - 게시글(POST) 또는 댓글(COMMENT) 중 하나
     */
    @ApiParam(name = "Target Type ID", value = "신고 대상 타입 입력(POST or COMMENT)", required = true)
    @ApiModelProperty(example = "COMMENT")
    @NotNull(message = "신고 대상 타입은 필수입니다.")
    private ReportTargetType targetType;

    /**
     * 신고 대상의 ID
     * - targetType이 POST인 경우 postId
     * - targetType이 COMMENT인 경우 commentId를 의미
     */
    @ApiParam(name = "Target ID", value = "신고 대상 아이디 입력", required = true)
    @ApiModelProperty(example = "1")
    @NotNull(message = "신고 대상 ID는 필수입니다.")
    private Long targetId;

    @ApiParam(name = "Reason", value = "신고 사유 입력", required = true)
    @ApiModelProperty(example = "신고 사유입니다.")
    @NotBlank(message = "신고 사유는 필수입니다.")
    private String reason;
}
