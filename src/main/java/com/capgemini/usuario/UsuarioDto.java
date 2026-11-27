package com.capgemini.usuario;

import com.capgemini.sala.SalaDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDto {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String rol;
    private String dni;

    private SalaDto sala;
}
