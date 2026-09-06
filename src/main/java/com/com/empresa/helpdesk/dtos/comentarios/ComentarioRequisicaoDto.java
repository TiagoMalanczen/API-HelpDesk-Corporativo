package com.com.empresa.helpdesk.dtos.comentarios;

import jakarta.validation.constraints.NotBlank;

public record ComentarioRequisicaoDto(
        @NotBlank String texto
) {
}
