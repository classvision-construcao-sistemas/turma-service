
package br.ifg.urutai.turmaservice.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public class TurmaRequisicao {

    @NotNull
    @Min(1900)
    @Max(9999)
    private Integer ano;

    @NotBlank
    @Size(max = 255)
    private String nome;

    @NotBlank
    @Size(max = 30)
    private String turno;

    @Size(max = 50)
    private String sala;

    private LocalDate inicioAula;
    private LocalDate fimAula;

    @NotNull
    @Positive
    private Long cursoId;

    public TurmaRequisicao() {
    }

    public TurmaRequisicao(
            Integer ano,
            String nome,
            String turno,
            String sala,
            LocalDate inicioAula,
            LocalDate fimAula,
            Long cursoId) {
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
