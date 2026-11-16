package br.ifg.urutai.turmaservice.repository;

import br.ifg.urutai.turmaservice.model.Curso;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CursoRepository extends JpaRepository<Curso, Long> {

    List<Curso> findAllByInstituicao_Id(Long instituicaoId);
    boolean existsByCodigoIgnoreCase(String codigo);
    boolean existsByCodigoIgnoreCaseAndIdNot(String codigo, Long id);
}
