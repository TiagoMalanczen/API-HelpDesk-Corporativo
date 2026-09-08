package com.com.empresa.helpdesk.services;

import com.com.empresa.helpdesk.config.TokenProvider;
import com.com.empresa.helpdesk.data.enums.PerfilUsuarioEnum;
import com.com.empresa.helpdesk.data.model.UsuarioEntity;
import com.com.empresa.helpdesk.data.repositories.UsuarioRepository;
import com.com.empresa.helpdesk.dtos.requisicao.LoginRequisicaoDto;
import com.com.empresa.helpdesk.dtos.requisicao.RegistroUsuarioDto;
import com.com.empresa.helpdesk.dtos.requisicao.TokenResponseDto;
import com.com.empresa.helpdesk.exceptions.EmailJaCadastradoException;
import com.com.empresa.helpdesk.exceptions.OperacaoNaoAutorizadaException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;

    @Value("${jwt.expiration}")
    private long expirationTime;

    @Transactional
    public void registrar(RegistroUsuarioDto registroUsuarioDto) {
        if (usuarioRepository.existsByEmail(registroUsuarioDto.email())) {
            throw new EmailJaCadastradoException("Email já cadastrado.");
        }

        PerfilUsuarioEnum perfilFinal = PerfilUsuarioEnum.ROLE_CLIENTE;

        if (registroUsuarioDto.perfil() == PerfilUsuarioEnum.ROLE_ADMIN) {
            var auth = SecurityContextHolder.getContext().getAuthentication();
            boolean isAdmin = auth != null
                    && auth.isAuthenticated()
                    && auth.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

            if (!isAdmin) {
                throw new OperacaoNaoAutorizadaException("Apenas administradores autenticados podem cadastrar novos admins.");
            }
            perfilFinal = PerfilUsuarioEnum.ROLE_ADMIN;
        } else if (registroUsuarioDto.perfil() != null) {
            perfilFinal = registroUsuarioDto.perfil();
        }

        UsuarioEntity usuario = UsuarioEntity.builder()
                .name(registroUsuarioDto.nome())
                .email(registroUsuarioDto.email())
                .senha(passwordEncoder.encode(registroUsuarioDto.senha()))
                .perfil(perfilFinal)
                .ativo(true)
                .build();

        usuarioRepository.save(usuario);
    }

    public TokenResponseDto login(LoginRequisicaoDto dto) {
        var authToken = new UsernamePasswordAuthenticationToken(dto.email(), dto.senha());
        Authentication authentication = authenticationManager.authenticate(authToken);
        String token = tokenProvider.gerarToken(authentication);
        return new TokenResponseDto(token, "Bearer", expirationTime);
    }
}