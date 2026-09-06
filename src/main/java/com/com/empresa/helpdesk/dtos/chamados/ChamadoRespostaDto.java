package com.com.empresa.helpdesk.dtos.chamados;

import com.com.empresa.helpdesk.data.enums.PrioridadeEnum;
import com.com.empresa.helpdesk.data.enums.StatusChamadoEnum;

import java.time.LocalDateTime;

public record ChamadoRespostaDto (
        Long id,
        String titulo,
        String descricao,
        StatusChamadoEnum status,
        PrioridadeEnum prioridade,
        Long clienteId,
        String clienteNome,
        Long tecnicoId,
        String tecnicoNome,
        LocalDateTime dataAbertura,
        LocalDateTime dataFechamento
){}
