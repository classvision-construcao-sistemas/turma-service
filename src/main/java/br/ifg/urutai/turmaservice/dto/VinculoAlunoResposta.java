
package br.ifg.urutai.turmaservice.dto;

public class VinculoAlunoResposta {

    private Long alunoId;
    private String situacao;

    public VinculoAlunoResposta() {
    }

    public VinculoAlunoResposta(
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
