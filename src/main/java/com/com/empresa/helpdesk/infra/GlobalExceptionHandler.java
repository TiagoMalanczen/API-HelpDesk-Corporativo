package com.com.empresa.helpdesk.infra;

import com.com.empresa.helpdesk.dtos.ErroResponseDto;
import com.com.empresa.helpdesk.exceptions.EmailJaCadastradoException;
import com.com.empresa.helpdesk.exceptions.OperacaoNaoAutorizadaException;
import com.com.empresa.helpdesk.exceptions.RecursoNaoEncontradoException;
import com.com.empresa.helpdesk.exceptions.RegraDeNegocioException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ErroResponseDto> handlerRecursoNaoEncontrado(RecursoNaoEncontradoException e, HttpServletRequest request){
        ErroResponseDto erro = new ErroResponseDto(
                (LocalDateTime.now()),
                HttpStatus.NOT_FOUND.value(),
                "Recurso nao encontrado",
                e.getMessage(),
                request.getRequestURI(),
                Map.of()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }
    @ExceptionHandler(OperacaoNaoAutorizadaException.class)
    public ResponseEntity<ErroResponseDto> handlerOperacaoNaoAutorizada(OperacaoNaoAutorizadaException e, HttpServletRequest request){
        ErroResponseDto erro = new ErroResponseDto(
                (LocalDateTime.now()),
                HttpStatus.FORBIDDEN.value(),
                "Operacao nao autorizada",
                e.getMessage(),
                request.getRequestURI(),
                Map.of()
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(erro);
    }

    @ExceptionHandler(EmailJaCadastradoException.class)
    public ResponseEntity<ErroResponseDto> handlerEmailJaCadastrado(EmailJaCadastradoException e, HttpServletRequest request){
        ErroResponseDto erro = new ErroResponseDto(
                (LocalDateTime.now()),
                HttpStatus.CONFLICT.value(),
                "Email ja cadastrado",
                e.getMessage(),
                request.getRequestURI(),
                Map.of()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(erro);
    }

    @ExceptionHandler(RegraDeNegocioException.class)
    public ResponseEntity<ErroResponseDto> handlerRegraDeNegocio(RegraDeNegocioException e, HttpServletRequest request){
        ErroResponseDto erro = new ErroResponseDto(
                (LocalDateTime.now()),
                HttpStatus.BAD_REQUEST.value(),
                "Regra de negocio",
                e.getMessage(),
                request.getRequestURI(),
                Map.of()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponseDto> handlerMethodArgumentNotValid(MethodArgumentNotValidException e, HttpServletRequest request){

        Map<String, String> erros = new HashMap<>();
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            erros.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        ErroResponseDto erro = new ErroResponseDto(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Erro de validacao nos campos informados",
                "Erro de validacao",
                request.getRequestURI(),
                erros
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErroResponseDto> handlerAccessDenied(AccessDeniedException e, HttpServletRequest request) {
        ErroResponseDto erro = new ErroResponseDto(
                LocalDateTime.now(),
                HttpStatus.FORBIDDEN.value(),
                "Acesso Negado",
                "Você não tem permissão para acessar este recurso.",
                request.getRequestURI(),
                Map.of()
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(erro);
    }
}
