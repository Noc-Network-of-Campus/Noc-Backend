package com.mycompany.myapp.web.dto.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * API 응답의 기본 응답 포맷 클래스
 *
 * @param <T> data에 포함될 실제 응답 데이터 타입
 *
 * 구조 예시:
 * {
 *   "statusCode": 200,
 *   "responseMessage": "성공 메시지",
 *   "data": { ... } // 생략 가능
 * }
 */
@Data
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DefaultRes<T> {
    /** HTTP 상태 코드 */
    private Integer statusCode;

    /** 응답 메시지 (성공 또는 에러 메시지 등) */
    private String responseMessage;

    /** 실제 응답 데이터 (nullable) */
    private T data;

    /**
     * 데이터가 없는 경우 사용할 생성자
     *
     * @param statusCode 응답 코드
     * @param responseMessage 메시지
     */
    public DefaultRes(final Integer statusCode, final String responseMessage) {
        this.statusCode = statusCode;
        this.responseMessage = responseMessage;
        this.data = null;
    }

    /**
     * 응답 데이터가 없는 경우에 사용되는 정적 팩토리 메서드
     * @param statusCode 응답 코드
     * @param responseMessage 메시지
     * @return DefaultRes 객체
     */
    public static<T> DefaultRes<T> res(final Integer statusCode, final String responseMessage) {
        return res(statusCode, responseMessage, null);
    }

    /**
     * 응답 데이터가 포함된 경우에 사용되는 정적 팩토리 메서드
     *
     * @param statusCode 응답 코드
     * @param responseMessage 메시지
     * @param t 실제 응답 데이터
     * @return DefaultRes 객체
     */
    public static<T> DefaultRes<T> res(final Integer statusCode, final String responseMessage, final T t) {
        return DefaultRes.<T>builder()
                .data(t)
                .statusCode(statusCode)
                .responseMessage(responseMessage)
                .build();
    }
}