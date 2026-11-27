package com.capgemini.usuario;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface UsuarioRepository extends CrudRepository<Usuario, Long> {

    boolean existsByEmail(String email);

    Optional<Usuario> findByIdAndSala_Id(Long id, Long salaId);
}
