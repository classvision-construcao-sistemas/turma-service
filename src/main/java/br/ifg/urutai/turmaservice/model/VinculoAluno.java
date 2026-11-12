package br.ifg.urutai.turmaservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class VinculoAluno {

    @Column(name = "aluno_id", nullable = false)
    private Long alunoId;

    @Column(name = "situacao", nullable = false, length = 30)
    private String situacao;

    public VinculoAluno() {
    }

    public VinculoAluno(Long alunoId, String situacao) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VinculoAluno that)) return false;

        return Objects.equals(alunoId, that.alunoId)
                && Objects.equals(situacao, that.situacao);
    }

    @Override
    public int hashCode() {
        return Objects.hash(alunoId, situacao);
    }
}