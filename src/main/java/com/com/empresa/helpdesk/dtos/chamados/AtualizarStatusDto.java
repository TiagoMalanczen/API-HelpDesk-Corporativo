package com.com.empresa.helpdesk.dtos.chamados;

import com.com.empresa.helpdesk.data.enums.StatusChamadoEnum;
import jakarta.validation.constraints.NotNull;

public record AtualizarStatusDto(
        @NotNull StatusChamadoEnum status
) {
}
