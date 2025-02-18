package com.prac.source.common;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum responseCode {
	
    // 성공
    SUCCESS_READ(200, "READ_SUCCESS", "조회 성공"),
    SUCCESS_INSERT(201, "INSERT_SUCCESS", "생성 성공"),
    SUCCESS_UPDATE(200, "UPDATE_SUCCESS", "수정 성공"),
    SUCCESS_DELETE(200, "DELETE_SUCCESS", "삭제 성공"),
    SUCCESS_ORDER(200, "ORDER_SUCCESS", "주문 성공"),
    SUCCESS_PAYMENT(200, "PAYMENT_SUCCESS", "결제 성공"),

    // 실패
    FAIL_READ(404, "READ_FAIL", "조회 실패"),
    FAIL_INSERT(400, "INSERT_FAIL", "생성 실패"),
    FAIL_UPDATE(400, "UPDATE_FAIL", "수정 실패"),
    FAIL_DELETE(400, "DELETE_FAIL", "삭제 실패"),

    // 클라이언트 요청 오류
    BAD_REQUEST(400, "BAD_REQUEST", "잘못된 요청입니다"),
    UNAUTHORIZED(401, "UNAUTHORIZED", "인증이 필요합니다"),
    FORBIDDEN(403, "FORBIDDEN", "접근 권한이 없습니다"),
    NOT_FOUND(404, "NOT_FOUND", "리소스를 찾을 수 없습니다"),

    // 서버 처리 오류
    INTERNAL_SERVER_ERROR(500, "INTERNAL_SERVER_ERROR", "서버 내부 오류"),
    SERVICE_UNAVAILABLE(503, "SERVICE_UNAVAILABLE", "서비스를 사용할 수 없습니다");;

    private int status;
    private String code;
    private String message;

}
