package com.capgemini.test.usuario;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.capgemini.sala.Sala;
import com.capgemini.usuario.Usuario;

public class UsuarioTest {
    
    @Test
    public void testUsuarioId() {
        Usuario usuario = new Usuario();
        Long id = 1L;
        String name = "pedro";
        String email = "test@ab.com";
        String phone = "123456789";
        String dni = "12345678A";
        String rol = "admin";
        usuario.setId(id);
        usuario.setName(name);
        usuario.setEmail(email);
        usuario.setPhone(phone);
        usuario.setDni(dni);
        usuario.setRol(rol);

        Assertions.assertEquals(name, usuario.getName(), "El nombre del usuario no coincide");
        Assertions.assertEquals(email, usuario.getEmail(), "El email del usuario no coincide");
        Assertions.assertEquals(phone, usuario.getPhone(), "El teléfono del usuario no coincide");
        Assertions.assertEquals(dni, usuario.getDni(), "El DNI del usuario no coincide");
        Assertions.assertEquals(rol, usuario.getRol(), "El rol del usuario no coincide");
        Assertions.assertEquals(id, usuario.getId(), "El ID del usuario no coincide");

        Sala sala = new Sala();
        sala.setId(1L);
        sala.setName("Sala 1");
        usuario.setSala(sala);
        Assertions.assertEquals(sala, usuario.getSala(), "La sala del usuario no coincide");
    }
}
    