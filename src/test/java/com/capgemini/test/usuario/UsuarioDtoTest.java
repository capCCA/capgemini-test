package com.capgemini.test.usuario;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.capgemini.sala.SalaDto;
import com.capgemini.usuario.UsuarioDto;

public class UsuarioDtoTest {
    @Test
    public void testUsuarioId() {
        UsuarioDto usuarioDto = new UsuarioDto();
        Long id = 1L;
        String name = "pedro";
        String email = "test@ab.com";
        String phone = "123456789";
        String dni = "12345678A";
        String rol = "admin";
        usuarioDto.setId(id);
        usuarioDto.setName(name);
        usuarioDto.setEmail(email);
        usuarioDto.setPhone(phone);
        usuarioDto.setDni(dni);
        usuarioDto.setRol(rol);

        Assertions.assertEquals(name, usuarioDto.getName(), "El nombre del usuario no coincide");
        Assertions.assertEquals(email, usuarioDto.getEmail(), "El email del usuario no coincide");
        Assertions.assertEquals(phone, usuarioDto.getPhone(), "El teléfono del usuario no coincide");
        Assertions.assertEquals(dni, usuarioDto.getDni(), "El DNI del usuario no coincide");
        Assertions.assertEquals(rol, usuarioDto.getRol(), "El rol del usuario no coincide");
        Assertions.assertEquals(id, usuarioDto.getId(), "El ID del usuario no coincide");

        SalaDto salaDto = new SalaDto();
        salaDto.setId(1L);
        salaDto.setName("Sala 1");
        usuarioDto.setSala(salaDto);
        Assertions.assertEquals(salaDto, usuarioDto.getSala(), "La sala del usuario no coincide");
    }
}
    