package br.ifg.urutai.turmaservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Schema(description = "Atualização parcial de um curso. Informe somente os campos que deseja alterar.")
public class CursoRequisicaoPatch {

    @Size(max = 50)
    @Pattern(regexp = ".*\\S.*", message = "O código não pode estar em branco")
    @Schema(description = "Novo código do curso, caso deseje alterá-lo", example = "ADS-2027")
    private String codigo;

    @Size(max = 255)
    @Pattern(regexp = ".*\\S.*", message = "O nome não pode estar em branco")
    @Schema(description = "Novo nome do curso", example = "Tecnologia em Análise e Desenvolvimento de Sistemas")
    private String nome;

    @Size(max = 30)
    @Pattern(regexp = ".*\\S.*", message = "A situação não pode estar em branco")
    @Schema(description = "Nova situação do curso", example = "INATIVO")
    private String situacao;

    public CursoRequisicaoPatch() {
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
