package br.ifg.urutai.turmaservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

@Configuration
@EnableMethodSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationConverter conversorJwt) {

        http.csrf(AbstractHttpConfigurer::disable)

                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth

                        // Swagger: documentacao publica.
                        .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()

                        // Vínculos de alunos: escrita.
                        .requestMatchers(HttpMethod.POST, "/turmas/*/alunos").hasAnyRole("ADMINISTRADOR", "ADMIN_INSTITUICAO", "COORDENADOR", "SECRETARIO")

                        .requestMatchers(HttpMethod.PUT, "/turmas/*/alunos/*").hasAnyRole("ADMINISTRADOR", "ADMIN_INSTITUICAO", "COORDENADOR", "SECRETARIO")

                        .requestMatchers(HttpMethod.DELETE, "/turmas/*/alunos/*").hasAnyRole("ADMINISTRADOR", "ADMIN_INSTITUICAO", "COORDENADOR", "SECRETARIO")

                        // Criar cursos e turmas.
                        .requestMatchers(HttpMethod.POST, "/cursos", "/turmas").hasAnyRole("ADMINISTRADOR", "ADMIN_INSTITUICAO", "COORDENADOR")

                        // Atualizar cursos e turmas.
                        .requestMatchers(HttpMethod.PUT, "/cursos/*", "/turmas/*").hasAnyRole("ADMINISTRADOR", "ADMIN_INSTITUICAO", "COORDENADOR")

                        // Excluir cursos e turmas.
                        .requestMatchers(HttpMethod.DELETE, "/cursos/*", "/turmas/*").hasAnyRole("ADMINISTRADOR", "ADMIN_INSTITUICAO", "COORDENADOR")

                        // Consulta de cursos: perfis da instituicao.
                        // O filtro por instituicao e feito no CursoService.
                        .requestMatchers(HttpMethod.GET, "/cursos", "/cursos/*").hasAnyRole("ADMINISTRADOR", "ADMIN_INSTITUICAO", "COORDENADOR", "PROFESSOR", "SECRETARIO", "ASSISTENTE")

                        // Consultas de turmas: qualquer usuario autenticado.
                        // Habilitar somente junto aos filtros
                        // por instituicao das etapas seguintes.
                        .requestMatchers(HttpMethod.GET, "/turmas", "/turmas/**").authenticated()

                        // Não permitir rotas desconhecidas.
                        .anyRequest().denyAll())

                .oauth2ResourceServer(oauth -> oauth.jwt(jwt -> jwt.jwtAuthenticationConverter(conversorJwt)));

        return http.build();
    }

    @Bean
    public JwtAuthenticationConverter conversorJwt() {

        JwtGrantedAuthoritiesConverter autoridades = new JwtGrantedAuthoritiesConverter();

        autoridades.setAuthoritiesClaimName("roles");

        autoridades.setAuthorityPrefix("");

        JwtAuthenticationConverter conversor = new JwtAuthenticationConverter();

        conversor.setJwtGrantedAuthoritiesConverter(autoridades);

        return conversor;
    }

    @Bean
    public JwtDecoder jwtDecoder(@Value("${jwt.secret}") String segredo) {

        byte[] chave = segredo.getBytes(StandardCharsets.UTF_8);

        if (chave.length < 32) {
            throw new IllegalStateException("A chave JWT deve ter pelo menos 32 bytes");
        }

        MacAlgorithm algoritmo;
        String nomeAlgoritmo;

        if (chave.length >= 64) {
            algoritmo = MacAlgorithm.HS512;
            nomeAlgoritmo = "HmacSHA512";

        } else if (chave.length >= 48) {
            algoritmo = MacAlgorithm.HS384;
            nomeAlgoritmo = "HmacSHA384";

        } else {
            algoritmo = MacAlgorithm.HS256;
            nomeAlgoritmo = "HmacSHA256";
        }

        SecretKeySpec chaveSecreta = new SecretKeySpec(chave, nomeAlgoritmo);

        return NimbusJwtDecoder.withSecretKey(chaveSecreta).macAlgorithm(algoritmo).build();
    }
}
