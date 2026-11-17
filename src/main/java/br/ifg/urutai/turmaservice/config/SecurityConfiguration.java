
package br.ifg.urutai.turmaservice.config;

import br.ifg.urutai.turmaservice.security.RespostaErroSeguranca;
import jakarta.servlet.DispatcherType;
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
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JwtAuthenticationConverter conversorJwt,
            RespostaErroSeguranca respostaErro
    ) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                .authorizeHttpRequests(auth -> auth

                        // Permite o tratamento interno de erros.
                        .dispatcherTypeMatchers(
                                DispatcherType.ERROR
                        ).permitAll()

                        // Documentação Swagger.
                        .requestMatchers(
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        ).permitAll()

                        // Gerenciamento de vínculos de alunos.
                        .requestMatchers(
                                HttpMethod.POST,
                                "/turmas/*/alunos"
                        ).hasAnyRole(
                                "ADMINISTRADOR",
                                "ADMIN_INSTITUICAO",
                                "COORDENADOR",
                                "SECRETARIO"
                        )

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/turmas/*/alunos/*"
                        ).hasAnyRole(
                                "ADMINISTRADOR",
                                "ADMIN_INSTITUICAO",
                                "COORDENADOR",
                                "SECRETARIO"
                        )

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/turmas/*/alunos/*"
                        ).hasAnyRole(
                                "ADMINISTRADOR",
                                "ADMIN_INSTITUICAO",
                                "COORDENADOR",
                                "SECRETARIO"
                        )

                        // Cadastro de cursos e turmas.
                        .requestMatchers(
                                HttpMethod.POST,
                                "/cursos",
                                "/turmas"
                        ).hasAnyRole(
                                "ADMINISTRADOR",
                                "ADMIN_INSTITUICAO",
                                "COORDENADOR"
                        )

                        // Atualização de cursos e turmas.
                        .requestMatchers(
                                HttpMethod.PUT,
                                "/cursos/*",
                                "/turmas/*"
                        ).hasAnyRole(
                                "ADMINISTRADOR",
                                "ADMIN_INSTITUICAO",
                                "COORDENADOR"
                        )

                        // Atualização parcial de cursos e turmas.
                        .requestMatchers(
                                HttpMethod.PATCH,
                                "/cursos/*",
                                "/turmas/*"
                        ).hasAnyRole(
                                "ADMINISTRADOR",
                                "ADMIN_INSTITUICAO",
                                "COORDENADOR"
                        )

                        // Exclusão de cursos e turmas.
                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/cursos/*",
                                "/turmas/*"
                        ).hasAnyRole(
                                "ADMINISTRADOR",
                                "ADMIN_INSTITUICAO",
                                "COORDENADOR"
                        )

                        // Consulta de cursos.
                        .requestMatchers(
                                HttpMethod.GET,
                                "/cursos",
                                "/cursos/*"
                        ).hasAnyRole(
                                "ADMINISTRADOR",
                                "ADMIN_INSTITUICAO",
                                "COORDENADOR",
                                "PROFESSOR",
                                "SECRETARIO",
                                "ASSISTENTE"
                        )

                        // Consulta de turmas.
                        .requestMatchers(
                                HttpMethod.GET,
                                "/turmas",
                                "/turmas/**"
                        ).authenticated()

                        // Bloqueia endpoints desconhecidos.
                        .anyRequest().denyAll()
                )

                // Tratamento de erros 401 e 403.
                .exceptionHandling(excecoes -> excecoes
                        .authenticationEntryPoint(respostaErro)
                        .accessDeniedHandler(respostaErro)
                )

                // Validação do JWT.
                .oauth2ResourceServer(oauth -> oauth
                        .jwt(jwt -> jwt
                                .jwtAuthenticationConverter(conversorJwt)
                        )
                        .authenticationEntryPoint(respostaErro)
                        .accessDeniedHandler(respostaErro)
                );

        return http.build();
    }

    @Bean
    public JwtAuthenticationConverter conversorJwt() {

        JwtGrantedAuthoritiesConverter autoridades =
                new JwtGrantedAuthoritiesConverter();

        autoridades.setAuthoritiesClaimName("roles");
        autoridades.setAuthorityPrefix("");

        JwtAuthenticationConverter conversor =
                new JwtAuthenticationConverter();

        conversor.setJwtGrantedAuthoritiesConverter(
                autoridades
        );

        return conversor;
    }

    @Bean
    public JwtDecoder jwtDecoder(
            @Value("${jwt.secret}") String segredo
    ) {

        byte[] chave = segredo.getBytes(
                StandardCharsets.UTF_8
        );

        if (chave.length < 32) {
            throw new IllegalStateException(
                    "A chave JWT deve ter pelo menos 32 bytes"
            );
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

        SecretKeySpec chaveSecreta =
                new SecretKeySpec(chave, nomeAlgoritmo);

        return NimbusJwtDecoder
                .withSecretKey(chaveSecreta)
                .macAlgorithm(algoritmo)
                .build();
    }
}
