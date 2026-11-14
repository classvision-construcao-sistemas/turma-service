package br.ifg.urutai.turmaservice.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "cv05_turmas")
public class Turma {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "ano", columnDefinition = "smallint UNSIGNED not null")
    private Integer ano;
    @Column(nullable = false)
    private String nome;
    @Column(nullable = false, length = 30)
    private String turno;
    @Column(length = 50)
    private String sala;
    @Column(name = "inicio_aula")
    private LocalDate inicioAula;
    @Column(name = "fim_aula")
    private LocalDate fimAula;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "bigint_curso_id", nullable = false)
    private Curso curso;
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "cv09_turmas_alunos", joinColumns = @JoinColumn(name = "bigint_turma_id"))
    private List<VinculoAluno> alunos = new ArrayList<>();

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

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public List<VinculoAluno> getAlunos() {
        return alunos;
    }

    public void setAlunos(List<VinculoAluno> alunos) {
        this.alunos = alunos;
    }
}
