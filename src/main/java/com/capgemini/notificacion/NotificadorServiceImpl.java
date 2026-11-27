package com.capgemini.notificacion;

import org.springframework.stereotype.Service;
import java.util.List;

import com.capgemini.usuario.UsuarioDto;

@Service
public class NotificadorServiceImpl implements NotificadorService {
 
    private final List<NotificadorUsuario> notificadores;

    public NotificadorServiceImpl(List<NotificadorUsuario> notificadores) {
        this.notificadores = notificadores;
    }

    @Override
    public void notificar(UsuarioDto usuario) throws Exception {
        for (NotificadorUsuario notificador : notificadores) {
            if (notificador.aplica(usuario)) {
                notificador.notificar(usuario);
            }
        }
    }
}