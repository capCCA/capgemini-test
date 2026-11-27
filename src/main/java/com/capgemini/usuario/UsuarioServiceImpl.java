package com.capgemini.usuario;

import org.springframework.stereotype.Service;

import com.capgemini.excepciones.UsuarioNoEncontradoException;
import com.capgemini.excepciones.ErrorInternoException;
import com.capgemini.sala.Sala;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import com.capgemini.sala.SalaRepository;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    UsuarioRepository usuarioRepository;
    
    @Autowired
    SalaRepository salaRepository;

    @Override
    public Long saveUsuario(UsuarioDto usuarioDto) throws ErrorInternoException {
        if (usuarioDto.getSala() == null) {
            throw new ErrorInternoException("El usuario debe indicar una sala");
        }
        Long salaId = usuarioDto.getSala().getId();
        Sala sala = salaRepository.findById(salaId)
            .orElseThrow(() -> new ErrorInternoException("Sala no encontrada con id " + salaId));

        Usuario usuario = new Usuario();
        BeanUtils.copyProperties(usuarioDto, usuario, "id", "sala");
        usuario.setSala(sala);

        Usuario guardado = usuarioRepository.save(usuario);
        return guardado.getId();
    }

    @Override
    public boolean existeUsuarioByEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    @Override
    public Usuario getUsuarioByIdYSala(Long id, Long salaId) throws UsuarioNoEncontradoException {
        return usuarioRepository.findByIdAndSala_Id(id, salaId)
            .orElseThrow(() -> new UsuarioNoEncontradoException(id, salaId));
    }

}