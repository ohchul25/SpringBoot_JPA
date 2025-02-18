package com.prac.config;

import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.prac.source.common.ResponseApi;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseApi<?> handleAllExceptions(Exception e) {
        ResponseApi<Object> errorResponse = new ResponseApi<>(
                false,
                null,
                e.getMessage() != null ? e.getMessage() : "서버 내부 오류가 발생했습니다."
        );
        return errorResponse;
    }
    
    @ExceptionHandler(value = {NoHandlerFoundException.class, HttpRequestMethodNotSupportedException.class})
    public ResponseApi<?> handleNoPageFoundException(Exception e) {
        ResponseApi<Object> errorResponse = new ResponseApi<>(
                false,
                null,
                e.getMessage() != null ? e.getMessage() : "서버 내부 오류가 발생했습니다."
        );
        return errorResponse;
    }

//		추후 커스텀 핸들러 익셉션 필요하면 커스텀익셉션 클래스 생성 후 추가
//    @ExceptionHandler(CustomException.class)
//    public ResponseEntity<ResponseApi<?>> handleCustomException(CustomNotFoundException e) {
//        ResponseApi<Object> errorResponse = new ResponseApi<>(
//                false,
//                null,
//                ex.getMessage()
//        );
//        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
//    }
    
}