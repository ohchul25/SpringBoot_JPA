package com.prac.source.common;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseApi<T> {
	
    private boolean success;
    private T data;
    private String message;


}
