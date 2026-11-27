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
public class NotificadorAdmin implements NotificadorUsuario {
    
    private static final Logger logger = LoggerFactory.getLogger(NotificadorAdmin.class);

    @Autowired
    private MockService mockService;

    public boolean aplica(UsuarioDto usuarioDto) {
        return "ADMIN".equalsIgnoreCase(usuarioDto.getRol());
    }

    public void notificar(UsuarioDto usuarioDto) throws Exception {
        HttpStatusCode resultado = mockService.emailUser(usuarioDto.getEmail(), "usuario guardado");
        if (HttpStatus.OK.equals(resultado)) {
           logger.info("Usuario admin notificado por email: " + usuarioDto.getEmail());
        } else {
            logger.error("Error al notificar al usuario admin por email: " + usuarioDto.getEmail());
        }
    }
}
