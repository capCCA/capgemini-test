package com.capgemini.notificacion;

import com.capgemini.usuario.UsuarioDto;

public interface NotificadorService {
    
    void notificar(UsuarioDto usuario) throws Exception;
}
