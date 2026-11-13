package br.ifg.urutai.turmaservice.service;

import br.ifg.urutai.turmaservice.dto.*;
import br.ifg.urutai.turmaservice.model.Instituicao;
import br.ifg.urutai.turmaservice.repository.InstituicaoRepository;
import br.ifg.urutai.turmaservice.exception.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class InstituicaoService {
    private final InstituicaoRepository instituicaoRepository;

    public InstituicaoService(InstituicaoRepository instituicaoRepository) {
        this.instituicaoRepository = instituicaoRepository;
    }

    @Transactional(readOnly = true)
    public List<InstituicaoResposta> listarTodos() {
        return instituicaoRepository.findAll().stream().map(this::converter).toList();
    }

    @Transactional(readOnly = true)
    public InstituicaoResposta buscarPorId(Long id) {
        return converter(obter(id));
    }

    @Transactional
    public InstituicaoResposta cadastrar(InstituicaoRequisicao dados) {
        if (instituicaoRepository.existsByCnpj(dados.cnpj())) throw new RegraNegocioException("CNPJ já cadastrado");
        Instituicao instituicao = new Instituicao();
        aplicar(instituicao, dados);
        instituicao.setCriadoEm(Instant.now());
        return converter(instituicaoRepository.save(instituicao));
    }

    @Transactional
    public InstituicaoResposta atualizar(Long id, InstituicaoRequisicao dados) {
        Instituicao instituicao = obter(id);
        if (instituicaoRepository.existsByCnpjAndIdNot(dados.cnpj(), id)) throw new RegraNegocioException("CNPJ já cadastrado");
        aplicar(instituicao, dados);
        instituicao.setAtualizadoEm(Instant.now());
        return converter(instituicao);
    }

    @Transactional
    public void excluir(Long id) {
        Instituicao instituicao = obter(id);
        instituicaoRepository.delete(instituicao);
    }

    private Instituicao obter(Long id) {
        return instituicaoRepository.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Instituição não encontrada: " + id));
    }

    private void aplicar(Instituicao instituicao, InstituicaoRequisicao dados) {
        instituicao.setNome(dados.nome().trim());
        instituicao.setCnpj(dados.cnpj());
        instituicao.setTelefone(dados.telefone().trim());
        instituicao.setSituacao(dados.situacao());
    }

    private InstituicaoResposta converter(Instituicao instituicao) {
        return new InstituicaoResposta(instituicao.getId(), instituicao.getNome(), instituicao.getCnpj(), instituicao.getTelefone(), instituicao.getSituacao(), instituicao.getCriadoEm(), instituicao.getAtualizadoEm());
    }
}
