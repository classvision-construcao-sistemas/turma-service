package br.ifg.urutai.turmaservice.service;

import br.ifg.urutai.turmaservice.dto.*;
import br.ifg.urutai.turmaservice.model.Curso;
import br.ifg.urutai.turmaservice.model.Instituicao;
import br.ifg.urutai.turmaservice.repository.*;
import br.ifg.urutai.turmaservice.exception.*;
import br.ifg.urutai.turmaservice.security.ControleAcessoInstituicao;
import br.ifg.urutai.turmaservice.util.DataMapper;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CursoService {

    private final CursoRepository cursoRepository;
    private final TurmaRepository turmaRepository;
    private final InstituicaoRepository instituicaoRepository;
    private final ControleAcessoInstituicao controleAcesso;

    public CursoService(CursoRepository cursoRepository, TurmaRepository turmaRepository, InstituicaoRepository instituicaoRepository, ControleAcessoInstituicao controleAcesso) {

        this.cursoRepository = cursoRepository;
        this.turmaRepository = turmaRepository;
        this.instituicaoRepository = instituicaoRepository;
        this.controleAcesso = controleAcesso;
    }

    @Transactional(readOnly = true)
    public List<CursoResposta> listarTodos() {

        List<Curso> cursos = controleAcesso.administradorGeral() ? cursoRepository.findAll() : cursoRepository.findAllByInstituicao_Id(controleAcesso.instituicaoId());

        return cursos.stream().map(this::converter).toList();
    }

    @Transactional(readOnly = true)
    public CursoResposta buscarPorId(Long id) {
        return converter(obterCursoAutorizado(id));
    }

    @Transactional
    public CursoResposta cadastrar(CursoRequisicao dados) {

        controleAcesso.validarInstituicao(dados.getInstituicaoId());

        String codigo = dados.getCodigo().trim();

        if (cursoRepository.existsByCodigoIgnoreCase(codigo)) {
            throw new RegraNegocioException("Código de curso já cadastrado");
        }

        Instituicao instituicao = obterInstituicao(dados.getInstituicaoId());

        Curso curso = new Curso();

        curso.setCodigo(codigo);
        curso.setNome(dados.getNome().trim());
        curso.setSituacao(dados.getSituacao().trim());

        curso.setInstituicao(instituicao);

        return converter(cursoRepository.save(curso));
    }


    @Transactional
    public CursoResposta atualizar(
            Long id,
            CursoRequisicaoAtualizar dados) {

        Curso curso = obterCursoAutorizado(id);

        String codigo = dados.getCodigo().trim();

        if (cursoRepository.existsByCodigoIgnoreCaseAndIdNot(
                codigo, id)) {

            throw new RegraNegocioException(
                    "Código de curso já cadastrado"
            );
        }

        curso.setCodigo(codigo);
        curso.setNome(dados.getNome().trim());
        curso.setSituacao(dados.getSituacao().trim());

        return converter(curso);
    }



    @Transactional
    public CursoResposta atualizarParcialmente(Long id, CursoRequisicaoPatch dados) {

        Curso curso = obterCursoAutorizado(id);

        if (dados.getCodigo() != null) {

            String codigo = dados.getCodigo().trim();

            if (cursoRepository.existsByCodigoIgnoreCaseAndIdNot(codigo, id)) {
                throw new RegraNegocioException("Código de curso já cadastrado");
            }

            curso.setCodigo(codigo);
        }

        if (dados.getNome() != null) {
            curso.setNome(dados.getNome().trim());
        }

        if (dados.getSituacao() != null) {
            curso.setSituacao(dados.getSituacao().trim());
        }

        return converter(curso);
    }


    @Transactional
    public void excluir(Long id) {

        Curso curso = obterCursoAutorizado(id);

        if (turmaRepository.existsByCurso_Id(id)) {
            throw new RegraNegocioException("Não é possível excluir um curso que possui turmas");
        }

        cursoRepository.delete(curso);
    }

    @Transactional(readOnly = true)
    public Curso obterCurso(Long id) {

        return cursoRepository.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Curso não encontrado: " + id));
    }

    private Curso obterCursoAutorizado(Long id) {

        Curso curso = obterCurso(id);

        controleAcesso.validarInstituicao(curso.getInstituicao().getId());

        return curso;
    }

    private Instituicao obterInstituicao(Long id) {

        return instituicaoRepository.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Instituição não encontrada: " + id));
    }

    private CursoResposta converter(Curso curso) {

        CursoResposta resposta = DataMapper.parseObject(curso, CursoResposta.class);

        resposta.setInstituicaoId(curso.getInstituicao().getId());

        return resposta;
    }


}
