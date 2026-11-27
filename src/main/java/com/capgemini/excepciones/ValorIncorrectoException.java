package com.capgemini.excepciones;

public class ValorIncorrectoException extends RuntimeException {
    public ValorIncorrectoException(String mensaje) {
        super(mensaje);
    }
}
