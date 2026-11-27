package com.capgemini.response;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;

public class ResponseUtils {

    public static ResponseEntity<Object> returnOKResponse(Object value) {
        return ResponseEntity.status(200).body(value);
    }

    public static ResponseEntity<Object> returnCreateResponse(Object value) {
        return ResponseEntity.status(201).body(value);
    }

    public static ResponseEntity<Map<String, Object>> returnErrorResponse(ErrorResponse errorResponse) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("code", errorResponse.getCode());
        body.put("message", errorResponse.getMessage());
        return ResponseEntity.status(errorResponse.getCode()).body(body);
    }
    
}
