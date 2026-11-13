package br.ifg.urutai.turmaservice.dto;
import java.time.LocalDate;
import java.util.List;
public record TurmaResposta(Long id,Integer ano,String nome,String turno,String sala,LocalDate inicioAula,LocalDate fimAula,Long cursoId,List<VinculoAlunoResposta> alunos) {}
