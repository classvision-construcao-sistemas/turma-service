package br.ifg.urutai.turmaservice.model;

import jakarta.persistence.*;

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
    public boolean equals(Object outro) {
        if (this == outro) return true;
        if (!(outro instanceof VinculoAluno vinculo)) return false;
        return Objects.equals(alunoId, vinculo.alunoId) && Objects.equals(situacao, vinculo.situacao);
    }

    @Override
    public int hashCode() {
        return Objects.hash(alunoId, situacao);
    }
}
