
package br.ifg.urutai.turmaservice.service;

import br.ifg.urutai.turmaservice.dto.*;
import br.ifg.urutai.turmaservice.model.*;
import br.ifg.urutai.turmaservice.repository.TurmaRepository;
import br.ifg.urutai.turmaservice.exception.*;
import br.ifg.urutai.turmaservice.util.DataMapper;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TurmaService {

    private final TurmaRepository turmaRepository;
    private final CursoService cursoService;

    public TurmaService(
            TurmaRepository turmaRepository,
            CursoService cursoService) {

        this.turmaRepository = turmaRepository;
        this.cursoService = cursoService;
    }

    @Transactional(readOnly = true)
    public List<TurmaResposta> listarTodas() {

        return turmaRepository
                .buscarTodasComRelacionamentos()
                .stream()
                .map(this::converter)
                .toList();
    }

    @Transactional(readOnly = true)
    public TurmaResposta buscarPorId(Long id) {

        return converter(obterTurma(id));
    }

    @Transactional(readOnly = true)
    public List<TurmaResposta> listarPorCurso(Long cursoId) {

        cursoService.obterCurso(cursoId);

        return turmaRepository
                .buscarPorCursoComRelacionamentos(cursoId)
                .stream()
                .map(this::converter)
                .toList();
    }

    @Transactional
    public TurmaResposta cadastrar(TurmaRequisicao dados) {

        validarDatas(dados);

        Turma turma = DataMapper.parseObject(
                dados,
                Turma.class
        );

        configurarCampos(turma, dados);

        turma = turmaRepository.save(turma);

        return converter(turma);
    }

    @Transactional
    public TurmaResposta atualizar(
            Long id,
            TurmaRequisicao dados) {

        validarDatas(dados);

        Turma turma = obterTurma(id);

        DataMapper.copiarPropriedades(
                dados,
                turma
        );

        configurarCampos(turma, dados);

        return converter(turma);
    }

    @Transactional
    public void excluir(Long id) {

        Turma turma = obterTurma(id);

        turmaRepository.delete(turma);
    }

    @Transactional
    public TurmaResposta vincularAluno(
            Long turmaId,
            VinculoAlunoRequisicao dados) {

        Turma turma = obterTurma(turmaId);

        verificarVinculo(
                turma,
                dados.getAlunoId()
        );

        VinculoAluno vinculo = DataMapper.parseObject(
                dados,
                VinculoAluno.class
        );

        vinculo.setSituacao(
                vinculo.getSituacao().trim()
        );

        turma.getAlunos().add(vinculo);

        return converter(turma);
    }

    @Transactional
    public TurmaResposta atualizarVinculo(
            Long turmaId,
            Long alunoId,
            VinculoAlunoRequisicao dados) {

        Turma turma = obterTurma(turmaId);

        if (!alunoId.equals(dados.getAlunoId())) {
            throw new RegraNegocioException(
                    "O identificador do aluno não pode ser alterado"
            );
        }

        VinculoAluno vinculo = obterVinculo(
                turma,
                alunoId
        );

        vinculo.setSituacao(
                dados.getSituacao().trim()
        );

        return converter(turma);
    }

    @Transactional
    public void desvincularAluno(
            Long turmaId,
            Long alunoId) {

        Turma turma = obterTurma(turmaId);

        VinculoAluno vinculo = obterVinculo(
                turma,
                alunoId
        );

        turma.getAlunos().remove(vinculo);
    }

    private void verificarVinculo(
            Turma turma,
            Long alunoId) {

        boolean jaVinculado = turma.getAlunos()
                .stream()
                .anyMatch(vinculo ->
                        vinculo.getAlunoId().equals(alunoId)
                );

        if (jaVinculado) {
            throw new RegraNegocioException(
                    "Aluno já vinculado à turma"
            );
        }
    }

    private VinculoAluno obterVinculo(
            Turma turma,
            Long alunoId) {

        return turma.getAlunos()
                .stream()
                .filter(vinculo ->
                        vinculo.getAlunoId().equals(alunoId)
                )
                .findFirst()
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException(
                                "Aluno não vinculado à turma: " + alunoId
                        )
                );
    }

    private Turma obterTurma(Long id) {

        return turmaRepository
                .buscarPorIdComRelacionamentos(id)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException(
                                "Turma não encontrada: " + id
                        )
                );
    }

    private void validarDatas(TurmaRequisicao dados) {

        if (dados.getInicioAula() != null
                && dados.getFimAula() != null
                && dados.getFimAula().isBefore(
                dados.getInicioAula())) {

            throw new RegraNegocioException(
                    "O fim das aulas não pode anteceder o início"
            );
        }
    }

    private void configurarCampos(
            Turma turma,
            TurmaRequisicao dados) {

        turma.setNome(
                turma.getNome().trim()
        );

        turma.setTurno(
                turma.getTurno().trim()
        );

        turma.setSala(dados.getSala());
        turma.setInicioAula(dados.getInicioAula());
        turma.setFimAula(dados.getFimAula());

        Curso curso = cursoService.obterCurso(
                dados.getCursoId()
        );

        turma.setCurso(curso);
    }

    private TurmaResposta converter(Turma turma) {

        TurmaResposta resposta = DataMapper.parseObject(
                turma,
                TurmaResposta.class
        );

        resposta.setCursoId(
                turma.getCurso().getId()
        );

        resposta.setAlunos(
                DataMapper.parseListObjects(
                        turma.getAlunos(),
                        VinculoAlunoResposta.class
                )
        );

        return resposta;
    }
}
