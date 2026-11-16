
package br.ifg.urutai.turmaservice.service;

import br.ifg.urutai.turmaservice.dto.*;
import br.ifg.urutai.turmaservice.model.Curso;
import br.ifg.urutai.turmaservice.model.Instituicao;
import br.ifg.urutai.turmaservice.repository.*;
import br.ifg.urutai.turmaservice.exception.*;
import br.ifg.urutai.turmaservice.util.DataMapper;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CursoService {

    private final CursoRepository cursoRepository;
    private final TurmaRepository turmaRepository;
    private final InstituicaoRepository instituicaoRepository;

    public CursoService(
            CursoRepository cursoRepository,
            TurmaRepository turmaRepository,
            InstituicaoRepository instituicaoRepository) {

        this.cursoRepository = cursoRepository;
        this.turmaRepository = turmaRepository;
        this.instituicaoRepository = instituicaoRepository;
    }

    @Transactional(readOnly = true)
    public List<CursoResposta> listarTodos() {
        return cursoRepository.findAll()
                .stream()
                .map(this::converter)
                .toList();
    }

    @Transactional(readOnly = true)
    public CursoResposta buscarPorId(Long id) {
        return converter(obterCurso(id));
    }

    @Transactional
    public CursoResposta cadastrar(CursoRequisicao dados) {

        String codigo = dados.getCodigo().trim();

        if (cursoRepository.existsByCodigoIgnoreCase(codigo)) {
            throw new RegraNegocioException(
                    "Código de curso já cadastrado"
            );
        }

        Instituicao instituicao = obterInstituicao(
                dados.getInstituicaoId()
        );

        Curso curso = DataMapper.parseObject(
                dados, Curso.class
        );

        normalizarCampos(curso);
        curso.setInstituicao(instituicao);

        return converter(cursoRepository.save(curso));
    }

    @Transactional
    public CursoResposta atualizar(
            Long id,
            CursoRequisicao dados) {

        Curso curso = obterCurso(id);

        String codigo = dados.getCodigo().trim();

        if (cursoRepository.existsByCodigoIgnoreCaseAndIdNot(
                codigo, id)) {
            throw new RegraNegocioException(
                    "Código de curso já cadastrado"
            );
        }

        Instituicao instituicao = obterInstituicao(
                dados.getInstituicaoId()
        );

        DataMapper.copiarPropriedades(dados, curso);

        normalizarCampos(curso);
        curso.setInstituicao(instituicao);

        return converter(curso);
    }

    @Transactional
    public void excluir(Long id) {

        Curso curso = obterCurso(id);

        if (turmaRepository.existsByCurso_Id(id)) {
            throw new RegraNegocioException(
                    "Não é possível excluir um curso que possui turmas"
            );
        }

        cursoRepository.delete(curso);
    }

    @Transactional(readOnly = true)
    public Curso obterCurso(Long id) {
        return cursoRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException(
                                "Curso não encontrado: " + id
                        )
                );
    }

    private Instituicao obterInstituicao(Long id) {
        return instituicaoRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException(
                                "Instituição não encontrada: " + id
                        )
                );
    }

    private void normalizarCampos(Curso curso) {
        curso.setCodigo(curso.getCodigo().trim());
        curso.setNome(curso.getNome().trim());
        curso.setSituacao(curso.getSituacao().trim());
    }

    private CursoResposta converter(Curso curso) {

        CursoResposta resposta = DataMapper.parseObject(
                curso, CursoResposta.class
        );

        resposta.setInstituicaoId(
                curso.getInstituicao().getId()
        );

        return resposta;
    }
}
