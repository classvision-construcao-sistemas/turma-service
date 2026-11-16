package br.ifg.urutai.turmaservice.dto;

import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados para criar ou atualizar um curso")
public class CursoRequisicao {

        @NotBlank
        @Size(max = 50)
        private String codigo;

        @NotBlank
        @Size(max = 255)
        private String nome;

        @NotBlank
        @Size(max = 30)
        private String situacao;

        @NotNull
        @Positive
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
