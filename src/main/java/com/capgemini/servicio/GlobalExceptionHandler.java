package com.capgemini.servicio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.capgemini.response.ResponseUtils;

import com.capgemini.excepciones.ErrorInternoException;
import com.capgemini.excepciones.UsuarioNoEncontradoException;
import com.capgemini.excepciones.ValorIncorrectoException;
import com.capgemini.response.ErrorResponse;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(UsuarioNoEncontradoException.class)
    public ResponseEntity<Map<String, Object>> handleNoEncontrado(UsuarioNoEncontradoException e) {
        ErrorResponse errorResponse = new ErrorResponse(404, e.getMessage());
        return ResponseUtils.returnErrorResponse(errorResponse);
    }

    @ExceptionHandler(ErrorInternoException.class)
    public ResponseEntity<Map<String, Object>> handleErrorInterno(ErrorInternoException e) {
        logger.error("Error interno: {}", e.getMessage(), e);
        ErrorResponse errorResponse = new ErrorResponse(500, e.getMessage());
        return ResponseUtils.returnErrorResponse(errorResponse);
    }

    @ExceptionHandler(ValorIncorrectoException.class)
    public ResponseEntity<Map<String, Object>> handleValorIncorrecto(ValorIncorrectoException e) {
        ErrorResponse errorResponse = new ErrorResponse(409, e.getMessage());
        return ResponseUtils.returnErrorResponse(errorResponse);
    }
}
