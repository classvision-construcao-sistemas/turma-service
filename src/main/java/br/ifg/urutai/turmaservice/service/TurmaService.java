package br.ifg.urutai.turmaservice.service;

import br.ifg.urutai.turmaservice.dto.*;
import br.ifg.urutai.turmaservice.model.*;
import br.ifg.urutai.turmaservice.repository.TurmaRepository;
import br.ifg.urutai.turmaservice.exception.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class TurmaService {
    private final TurmaRepository turmaRespository;
    private final CursoService cursoService;

    public TurmaService(TurmaRepository turmaRespository, CursoService cursoService) {
        this.turmaRespository = turmaRespository;
        this.cursoService = cursoService;
    }

    @Transactional(readOnly = true)
    public List<TurmaResposta> listarTodas() {
        return turmaRespository.buscarTodasComRelacionamentos().stream().map(this::converter).toList();
    }

    @Transactional(readOnly = true)
    public TurmaResposta buscarPorId(Long id) {
        return converter(obterTurma(id));
    }

    @Transactional(readOnly = true)
    public List<TurmaResposta> listarPorCurso(Long cursoId) {
        cursoService.obterCurso(cursoId);
        return turmaRespository.buscarPorCursoComRelacionamentos(cursoId).stream().map(this::converter).toList();
    }

    @Transactional
    public TurmaResposta cadastrar(TurmaRequisicao dados) {
        validarDatas(dados);
        Turma turma = new Turma();
        aplicar(turma, dados);
        return converter(turmaRespository.save(turma));
    }

    @Transactional
    public TurmaResposta atualizar(Long id, TurmaRequisicao dados) {
        validarDatas(dados);
        Turma turma = obterTurma(id);
        aplicar(turma, dados);
        return converter(turma);
    }

    @Transactional
    public void excluir(Long id) {
        Turma turma = obterTurma(id);
        turmaRespository.delete(turma);
    }

    @Transactional
    public TurmaResposta vincularAluno(Long turmaId, VinculoAlunoRequisicao dados) {
        Turma turma = obterTurma(turmaId);
        verificarVinculo(turma, dados.alunoId());
        turma.getAlunos().add(new VinculoAluno(dados.alunoId(), dados.situacao().trim()));
        return converter(turma);
    }

    @Transactional
    public TurmaResposta atualizarVinculo(Long turmaId, Long alunoId, VinculoAlunoRequisicao dados) {
        Turma turma = obterTurma(turmaId);
        if (!alunoId.equals(dados.alunoId()))
            throw new RegraNegocioException("O identificador do aluno não pode ser alterado");
        VinculoAluno vinculo = obterVinculo(turma, alunoId);
        vinculo.setSituacao(dados.situacao().trim());
        return converter(turma);
    }

    @Transactional
    public void desvincularAluno(Long turmaId, Long alunoId) {
        Turma turma = obterTurma(turmaId);
        VinculoAluno vinculo = obterVinculo(turma, alunoId);
        turma.getAlunos().remove(vinculo);
    }

    private void verificarVinculo(Turma turma, Long alunoId) {
        if (turma.getAlunos().stream().anyMatch(v -> v.getAlunoId().equals(alunoId)))
            throw new RegraNegocioException("Aluno já vinculado à turma");
    }

    private VinculoAluno obterVinculo(Turma turma, Long alunoId) {
        return turma.getAlunos().stream().filter(v -> v.getAlunoId().equals(alunoId)).findFirst().orElseThrow(() -> new RecursoNaoEncontradoException("Aluno não vinculado à turma: " + alunoId));
    }

    private Turma obterTurma(Long id) {
        return turmaRespository.buscarPorIdComRelacionamentos(id).orElseThrow(() -> new RecursoNaoEncontradoException("Turma não encontrada: " + id));
    }

    private void validarDatas(TurmaRequisicao dados) {
        if (dados.inicioAula() != null && dados.fimAula() != null && dados.fimAula().isBefore(dados.inicioAula()))
            throw new RegraNegocioException("O fim das aulas não pode anteceder o início");
    }

    private void aplicar(Turma turma, TurmaRequisicao dados) {
        turma.setAno(dados.ano());
        turma.setNome(dados.nome().trim());
        turma.setTurno(dados.turno().trim());
        turma.setSala(dados.sala());
        turma.setInicioAula(dados.inicioAula());
        turma.setFimAula(dados.fimAula());
        turma.setCurso(cursoService.obterCurso(dados.cursoId()));
    }

    private TurmaResposta converter(Turma turma) {
        return new TurmaResposta(turma.getId(), turma.getAno(), turma.getNome(), turma.getTurno(), turma.getSala(), turma.getInicioAula(), turma.getFimAula(), turma.getCurso().getId(), turma.getAlunos().stream().map(v -> new VinculoAlunoResposta(v.getAlunoId(), v.getSituacao())).toList());
    }
}
