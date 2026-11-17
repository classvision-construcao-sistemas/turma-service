package br.ifg.urutai.turmaservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "Dados necessários para vincular um aluno a uma turma")
public class VinculoAlunoRequisicao {

    @NotNull
    @Positive
    @Schema(description = "Identificador numérico do aluno", example = "101", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long alunoId;

    @NotBlank
    @Size(max = 30)
    @Schema(description = "Situação acadêmica do aluno na turma", example = "MATRICULADO", requiredMode = Schema.RequiredMode.REQUIRED)
    private String situacao;

    public VinculoAlunoRequisicao() {
    }

    public VinculoAlunoRequisicao(Long alunoId, String situacao) {
        this.alunoId = alunoId;
        this.situacao = situacao;
    }

    public Long getAlunoId() {
        return alunoId;
    }

    public void setAlunoId(Long alunoId) {
        this.alunoId = alunoId;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }
}
