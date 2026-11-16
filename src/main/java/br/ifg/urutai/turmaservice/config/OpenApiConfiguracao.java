
package br.ifg.urutai.turmaservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguracao {

 @Bean
 public OpenAPI configurarDocumentacao() {

  return new OpenAPI()
          .info(new Info()
                  .title("Class Vision - API de Turmas")
                  .version("1.0.0")
                  .description("Gerenciamento de cursos, turmas e alunos")
          )
          .components(new Components()
                  .addSecuritySchemes(
                          "bearerAuth",
                          new SecurityScheme()
                                  .type(SecurityScheme.Type.HTTP)
                                  .scheme("bearer")
                                  .bearerFormat("JWT")
                  )
          )
          .addSecurityItem(
                  new SecurityRequirement()
                          .addList("bearerAuth")
          );
 }
}
