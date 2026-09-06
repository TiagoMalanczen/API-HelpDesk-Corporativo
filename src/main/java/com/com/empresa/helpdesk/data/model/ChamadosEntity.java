package com.com.empresa.helpdesk.data.model;


import com.com.empresa.helpdesk.data.enums.PrioridadeEnum;
import com.com.empresa.helpdesk.data.enums.StatusChamadoEnum;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "chamados")
@EqualsAndHashCode(of = "id")
public class ChamadosEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String titulo;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String descricao;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusChamadoEnum status;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PrioridadeEnum prioridade;

    @Column(nullable = false)
    private LocalDateTime dataAbertura = LocalDateTime.now();

    @Column
    private LocalDateTime dataFechamento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private UsuarioEntity usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tecnico_id")
    private UsuarioEntity tecnico;

}
