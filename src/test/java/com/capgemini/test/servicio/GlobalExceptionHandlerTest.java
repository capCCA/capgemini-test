package com.capgemini.test.servicio;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.capgemini.excepciones.ErrorInternoException;
import com.capgemini.excepciones.UsuarioNoEncontradoException;
import com.capgemini.excepciones.ValorIncorrectoException;

import com.capgemini.servicio.GlobalExceptionHandler;

public class GlobalExceptionHandlerTest {
    
    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    @Test 
    public void testHandleNoEncontradoOK() {
        // GIVEN
        Long usuarioId = 1L;
        Long otroId = 1L;

        // WHEN
        ResponseEntity<Map<String, Object>> respuesta =
            handler.handleNoEncontrado(new UsuarioNoEncontradoException(usuarioId , otroId));
        
        // THEN
        assertEquals(HttpStatus.NOT_FOUND, respuesta.getStatusCode());
        assertEquals(404, respuesta.getBody().get("code"));
        assertEquals("Usuario no encontrado con id 1 para la sala 1", respuesta.getBody().get("message"));
    }

    @Test 
    public void testHandleErrorInterno() {
        // GIVEN
        String mensaje = "Error interno";

        ResponseEntity<Map<String, Object>> respuesta =
            handler.handleErrorInterno(new ErrorInternoException(mensaje));
        
        // THEN
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, respuesta.getStatusCode());
        assertEquals(500, respuesta.getBody().get("code"));
        assertEquals(mensaje, respuesta.getBody().get("message"));
    }

    @Test
    public void testHandleValorIncorrecto() {
        // GIVEN
        String mensaje = "Valor incorrecto";

        // WHEN
        ResponseEntity<Map<String, Object>> respuesta =
            handler.handleValorIncorrecto(new ValorIncorrectoException(mensaje));

        // THEN
        assertEquals(HttpStatus.CONFLICT, respuesta.getStatusCode());
        assertEquals(409, respuesta.getBody().get("code"));
        assertEquals(mensaje, respuesta.getBody().get("message"));
    }
}
