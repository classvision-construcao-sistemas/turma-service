
package br.ifg.urutai.turmaservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Schema(description = "Dados para atualizar parcialmente uma turma")
public class TurmaRequisicaoPatch {

    @Min(1900)
    @Max(9999)
    private Integer ano;

    @Size(max = 255)
    @Pattern(regexp = ".*\\S.*",
            message = "O nome não pode estar em branco")
    private String nome;

    @Size(max = 30)
    @Pattern(regexp = ".*\\S.*",
            message = "O turno não pode estar em branco")
    private String turno;

    @Size(max = 50)
    private String sala;

    private LocalDate inicioAula;

    private LocalDate fimAula;

    @Positive
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
