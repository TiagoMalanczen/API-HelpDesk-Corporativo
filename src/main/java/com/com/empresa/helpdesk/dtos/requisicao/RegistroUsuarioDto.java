package com.com.empresa.helpdesk.dtos.requisicao;

import com.com.empresa.helpdesk.data.enums.PerfilUsuarioEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegistroUsuarioDto(
        @NotBlank String nome,
        @NotBlank @Email String email,
        @NotBlank @Size(min = 6, message = "a senha deve conter no min 6 caracteres") String senha,
        @NotNull PerfilUsuarioEnum perfil
        ) {
}
