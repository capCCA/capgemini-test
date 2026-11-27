package com.capgemini.excepciones;

public class ErrorInternoException extends RuntimeException {
    public ErrorInternoException(String mensaje) {
        super(mensaje);
    }
}
