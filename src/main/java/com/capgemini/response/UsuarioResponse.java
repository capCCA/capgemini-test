package com.capgemini.response;

import com.capgemini.usuario.Usuario;

public record UsuarioResponse(Long id, String name, String email, String phone, String rol, String dni, Long salaId) {

    public static UsuarioResponse from(Usuario usuario) {
        return new UsuarioResponse(
            usuario.getId(),
            usuario.getName(),
            usuario.getEmail(),
            usuario.getPhone(),
            usuario.getRol(),
            usuario.getDni(),
            usuario.getSala().getId());
    }
}
