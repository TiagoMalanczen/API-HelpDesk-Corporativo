package com.com.empresa.helpdesk.controllers;

import com.com.empresa.helpdesk.dtos.comentarios.ComentarioRequisicaoDto;
import com.com.empresa.helpdesk.dtos.comentarios.ComentarioResponseDto;
import com.com.empresa.helpdesk.services.ComentarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/chamados/{chamadoId}/comentarios")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ComentarioController {

    private final ComentarioService comentarioService;

    @PostMapping
    public ResponseEntity<ComentarioResponseDto> adicionar(
            @PathVariable Long chamadoId,
            @RequestBody @Valid ComentarioRequisicaoDto dto
    ) {
        ComentarioResponseDto resposta = comentarioService.adicionarComentario(chamadoId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
    }

    @GetMapping
    public ResponseEntity<List<ComentarioResponseDto>> listar(@PathVariable Long chamadoId) {
        return ResponseEntity.ok(comentarioService.listarComentariosPorChamado(chamadoId));
    }
}