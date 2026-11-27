package com.capgemini.notificacion;
import com.capgemini.mock.MockService;
import com.capgemini.usuario.UsuarioDto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Component
public class NotificadorSuperAdmin implements NotificadorUsuario {
    
    private static final Logger logger = LoggerFactory.getLogger(NotificadorSuperAdmin.class);

    @Autowired
    private MockService mockService;

    public boolean aplica(UsuarioDto usuarioDto) {
        return "SUPERADMIN".equalsIgnoreCase(usuarioDto.getRol());
    }

    public void notificar(UsuarioDto usuarioDto) throws Exception {
        HttpStatusCode resultado = mockService.sendSmsUser(usuarioDto.getPhone(), "usuario guardado");
        if (HttpStatus.OK.equals(resultado)) {
           logger.info("Usuario superadmin notificado por phone: " + usuarioDto.getPhone());
        } else {
            logger.error("Error al notificar al usuario superadmin por phone: " + usuarioDto.getPhone());
        }
    }
}
