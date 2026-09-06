package com.com.empresa.helpdesk.data.repositories;

import com.com.empresa.helpdesk.data.model.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {

    Optional<UsuarioEntity> findByEmail(String email);
    boolean existsByEmail(String email);
}
