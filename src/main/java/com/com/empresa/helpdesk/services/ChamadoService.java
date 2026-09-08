package com.com.empresa.helpdesk.services;

import com.com.empresa.helpdesk.data.enums.PerfilUsuarioEnum;
import com.com.empresa.helpdesk.data.enums.StatusChamadoEnum;
import com.com.empresa.helpdesk.data.model.ChamadosEntity;
import com.com.empresa.helpdesk.data.model.UsuarioEntity;
import com.com.empresa.helpdesk.data.repositories.ChamadoRepository;
import com.com.empresa.helpdesk.data.repositories.UsuarioRepository;
import com.com.empresa.helpdesk.dtos.chamados.AtualizarStatusDto;
import com.com.empresa.helpdesk.dtos.chamados.ChamadoRespostaDto;
import com.com.empresa.helpdesk.dtos.chamados.CriarChamadoDto;
import com.com.empresa.helpdesk.exceptions.OperacaoNaoAutorizadaException;
import com.com.empresa.helpdesk.exceptions.RecursoNaoEncontradoException;
import com.com.empresa.helpdesk.exceptions.RegraDeNegocioException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ChamadoService {

    private final ChamadoRepository chamadoRepository;
    private final UsuarioRepository usuarioRepository;

    private UsuarioEntity getUsuarioAutenticado() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário autenticado não encontrado."));
    }

    @Transactional
    public ChamadoRespostaDto criarChamado(CriarChamadoDto dto) {
        UsuarioEntity cliente = getUsuarioAutenticado();

        ChamadosEntity chamado = new ChamadosEntity();
        chamado.setTitulo(dto.titulo());
        chamado.setDescricao(dto.descricao());
        chamado.setPrioridade(dto.prioridade());
        chamado.setStatus(StatusChamadoEnum.ABERTO);
        chamado.setDataAbertura(LocalDateTime.now());
        chamado.setUsuario(cliente);

        ChamadosEntity salvo = chamadoRepository.save(chamado);
        return toDto(salvo);
    }

    @Transactional(readOnly = true)
    public Page<ChamadoRespostaDto> listarChamados(Pageable pageable) {
        UsuarioEntity usuario = getUsuarioAutenticado();

        if (usuario.getPerfil() == PerfilUsuarioEnum.ROLE_CLIENTE) {
            return chamadoRepository.findByUsuario_Id(usuario.getId(), pageable).map(this::toDto);
        } else if (usuario.getPerfil() == PerfilUsuarioEnum.ROLE_TECNICO) {
            return chamadoRepository.findByTecnico_Id(usuario.getId(), pageable).map(this::toDto);
        }

        return chamadoRepository.findAll(pageable).map(this::toDto);
    }

    @Transactional(readOnly = true)
    public ChamadoRespostaDto buscarPorId(Long id) {
        ChamadosEntity chamado = chamadoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Chamado não encontrado."));

        UsuarioEntity usuario = getUsuarioAutenticado();

        boolean isDono = chamado.getUsuario().getId().equals(usuario.getId());
        boolean isTecnicoAtribuido = chamado.getTecnico() != null && chamado.getTecnico().getId().equals(usuario.getId());
        boolean isAdmin = usuario.getPerfil() == PerfilUsuarioEnum.ROLE_ADMIN;

        if (!isDono && !isTecnicoAtribuido && !isAdmin) {
            throw new OperacaoNaoAutorizadaException("Você não tem permissão para visualizar este chamado.");
        }

        return toDto(chamado);
    }

    @Transactional
    public ChamadoRespostaDto atualizarStatus(Long id, AtualizarStatusDto dto) {
        ChamadosEntity chamado = chamadoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Chamado não encontrado."));

        UsuarioEntity usuario = getUsuarioAutenticado();

        if (usuario.getPerfil() == PerfilUsuarioEnum.ROLE_CLIENTE) {
            throw new OperacaoNaoAutorizadaException("Clientes não podem alterar o status de um chamado.");
        }

        if (chamado.getStatus() == StatusChamadoEnum.RESOLVIDO || chamado.getStatus() == StatusChamadoEnum.CANCELADO) {
            throw new RegraDeNegocioException("Não é possível alterar o status de um chamado já finalizado.");
        }

        chamado.setStatus(dto.status());
        if (dto.status() == StatusChamadoEnum.RESOLVIDO || dto.status() == StatusChamadoEnum.CANCELADO) {
            chamado.setDataFechamento(LocalDateTime.now());
        }

        return toDto(chamado);
    }

    @Transactional
    public ChamadoRespostaDto atribuirTecnico(Long chamadoId, Long tecnicoId) {
        ChamadosEntity chamado = chamadoRepository.findById(chamadoId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Chamado não encontrado."));

        UsuarioEntity tecnico = usuarioRepository.findById(tecnicoId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Técnico não encontrado."));

        if (tecnico.getPerfil() != PerfilUsuarioEnum.ROLE_TECNICO) {
            throw new RegraDeNegocioException("O usuário selecionado não é um técnico.");
        }

        chamado.setTecnico(tecnico);
        if (chamado.getStatus() == StatusChamadoEnum.ABERTO) {
            chamado.setStatus(StatusChamadoEnum.EM_ANDAMENTO);
        }

        return toDto(chamado);
    }

    private ChamadoRespostaDto toDto(ChamadosEntity entity) {
        return new ChamadoRespostaDto(
                entity.getId(),
                entity.getTitulo(),
                entity.getDescricao(),
                entity.getStatus(),
                entity.getPrioridade(),
                entity.getUsuario() != null ? entity.getUsuario().getId() : null,
                entity.getUsuario() != null ? entity.getUsuario().getName() : null,
                entity.getTecnico() != null ? entity.getTecnico().getId() : null,
                entity.getTecnico() != null ? entity.getTecnico().getName() : null,
                entity.getDataAbertura(),
                entity.getDataFechamento()
        );
    }
}