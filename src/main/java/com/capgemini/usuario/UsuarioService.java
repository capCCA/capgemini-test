package com.capgemini.usuario;

import com.capgemini.excepciones.UsuarioNoEncontradoException;
import com.capgemini.excepciones.ErrorInternoException;

public interface UsuarioService {
    
    Long saveUsuario(UsuarioDto usuarioDto) throws ErrorInternoException;

    boolean existeUsuarioByEmail(String email);

    Usuario getUsuarioByIdYSala(Long id, Long salaId) throws UsuarioNoEncontradoException;
}
