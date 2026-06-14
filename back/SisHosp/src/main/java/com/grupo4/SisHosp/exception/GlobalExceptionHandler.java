package com.grupo4.SisHosp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.LinkedHashMap;
import java.util.Map;


@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(EntidadeNaoEncontradaException.class)
    public ResponseEntity<Map<String, Object>> tratarNaoEncontrada(EntidadeNaoEncontradaException e) {
        return montarResposta(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(QuartoIndisponivelException.class)
    public ResponseEntity<Map<String, Object>> tratarQuartoIndisponivel(QuartoIndisponivelException e) {
        return montarResposta(HttpStatus.CONFLICT, e.getMessage());
    }

    @ExceptionHandler(CapacidadeExcedidaException.class)
    public ResponseEntity<Map<String, Object>> tratarCapacidade(CapacidadeExcedidaException e) {
        return montarResposta(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(DataInvalidaException.class)
    public ResponseEntity<Map<String, Object>> tratarDataInvalida(DataInvalidaException e) {
        return montarResposta(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(RecursoNaoPermitidoException.class)
    public ResponseEntity<Map<String, Object>> tratarRecursoNaoPermitido(RecursoNaoPermitidoException e) {
        return montarResposta(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, Object>> tratarEstadoInvalido(IllegalStateException e) {
        return montarResposta(HttpStatus.CONFLICT, e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> tratarArgumentoInvalido(IllegalArgumentException e) {
        return montarResposta(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<Map<String, Object>> tratarDataMalFormatada(DateTimeParseException e) {
        return montarResposta(HttpStatus.BAD_REQUEST,
                "Formato de data inválido. Use: 2026-01-20T14:00:00");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> tratarValidacao(MethodArgumentNotValidException e) {
        String mensagem = e.getBindingResult().getFieldErrors().stream().map(erro -> erro.getField() + ": " + erro.getDefaultMessage()).reduce((a, b) -> a + "; " + b).orElse("Dados inválidos");
        return montarResposta(HttpStatus.BAD_REQUEST, mensagem);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> tratarGenerica(Exception e) {
        return montarResposta(HttpStatus.INTERNAL_SERVER_ERROR,"Erro interno no servidor: " + e.getMessage());
    }

    private ResponseEntity<Map<String, Object>> montarResposta(HttpStatus status, String mensagem) {
        Map<String, Object> corpo = new LinkedHashMap<>();
        corpo.put("timestamp", LocalDateTime.now());
        corpo.put("status", status.value());
        corpo.put("erro", status.getReasonPhrase());
        corpo.put("mensagem", mensagem);
        return ResponseEntity.status(status).body(corpo);
    }
}

