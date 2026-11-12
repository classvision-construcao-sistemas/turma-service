package br.ifg.urutai.turmaservice.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cv05_turmas")
public class Turma {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "ano", columnDefinition = "smallint UNSIGNED not null")
    private Integer ano;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "turno", nullable = false, length = 30)
    private String turno;

    @Column(name = "sala", length = 50)
    private String sala;

    @Column(name = "inicio_aula")
    private LocalDate inicioAula;

    @Column(name = "fim_aula")
    private LocalDate fimAula;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "bigint_curso_id", nullable = false)
    private Curso bigintCurso;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "cv09_turmas_alunos",
            joinColumns = @JoinColumn(name = "bigint_turma_id")
    )
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

    public Curso getBigintCurso() {
        return bigintCurso;
    }

    public void setBigintCurso(Curso bigintCurso) {
        this.bigintCurso = bigintCurso;
    }

    public List<VinculoAluno> getAlunos() {
        return alunos;
    }

    public void setAlunos(List<VinculoAluno> alunos) {
        this.alunos = alunos;
    }
}