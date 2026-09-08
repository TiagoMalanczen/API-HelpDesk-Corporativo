package com.com.empresa.helpdesk.controllers;

import com.com.empresa.helpdesk.dtos.requisicao.LoginRequisicaoDto;
import com.com.empresa.helpdesk.dtos.requisicao.RegistroUsuarioDto;
import com.com.empresa.helpdesk.dtos.requisicao.TokenResponseDto;
import com.com.empresa.helpdesk.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody @Valid RegistroUsuarioDto dto) {
        authService.registrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(@RequestBody @Valid LoginRequisicaoDto dto) {
        TokenResponseDto token = authService.login(dto);
        return ResponseEntity.ok(token);
    }
}