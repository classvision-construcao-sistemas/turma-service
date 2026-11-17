package br.ifg.urutai.turmaservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

@Schema(description = "Atualização parcial de uma turma. Informe somente os campos que deseja modificar.")
public class TurmaRequisicaoPatch {

    @Min(1900)
    @Max(9999)
    @Schema(description = "Novo ano letivo", example = "2027")
    private Integer ano;

    @Size(max = 255)
    @Pattern(regexp = ".*\\S.*", message = "O nome não pode estar em branco")
    @Schema(description = "Novo nome da turma", example = "ADS - 2º Período A")
    private String nome;

    @Size(max = 30)
    @Pattern(regexp = ".*\\S.*", message = "O turno não pode estar em branco")
    @Schema(description = "Novo turno da turma", example = "MATUTINO")
    private String turno;

    @Size(max = 50)
    @Schema(description = "Nova sala da turma", example = "Bloco C - Sala 05")
    private String sala;

    @Schema(description = "Nova data de início das aulas", type = "string", format = "date", example = "2027-02-08")
    private LocalDate inicioAula;

    @Schema(description = "Nova data de encerramento das aulas", type = "string", format = "date", example = "2027-06-30")
    private LocalDate fimAula;

    @Positive
    @Schema(description = "Novo curso da turma. Deve pertencer à mesma instituição do curso atual.", example = "2")
    private Long cursoId;

    public TurmaRequisicaoPatch() {
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
