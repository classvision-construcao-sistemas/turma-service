package br.ifg.urutai.turmaservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados para atualizar a situação acadêmica de um aluno vinculado à turma")
public class VinculoAlunoRequisicaoAtualizar {

    @NotBlank
    @Size(max = 30)
    @Schema(description = "Nova situação acadêmica do aluno na turma", example = "TRANCADO", requiredMode = Schema.RequiredMode.REQUIRED)
    private String situacao;

    public VinculoAlunoRequisicaoAtualizar() {
    }

    public VinculoAlunoRequisicaoAtualizar(String situacao) {
        this.situacao = situacao;
    }

    public @NotBlank @Size(max = 30) String getSituacao() {
        return situacao;
    }

    public void setSituacao(@NotBlank @Size(max = 30) String situacao) {
        this.situacao = situacao;
    }
}
