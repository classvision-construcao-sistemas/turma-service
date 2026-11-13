package br.ifg.urutai.turmaservice.dto;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
public record TurmaRequisicao(
 @NotNull @Min(1900) @Max(9999) Integer ano,
 @NotBlank @Size(max=255) String nome,
 @NotBlank @Size(max=30) String turno,
 @Size(max=50) String sala,
 LocalDate inicioAula, LocalDate fimAula,
 @NotNull @Positive Long cursoId) {}
