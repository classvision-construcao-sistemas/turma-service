
package br.ifg.urutai.turmaservice.service;

import br.ifg.urutai.turmaservice.dto.*;
import br.ifg.urutai.turmaservice.model.*;
import br.ifg.urutai.turmaservice.repository.TurmaRepository;
import br.ifg.urutai.turmaservice.exception.*;
import br.ifg.urutai.turmaservice.security.ControleAcessoInstituicao;
import br.ifg.urutai.turmaservice.util.DataMapper;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TurmaService {

    private static final String GERENCIAR_TURMAS =
            "hasAnyRole('ADMINISTRADOR', 'ADMIN_INSTITUICAO', 'COORDENADOR')";

    private static final String GERENCIAR_VINCULOS =
            "hasAnyRole('ADMINISTRADOR', 'ADMIN_INSTITUICAO', 'COORDENADOR', 'SECRETARIO')";

    private final TurmaRepository turmaRepository;
    private final CursoService cursoService;
    private final ControleAcessoInstituicao controleAcesso;

    public TurmaService(
            TurmaRepository turmaRepository,
            CursoService cursoService,
            ControleAcessoInstituicao controleAcesso) {

        this.turmaRepository = turmaRepository;
        this.cursoService = cursoService;
        this.controleAcesso = controleAcesso;
    }

    @Transactional(readOnly = true)
    public List<TurmaResposta> listarTodas() {

        List<Turma> turmas = controleAcesso.administradorGeral()
                ? turmaRepository.buscarTodasComRelacionamentos()
                : turmaRepository.buscarPorInstituicaoComRelacionamentos(
                        controleAcesso.instituicaoId()
                );

        return turmas
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

        obterCursoAutorizado(cursoId);

        return turmaRepository
                .buscarPorCursoComRelacionamentos(cursoId)
                .stream()
                .map(this::converter)
                .toList();
    }

    @Transactional
    @PreAuthorize(GERENCIAR_TURMAS)
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
    @PreAuthorize(GERENCIAR_TURMAS)
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
    @PreAuthorize(GERENCIAR_TURMAS)
    public void excluir(Long id) {

        Turma turma = obterTurma(id);

        turmaRepository.delete(turma);
    }

    @Transactional
    @PreAuthorize(GERENCIAR_VINCULOS)
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
    @PreAuthorize(GERENCIAR_VINCULOS)
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
    @PreAuthorize(GERENCIAR_VINCULOS)
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

        Turma turma = turmaRepository
                .buscarPorIdComRelacionamentos(id)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException(
                                "Turma não encontrada: " + id
                        )
                );

        validarInstituicao(turma.getCurso());

        return turma;
    }

    private Curso obterCursoAutorizado(Long cursoId) {

        Curso curso = cursoService.obterCurso(cursoId);

        validarInstituicao(curso);

        return curso;
    }

    private void validarInstituicao(Curso curso) {

        Long instituicaoId = curso.getInstituicao() != null
                ? curso.getInstituicao().getId()
                : null;

        controleAcesso.validarInstituicao(instituicaoId);
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

        Curso curso = obterCursoAutorizado(
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
