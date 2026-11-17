package br.ifg.urutai.turmaservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class VinculoAlunoRequisicaoAtualizar {

    @NotBlank
    @Size(max = 30)
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
