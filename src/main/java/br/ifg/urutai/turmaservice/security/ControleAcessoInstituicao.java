
package br.ifg.urutai.turmaservice.security;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class ControleAcessoInstituicao {

    private JwtAuthenticationToken autenticacao() {

        Authentication autenticacao =
                SecurityContextHolder.getContext()
                        .getAuthentication();

        if (!(autenticacao instanceof JwtAuthenticationToken jwt)
                || !jwt.isAuthenticated()) {
            throw new AccessDeniedException(
                    "Usuario nao autenticado"
            );
        }

        return jwt;
    }

    public boolean administradorGeral() {

        return autenticacao().getAuthorities()
                .stream()
                .anyMatch(autoridade ->
                        autoridade.getAuthority()
                                .equals("ROLE_ADMINISTRADOR")
                );
    }

    public Long instituicaoId() {

        Object valor = autenticacao()
                .getToken()
                .getClaim("instituicaoId");

        if (!(valor instanceof Number numero)) {
            throw new AccessDeniedException(
                    "Instituicao nao identificada no token"
            );
        }

        return numero.longValue();
    }

    public void validarInstituicao(Long instituicaoId) {

        if (instituicaoId == null) {
            throw new AccessDeniedException(
                    "Recurso sem instituicao identificada"
            );
        }

        if (administradorGeral()) {
            return;
        }

        if (!Objects.equals(
                instituicaoId(),
                instituicaoId
        )) {
            throw new AccessDeniedException(
                    "Acesso negado a esta instituicao"
            );
        }
    }
}
