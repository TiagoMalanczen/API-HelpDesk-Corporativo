package com.com.empresa.helpdesk.services;

import com.com.empresa.helpdesk.data.enums.PerfilUsuarioEnum;
import com.com.empresa.helpdesk.data.model.ChamadosEntity;
import com.com.empresa.helpdesk.data.model.ComentarioEntity;
import com.com.empresa.helpdesk.data.model.UsuarioEntity;
import com.com.empresa.helpdesk.data.repositories.ChamadoRepository;
import com.com.empresa.helpdesk.data.repositories.ComentariosRepository;
import com.com.empresa.helpdesk.data.repositories.UsuarioRepository;
import com.com.empresa.helpdesk.dtos.comentarios.ComentarioRequisicaoDto;
import com.com.empresa.helpdesk.dtos.comentarios.ComentarioResponseDto;
import com.com.empresa.helpdesk.exceptions.OperacaoNaoAutorizadaException;
import com.com.empresa.helpdesk.exceptions.RecursoNaoEncontradoException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ComentarioService {

    private final ComentariosRepository comentariosRepository;
    private final ChamadoRepository chamadoRepository;
    private final UsuarioRepository usuarioRepository;

    private UsuarioEntity getUsuarioAutenticado() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado."));
    }

    @Transactional
    public ComentarioResponseDto adicionarComentario(Long chamadoId, ComentarioRequisicaoDto dto) {
        ChamadosEntity chamado = chamadoRepository.findById(chamadoId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Chamado não encontrado."));

        UsuarioEntity autor = getUsuarioAutenticado();

        boolean isDono = chamado.getUsuario().getId().equals(autor.getId());
        boolean isTecnico = chamado.getTecnico() != null && chamado.getTecnico().getId().equals(autor.getId());
        boolean isAdmin = autor.getPerfil() == PerfilUsuarioEnum.ROLE_ADMIN;

        if (!isDono && !isTecnico && !isAdmin) {
            throw new OperacaoNaoAutorizadaException("Você não tem permissão para comentar neste chamado.");
        }

        ComentarioEntity comentario = new ComentarioEntity();
        comentario.setTexto(dto.texto());
        comentario.setChamado(chamado);
        comentario.setUser(autor);
        comentario.setDataCriacao(LocalDateTime.now());

        ComentarioEntity salvo = comentariosRepository.save(comentario);
        return toDto(salvo);
    }

    @Transactional(readOnly = true)
    public List<ComentarioResponseDto> listarComentariosPorChamado(Long chamadoId) {
        if (!chamadoRepository.existsById(chamadoId)) {
            throw new RecursoNaoEncontradoException("Chamado não encontrado.");
        }
        return comentariosRepository.findByChamadoIdOrderByDataCriacaoAsc(chamadoId)
                .stream()
                .map(this::toDto)
                .toList();
    }

    private ComentarioResponseDto toDto(ComentarioEntity entity) {
        return new ComentarioResponseDto(
                entity.getId(),
                entity.getTexto(),
                entity.getUser().getName(),
                entity.getUser().getPerfil(),
                entity.getDataCriacao()
        );
    }
}