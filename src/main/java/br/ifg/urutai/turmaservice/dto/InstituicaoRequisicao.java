package br.ifg.urutai.turmaservice.dto;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;
@Schema(description="Dados para criar ou atualizar uma instituição")
public record InstituicaoRequisicao(
 @NotBlank @Size(max=255) String nome,
 @NotBlank
 @Pattern(regexp = "\\d{14}", message = "CNPJ deve ter 14 dígitos") String cnpj,
 @NotBlank @Size(max=20) String telefone,
 @NotBlank @Pattern(regexp="ATIVA|INATIVA",message="Situação deve ser ATIVA ou INATIVA") String situacao) {}
