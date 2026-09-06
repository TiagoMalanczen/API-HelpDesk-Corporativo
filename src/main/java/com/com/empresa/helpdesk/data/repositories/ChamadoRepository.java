    package com.com.empresa.helpdesk.data.repositories;

    import com.com.empresa.helpdesk.data.enums.StatusChamadoEnum;
    import com.com.empresa.helpdesk.data.model.ChamadosEntity;
    import org.springframework.data.domain.Page;
    import org.springframework.data.domain.Pageable;
    import org.springframework.data.jpa.repository.JpaRepository;

    public interface ChamadoRepository extends JpaRepository<ChamadosEntity, Long> {

        Page<ChamadosEntity> findByClienteId(Long clienteId, Pageable pageable);
        Page<ChamadosEntity> findByStatus(StatusChamadoEnum status, Pageable pageable);
        Page<ChamadosEntity> findByTecnicoId(Long tecnicoId, Pageable pageable);

    }
