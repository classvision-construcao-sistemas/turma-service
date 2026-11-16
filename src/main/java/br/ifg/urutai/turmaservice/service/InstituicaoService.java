
package br.ifg.urutai.turmaservice.service;

import br.ifg.urutai.turmaservice.dto.InstituicaoRequisicao;
import br.ifg.urutai.turmaservice.dto.InstituicaoResposta;
import br.ifg.urutai.turmaservice.exception.RecursoNaoEncontradoException;
import br.ifg.urutai.turmaservice.exception.RegraNegocioException;
import br.ifg.urutai.turmaservice.model.Instituicao;
import br.ifg.urutai.turmaservice.repository.InstituicaoRepository;
import br.ifg.urutai.turmaservice.util.DataMapper;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class InstituicaoService {

    private final InstituicaoRepository instituicaoRepository;

    public InstituicaoService(
            InstituicaoRepository instituicaoRepository) {
        this.instituicaoRepository = instituicaoRepository;
    }

    @Transactional(readOnly = true)
    public List<InstituicaoResposta> listarTodos() {

        return DataMapper.parseListObjects(
                instituicaoRepository.findAll(),
                InstituicaoResposta.class
        );
    }

    @Transactional(readOnly = true)
    public InstituicaoResposta buscarPorId(Long id) {

        return DataMapper.parseObject(
                obter(id),
                InstituicaoResposta.class
        );
    }

    @Transactional
    public InstituicaoResposta cadastrar(
            InstituicaoRequisicao dados) {

        if (instituicaoRepository.existsByCnpj(
                dados.getCnpj())) {
            throw new RegraNegocioException(
                    "CNPJ já cadastrado"
            );
        }

        Instituicao instituicao = DataMapper.parseObject(
                dados,
                Instituicao.class
        );

        normalizarCampos(instituicao);
        instituicao.setCriadoEm(Instant.now());

        instituicao = instituicaoRepository.save(instituicao);

        return DataMapper.parseObject(
                instituicao,
                InstituicaoResposta.class
        );
    }

    @Transactional
    public InstituicaoResposta atualizar(
            Long id,
            InstituicaoRequisicao dados) {

        Instituicao instituicao = obter(id);

        if (instituicaoRepository.existsByCnpjAndIdNot(
                dados.getCnpj(), id)) {
            throw new RegraNegocioException(
                    "CNPJ já cadastrado"
            );
        }

        DataMapper.copiarPropriedades(
                dados,
                instituicao
        );

        normalizarCampos(instituicao);
        instituicao.setAtualizadoEm(Instant.now());

        return DataMapper.parseObject(
                instituicao,
                InstituicaoResposta.class
        );
    }

    @Transactional
    public void excluir(Long id) {

        Instituicao instituicao = obter(id);

        instituicaoRepository.delete(instituicao);
    }

    private Instituicao obter(Long id) {

        return instituicaoRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException(
                                "Instituição não encontrada: " + id
                        )
                );
    }

    private void normalizarCampos(
            Instituicao instituicao) {

        instituicao.setNome(
                instituicao.getNome().trim()
        );

        instituicao.setTelefone(
                instituicao.getTelefone().trim()
        );
    }
}
