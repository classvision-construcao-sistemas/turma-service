package br.ifg.urutai.turmaservice.dto;
import jakarta.validation.constraints.*;
public record VinculoAlunoRequisicao(@NotNull @Positive Long alunoId,@NotBlank @Size(max=30) String situacao) {}
