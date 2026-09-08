package com.com.empresa.helpdesk.controllers;

import com.com.empresa.helpdesk.dtos.chamados.AtualizarStatusDto;
import com.com.empresa.helpdesk.dtos.chamados.ChamadoRespostaDto;
import com.com.empresa.helpdesk.dtos.chamados.CriarChamadoDto;
import com.com.empresa.helpdesk.services.ChamadoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/chamados")
@RequiredArgsConstructor
public class ChamadoController {

    private final ChamadoService chamadoService;

    @PostMapping
    public ResponseEntity<ChamadoRespostaDto> criar(@RequestBody @Valid CriarChamadoDto dto) {
        ChamadoRespostaDto resposta = chamadoService.criarChamado(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
    }

    @GetMapping
    public ResponseEntity<Page<ChamadoRespostaDto>> listar(@PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(chamadoService.listarChamados(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChamadoRespostaDto> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(chamadoService.buscarPorId(id));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('TECNICO', 'ADMIN')")
    public ResponseEntity<ChamadoRespostaDto> atualizarStatus(
            @PathVariable Long id,
            @RequestBody @Valid AtualizarStatusDto dto
    ) {
        return ResponseEntity.ok(chamadoService.atualizarStatus(id, dto));
    }

    @PatchMapping("/{id}/atribuir/{tecnicoId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ChamadoRespostaDto> atribuirTecnico(
            @PathVariable Long id,
            @PathVariable Long tecnicoId
    ) {
        return ResponseEntity.ok(chamadoService.atribuirTecnico(id, tecnicoId));
    }
}