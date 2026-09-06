package com.com.empresa.helpdesk.dtos.requisicao;

public record TokenResponseDto(
        String token,
        String tipo,
        Long expiraEM
) {
}
