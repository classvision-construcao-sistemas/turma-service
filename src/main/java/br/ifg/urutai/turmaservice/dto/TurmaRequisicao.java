package br.ifg.urutai.turmaservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

@Schema(description = "Dados para cadastrar ou atualizar uma turma")
public class TurmaRequisicao {

    @NotNull
    @Min(1900)
    @Max(9999)
    @Schema(description = "Ano letivo da turma", example = "2027", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer ano;

    @NotBlank
    @Size(max = 255)
    @Schema(description = "Nome de identificação da turma", example = "ADS - 1º Período A", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nome;

    @NotBlank
    @Size(max = 30)
    @Schema(description = "Turno em que a turma possui aulas", example = "NOTURNO", requiredMode = Schema.RequiredMode.REQUIRED)
    private String turno;

    @Size(max = 50)
    @Schema(description = "Sala onde a turma possui aulas", example = "Bloco B - Sala 12")
    private String sala;

    @Schema(description = "Data de início das aulas, no formato AAAA-MM-DD", type = "string", format = "date", example = "2027-02-08")
    private LocalDate inicioAula;

    @Schema(description = "Data de encerramento das aulas, no formato AAAA-MM-DD", type = "string", format = "date", example = "2027-06-30")
    private LocalDate fimAula;

    @NotNull
    @Positive
    @Schema(description = "Identificador do curso ao qual a turma pertence. A transferência só é permitida entre cursos da mesma instituição.", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long cursoId;

    public TurmaRequisicao() {
    }

    public TurmaRequisicao(Integer ano, String nome, String turno, String sala, LocalDate inicioAula, LocalDate fimAula, Long cursoId) {
        this.ano = ano;
        this.nome = nome;
        this.turno = turno;
        this.sala = sala;
        this.inicioAula = inicioAula;
        this.fimAula = fimAula;
        this.cursoId = cursoId;
    }

    public Integer getAno() {
        return ano;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public String getSala() {
        return sala;
    }

    public void setSala(String sala) {
        this.sala = sala;
    }

    public LocalDate getInicioAula() {
        return inicioAula;
    }

    public void setInicioAula(LocalDate inicioAula) {
        this.inicioAula = inicioAula;
    }

    public LocalDate getFimAula() {
        return fimAula;
    }

    public void setFimAula(LocalDate fimAula) {
        this.fimAula = fimAula;
    }

    public Long getCursoId() {
        return cursoId;
    }

    public void setCursoId(Long cursoId) {
        this.cursoId = cursoId;
    }
}
