package com.com.empresa.helpdesk.dtos.requisicao;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequisicaoDto(
        @NotBlank @Email String email,
        @NotBlank String senha
) {
}
