
package br.ifg.urutai.turmaservice.dto;

import jakarta.validation.constraints.*;

public class CursoRequisicaoAtualizar {

    @NotBlank
    @Size(max = 50)
    private String codigo;

    @NotBlank
    @Size(max = 255)
    private String nome;

    @NotBlank
    @Size(max = 30)
    private String situacao;

    public CursoRequisicaoAtualizar() {
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }
}
