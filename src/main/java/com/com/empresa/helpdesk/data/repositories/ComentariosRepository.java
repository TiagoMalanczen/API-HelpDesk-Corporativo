package com.com.empresa.helpdesk.data.repositories;

import com.com.empresa.helpdesk.data.model.ComentarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComentariosRepository extends JpaRepository<ComentarioEntity, Long> {

    List<ComentarioEntity> findByChamadoIdOrderByDataCriacaoAsc(Long chamadoId);

}
