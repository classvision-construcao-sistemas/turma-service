
package br.ifg.urutai.turmaservice.dto;

import java.time.LocalDate;
import java.util.List;

public class TurmaResposta {

    private Long id;
    private Integer ano;
    private String nome;
    private String turno;
    private String sala;
    private LocalDate inicioAula;
    private LocalDate fimAula;
    private Long cursoId;
    private List<VinculoAlunoResposta> alunos;

    public TurmaResposta() {
    }

    public TurmaResposta(
            Long id,
            Integer ano,
            String nome,
            String turno,
            String sala,
            LocalDate inicioAula,
            LocalDate fimAula,
            Long cursoId,
            List<VinculoAlunoResposta> alunos) {
        this.id = id;
        this.ano = ano;
        this.nome = nome;
        this.turno = turno;
        this.sala = sala;
        this.inicioAula = inicioAula;
        this.fimAula = fimAula;
        this.cursoId = cursoId;
        this.alunos = alunos;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public List<VinculoAlunoResposta> getAlunos() {
        return alunos;
    }

    public void setAlunos(List<VinculoAlunoResposta> alunos) {
        this.alunos = alunos;
    }
}
