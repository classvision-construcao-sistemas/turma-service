package br.ifg.urutai.turmaservice.repository;

import br.ifg.urutai.turmaservice.model.Turma;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.*;

public interface TurmaRepository extends JpaRepository<Turma, Long> {
    @Query("select distinct turma from Turma turma join fetch turma.curso left join fetch turma.alunos")
    List<Turma> buscarTodasComRelacionamentos();

    @Query("select distinct turma from Turma turma join fetch turma.curso left join fetch turma.alunos where turma.id=:id")
    Optional<Turma> buscarPorIdComRelacionamentos(@Param("id") Long id);

    @Query("select distinct turma from Turma turma join fetch turma.curso left join fetch turma.alunos where turma.curso.id=:cursoId")
    List<Turma> buscarPorCursoComRelacionamentos(@Param("cursoId") Long cursoId);

    boolean existsByCurso_Id(Long cursoId);
}
