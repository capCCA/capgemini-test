package com.capgemini.test.response;

import org.springframework.http.ResponseEntity;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Map;

import com.capgemini.response.ErrorResponse;
import com.capgemini.response.ResponseUtils;

public class ResponseUtilsTest {

    @Test 
    public void testReturnOKResponseOK() {
        // GIVEN
        String id = "123";

        // WHEN
        ResponseEntity<Object> respuesta = ResponseUtils.returnOKResponse(id);

        // THEN
        assertNotNull(respuesta);
        assertEquals(200, respuesta.getStatusCode().value());
        assertEquals(id, respuesta.getBody());
    }

    @Test 
    public void testReturnCreateResponseOK() {
        // GIVEN
        String id = "123";

        // WHEN
        ResponseEntity<Object> respuesta = ResponseUtils.returnCreateResponse(id);

        // THEN
        assertNotNull(respuesta);
        assertEquals(201, respuesta.getStatusCode().value());
        assertEquals(id, respuesta.getBody());
    }

    @Test 
    public void testReturnErrorResponseOK() {
        // GIVEN
        int code = 409;
        String message = "Conflict";
        ErrorResponse errorResponse = new ErrorResponse(code, message);

        // WHEN
        ResponseEntity<Map<String, Object>> respuesta = ResponseUtils.returnErrorResponse(errorResponse);

        // THEN
        assertNotNull(respuesta);
        assertEquals(code, respuesta.getStatusCode().value());
        assertEquals(message, respuesta.getBody().get("message"));
        assertEquals(code, respuesta.getBody().get("code"));
    }
}
