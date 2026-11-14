package br.ifg.urutai.turmaservice.config;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.*;
@Configuration
public class OpenApiConfiguracao {
 @Bean public OpenAPI configurarDocumentacao(){return new OpenAPI().info(new Info().title("Class Vision - API de Turmas").version("1.0.0").description("Gerenciamento de instituições, cursos, turmas e alunos vinculados."));}
}
