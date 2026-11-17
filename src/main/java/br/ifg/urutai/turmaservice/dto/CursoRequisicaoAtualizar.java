
package br.ifg.urutai.turmaservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "Dados para atualizar um curso sem alterar sua instituição")
public class CursoRequisicaoAtualizar {

    @NotBlank
    @Size(max = 50)
    @Schema(
            description = "Código do curso",
            example = "ADS-2027",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String codigo;

    @NotBlank
    @Size(max = 255)
    @Schema(
            description = "Nome atualizado do curso",
            example = "Tecnologia em Análise e Desenvolvimento de Sistemas",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String nome;

    @NotBlank
    @Size(max = 30)
    @Schema(
            description = "Situação atualizada do curso",
            example = "ATIVO",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
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
