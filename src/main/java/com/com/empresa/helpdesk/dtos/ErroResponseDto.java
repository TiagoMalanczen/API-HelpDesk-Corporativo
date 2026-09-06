package com.com.empresa.helpdesk.dtos;

import java.time.LocalDateTime;
import java.util.Map;

public record ErroResponseDto(
        LocalDateTime timestamp,
        Integer status,
        String erro,
        String mensagem,
        String path,
        Map<String , String> errosValidacao
) {
}
