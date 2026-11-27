package com.capgemini.excepciones;

public class UsuarioNoEncontradoException extends RuntimeException {
    public UsuarioNoEncontradoException(Long id, Long salaId) {
        super("Usuario no encontrado con id " + id + " para la sala " + salaId);
    }
}
