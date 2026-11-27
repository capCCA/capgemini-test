package com.capgemini.validacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import com.capgemini.excepciones.ErrorInternoException;
import com.capgemini.mock.MockService;
import com.capgemini.usuario.UsuarioService;

@Service
public class ValidarServiceImpl implements ValidarService {

    @Autowired
    private MockService mockService;

    @Autowired
    private UsuarioService usuarioService;

    @Override
    public boolean validarUserName(String userName) {
        return userName != null && !userName.isEmpty() && userName.length() < 7;
    }

    @Override
    public boolean validarDNI(String dni) {
        if (dni == null || dni.isEmpty()) {
            return false;
        }
        try {
            return mockService.checkDni(dni); 

        } catch (HttpClientErrorException e) {
            // Si devuelve error 409, entrara aqui y es solo validacion falsa
            return HttpStatus.OK.equals(e.getStatusCode());
        } catch (Exception e) {
            throw new ErrorInternoException(e.getMessage());
        }
    }

    @Override
    public boolean validarEmail(String email) {
        return email!= null && email.contains("@") &&
            email.contains(".") && !usuarioService.existeUsuarioByEmail(email);
    }

    @Override
    public boolean validarRol(String rol) {
        return rol != null && !rol.isEmpty() &&
            ("ADMIN".equals(rol.toUpperCase()) || "SUPERADMIN".equals(rol.toUpperCase())) ;
    }
    
}
