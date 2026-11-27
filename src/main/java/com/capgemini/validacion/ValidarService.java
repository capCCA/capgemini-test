package com.capgemini.validacion;

public interface ValidarService {
    
    boolean validarUserName(String userName);

    boolean validarDNI(String dni);

    boolean validarEmail(String email);

    boolean validarRol(String rol);
}
