package br.ifg.urutai.turmaservice.security;

import br.ifg.urutai.turmaservice.exception.TratadorGlobalExcecoes.ErroResposta;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Map;

@Component
public class RespostaErroSeguranca implements AuthenticationEntryPoint, AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    public RespostaErroSeguranca(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(
            HttpServletRequest requisicao,
            HttpServletResponse resposta,
            AuthenticationException erro) throws IOException {

        resposta.setHeader(HttpHeaders.WWW_AUTHENTICATE, "Bearer");

        escrever(
                resposta,
                requisicao,
                HttpStatus.UNAUTHORIZED,
                "Autenticação necessária: token ausente, inválido ou expirado"
        );
    }

    @Override
    public void handle(
            HttpServletRequest requisicao,
            HttpServletResponse resposta,
            AccessDeniedException erro) throws IOException {

        escrever(
                resposta,
                requisicao,
                HttpStatus.FORBIDDEN,
                "Acesso negado: perfil sem permissão para esta operação"
        );
    }

    private void escrever(
            HttpServletResponse resposta,
            HttpServletRequest requisicao,
            HttpStatus status,
            String mensagem) throws IOException {

        ErroResposta corpo = new ErroResposta(
                Instant.now(),
                status.value(),
                mensagem,
                requisicao.getRequestURI(),
                Map.of()
        );

        resposta.setStatus(status.value());
        resposta.setContentType(MediaType.APPLICATION_JSON_VALUE);
        resposta.setCharacterEncoding(StandardCharsets.UTF_8.name());

        objectMapper.writeValue(resposta.getOutputStream(), corpo);
    }
}
