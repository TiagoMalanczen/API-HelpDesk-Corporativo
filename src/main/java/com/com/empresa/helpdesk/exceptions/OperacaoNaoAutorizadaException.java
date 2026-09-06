package com.com.empresa.helpdesk.exceptions;

public class OperacaoNaoAutorizadaException extends RuntimeException {
    public OperacaoNaoAutorizadaException(String mensagem) {
        super(mensagem);
    }
}