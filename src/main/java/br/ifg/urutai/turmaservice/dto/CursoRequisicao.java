package br.ifg.urutai.turmaservice.dto;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;
@Schema(description="Dados para criar ou atualizar um curso")
public record CursoRequisicao(
 @NotBlank @Size(max=50) String codigo,
 @NotBlank @Size(max=255) String nome,
 @NotBlank @Size(max=30) String situacao) {}
