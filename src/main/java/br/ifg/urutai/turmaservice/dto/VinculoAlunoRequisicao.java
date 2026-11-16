
package br.ifg.urutai.turmaservice.dto;

import jakarta.validation.constraints.*;

public class VinculoAlunoRequisicao {

    @NotNull
    @Positive
    private Long alunoId;

    @NotBlank
    @Size(max = 30)
    private String situacao;

    public VinculoAlunoRequisicao() {
    }

    public VinculoAlunoRequisicao(
            Long alunoId,
            String situacao) {
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
