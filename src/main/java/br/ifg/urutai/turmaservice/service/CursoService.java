package br.ifg.urutai.turmaservice.service;

import br.ifg.urutai.turmaservice.dto.*;
import br.ifg.urutai.turmaservice.model.Curso;
import br.ifg.urutai.turmaservice.repository.*;
import br.ifg.urutai.turmaservice.exception.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CursoService {
    private final CursoRepository cursoRepository;
    private final TurmaRepository turmaRepository;

    public CursoService(CursoRepository cursoRepository, TurmaRepository turmaRepository) {
        this.cursoRepository = cursoRepository;
        this.turmaRepository = turmaRepository;
    }

    @Transactional(readOnly = true)
    public List<CursoResposta> listarTodos() {
        return cursoRepository.findAll().stream().map(this::converter).toList();
    }

    @Transactional(readOnly = true)
    public CursoResposta buscarPorId(Long id) {
        return converter(obterCurso(id));
    }

    @Transactional
    public CursoResposta cadastrar(CursoRequisicao dados) {
        if (cursoRepository.existsByCodigoIgnoreCase(dados.codigo()))
            throw new RegraNegocioException("Código de curso já cadastrado");
        Curso curso = new Curso();
        aplicar(curso, dados);
        return converter(cursoRepository.save(curso));
    }

    @Transactional
    public CursoResposta atualizar(Long id, CursoRequisicao dados) {
        Curso curso = obterCurso(id);
        if (cursoRepository.existsByCodigoIgnoreCaseAndIdNot(dados.codigo(), id))
            throw new RegraNegocioException("Código de curso já cadastrado");
        aplicar(curso, dados);
        return converter(curso);
    }

    @Transactional
    public void excluir(Long id) {
        obterCurso(id);
        if (turmaRepository.existsByCurso_Id(id))
            throw new RegraNegocioException("Não é possível excluir um curso que possui turmaRepository");
        cursoRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Curso obterCurso(Long id) {
        return cursoRepository.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Curso não encontrado: " + id));
    }

    private void aplicar(Curso curso, CursoRequisicao dados) {
        curso.setCodigo(dados.codigo().trim());
        curso.setNome(dados.nome().trim());
        curso.setSituacao(dados.situacao().trim());
    }

    private CursoResposta converter(Curso curso) {
        return new CursoResposta(curso.getId(), curso.getCodigo(), curso.getNome(), curso.getSituacao());
    }
}
