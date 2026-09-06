package com.com.empresa.helpdesk.dtos.chamados;

import com.com.empresa.helpdesk.data.enums.PrioridadeEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CriarChamadoDto(
        @NotBlank @Size(min = 5, max = 100) String titulo,
        @NotBlank String descricao,
        @NotNull PrioridadeEnum prioridade
        ) {
}
