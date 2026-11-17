package br.ifg.urutai.turmaservice.dto;

import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados necessários para cadastrar um curso")
public class CursoRequisicao {

    @NotBlank
    @Size(max = 50)
    @Schema(description = "Código único de identificação do curso", example = "ADS-2027", requiredMode = Schema.RequiredMode.REQUIRED)
    private String codigo;

    @NotBlank
    @Size(max = 255)
    @Schema(description = "Nome completo do curso", example = "Tecnologia em Análise e Desenvolvimento de Sistemas", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nome;

    @NotBlank
    @Size(max = 30)
    @Schema(description = "Situação atual do curso", example = "ATIVO", requiredMode = Schema.RequiredMode.REQUIRED)
    private String situacao;

    @NotNull
    @Positive
    @Schema(description = "Identificador da instituição à qual o curso pertence. Não poderá ser alterado posteriormente.", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long instituicaoId;

    public CursoRequisicao() {
    }

    public @NotBlank @Size(max = 50) String getCodigo() {
        return codigo;
    }

    public void setCodigo(@NotBlank @Size(max = 50) String codigo) {
        this.codigo = codigo;
    }

    public @NotBlank @Size(max = 255) String getNome() {
        return nome;
    }

    public void setNome(@NotBlank @Size(max = 255) String nome) {
        this.nome = nome;
    }

    public @NotBlank @Size(max = 30) String getSituacao() {
        return situacao;
    }

    public void setSituacao(@NotBlank @Size(max = 30) String situacao) {
        this.situacao = situacao;
    }

    public @NotNull @Positive Long getInstituicaoId() {
        return instituicaoId;
    }

    public void setInstituicaoId(@NotNull @Positive Long instituicaoId) {
        this.instituicaoId = instituicaoId;
    }


    // Getters e setters dos quatro campos
}
