package com.com.empresa.helpdesk.dtos.comentarios;

import com.com.empresa.helpdesk.data.enums.PerfilUsuarioEnum;

import java.time.LocalDateTime;

public record ComentarioResponseDto(
        Long id,
        String texto,
        String autorNome,
        PerfilUsuarioEnum autorPerfil,
        LocalDateTime dataCriacao
) {
}
