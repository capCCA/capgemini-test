package com.capgemini.notificacion;

import com.capgemini.usuario.UsuarioDto;

public interface NotificadorUsuario {
    boolean aplica(UsuarioDto usuario);
    void notificar(UsuarioDto usuario) throws Exception;
}
