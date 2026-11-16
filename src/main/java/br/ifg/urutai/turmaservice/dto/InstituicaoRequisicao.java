
package br.ifg.urutai.turmaservice.dto;

import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados para criar ou atualizar uma instituição")
public class InstituicaoRequisicao {

        @NotBlank
        @Size(max = 255)
        private String nome;

        @NotBlank
        @Pattern(
                regexp = "\\d{14}",
                message = "CNPJ deve ter 14 dígitos"
        )
        private String cnpj;

        @NotBlank
        @Size(max = 20)
        private String telefone;

        @NotBlank
        @Pattern(
                regexp = "ATIVA|INATIVA",
                message = "Situação deve ser ATIVA ou INATIVA"
        )
        private String situacao;

        public InstituicaoRequisicao() {
        }

        public InstituicaoRequisicao(
                String nome,
                String cnpj,
                String telefone,
                String situacao) {
                this.nome = nome;
                this.cnpj = cnpj;
                this.telefone = telefone;
                this.situacao = situacao;
        }

        public String getNome() {
                return nome;
        }

        public void setNome(String nome) {
                this.nome = nome;
        }

        public String getCnpj() {
                return cnpj;
        }

        public void setCnpj(String cnpj) {
                this.cnpj = cnpj;
        }

        public String getTelefone() {
                return telefone;
        }

        public void setTelefone(String telefone) {
                this.telefone = telefone;
        }

        public String getSituacao() {
                return situacao;
        }

        public void setSituacao(String situacao) {
                this.situacao = situacao;
        }
}
