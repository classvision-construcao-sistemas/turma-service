package br.ifg.urutai.turmaservice.service;

import br.ifg.urutai.turmaservice.dto.*;
import br.ifg.urutai.turmaservice.exception.RegraNegocioException;
import br.ifg.urutai.turmaservice.model.Curso;
import br.ifg.urutai.turmaservice.model.Instituicao;
import br.ifg.urutai.turmaservice.repository.CursoRepository;
import br.ifg.urutai.turmaservice.repository.InstituicaoRepository;
import br.ifg.urutai.turmaservice.repository.TurmaRepository;
import br.ifg.urutai.turmaservice.security.ControleAcessoInstituicao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CursoServiceAutorizacaoTest {

    private CursoRepository cursoRepository;
    private TurmaRepository turmaRepository;
    private InstituicaoRepository instituicaoRepository;
    private CursoService cursoService;

    @BeforeEach
    void configurar() {

        cursoRepository = mock(CursoRepository.class);
        turmaRepository = mock(TurmaRepository.class);
        instituicaoRepository = mock(InstituicaoRepository.class);

        cursoService = new CursoService(cursoRepository, turmaRepository, instituicaoRepository, new ControleAcessoInstituicao());

        when(cursoRepository.findById(10L)).thenReturn(Optional.of(curso(10L, 1L)));

        when(cursoRepository.findById(20L)).thenReturn(Optional.of(curso(20L, 2L)));

        when(instituicaoRepository.findById(any())).thenAnswer(chamada -> Optional.of(instituicao(chamada.getArgument(0))));

        when(cursoRepository.save(any())).thenAnswer(chamada -> chamada.getArgument(0));
    }

    @AfterEach
    void limpar() {
        SecurityContextHolder.clearContext();
    }

    // =====================================================
    // CONSULTA
    // =====================================================

    @Test
    void administradorListaCursosDeTodasAsInstituicoes() {

        autenticar("ROLE_ADMINISTRADOR", null);

        when(cursoRepository.findAll()).thenReturn(List.of(curso(10L, 1L), curso(20L, 2L)));

        assertEquals(2, cursoService.listarTodos().size());

        verify(cursoRepository, never()).findAllByInstituicao_Id(any());
    }

    @Test
    void demaisPerfisListamApenasCursosDaPropriaInstituicao() {

        autenticar("ROLE_PROFESSOR", 1L);

        when(cursoRepository.findAllByInstituicao_Id(1L)).thenReturn(List.of(curso(10L, 1L)));

        List<CursoResposta> cursos = cursoService.listarTodos();

        assertEquals(1, cursos.size());
        assertEquals(1L, cursos.get(0).getInstituicaoId());

        verify(cursoRepository, never()).findAll();
    }

    @Test
    void buscarCursoDeOutraInstituicaoEhNegado() {

        autenticar("ROLE_SECRETARIO", 1L);

        assertEquals(10L, cursoService.buscarPorId(10L).getId());

        assertThrows(AccessDeniedException.class, () -> cursoService.buscarPorId(20L));
    }

    @Test
    void administradorBuscaCursoDeQualquerInstituicao() {

        autenticar("ROLE_ADMINISTRADOR", null);

        assertEquals(20L, cursoService.buscarPorId(20L).getId());
    }

    // =====================================================
    // CADASTRO
    // =====================================================

    @Test
    void cadastrarEmOutraInstituicaoEhNegado() {

        autenticar("ROLE_COORDENADOR", 1L);

        assertThrows(AccessDeniedException.class, () -> cursoService.cadastrar(requisicaoCadastro(2L)));

        verify(cursoRepository, never()).save(any());

        assertEquals(1L, cursoService.cadastrar(requisicaoCadastro(1L)).getInstituicaoId());
    }

    @Test
    void administradorPodeCadastrarEmQualquerInstituicao() {

        autenticar("ROLE_ADMINISTRADOR", null);

        CursoResposta resposta = cursoService.cadastrar(requisicaoCadastro(2L));

        assertEquals(2L, resposta.getInstituicaoId());

        verify(cursoRepository).save(any(Curso.class));
    }

    // =====================================================
    // ATUALIZAÇÃO COMPLETA - PUT
    // =====================================================

    @Test
    void atualizarCursoDeOutraInstituicaoEhNegado() {

        autenticar("ROLE_ADMIN_INSTITUICAO", 1L);

        assertThrows(AccessDeniedException.class, () -> cursoService.atualizar(20L, requisicaoAtualizar()));
    }

    @Test
    void atualizarCursoDaPropriaInstituicaoEhPermitido() {

        autenticar("ROLE_COORDENADOR", 1L);

        CursoResposta resposta = cursoService.atualizar(10L, requisicaoAtualizar());

        assertEquals("CUR-NOVO", resposta.getCodigo());
        assertEquals("Curso Atualizado", resposta.getNome());
        assertEquals(1L, resposta.getInstituicaoId());
    }

    @Test
    void atualizarCursoNaoAlteraInstituicaoOriginal() {

        autenticar("ROLE_ADMINISTRADOR", null);

        Curso cursoOriginal = cursoRepository.findById(20L).orElseThrow();

        Instituicao instituicaoOriginal = cursoOriginal.getInstituicao();

        cursoService.atualizar(20L, requisicaoAtualizar());

        assertSame(instituicaoOriginal, cursoOriginal.getInstituicao());

        assertEquals(2L, cursoOriginal.getInstituicao().getId());

        verify(instituicaoRepository, never()).findById(any());
    }

    @Test
    void codigoDuplicadoNoPutEhNegado() {

        autenticar("ROLE_ADMINISTRADOR", null);

        when(cursoRepository.existsByCodigoIgnoreCaseAndIdNot("CUR-NOVO", 10L)).thenReturn(true);

        assertThrows(RegraNegocioException.class, () -> cursoService.atualizar(10L, requisicaoAtualizar()));
    }

    // =====================================================
    // ATUALIZAÇÃO PARCIAL - PATCH
    // =====================================================

    @Test
    void atualizarParcialmenteCursoDaPropriaInstituicao() {

        autenticar("ROLE_COORDENADOR", 1L);

        CursoRequisicaoPatch dados = new CursoRequisicaoPatch();
        dados.setNome("Nome Alterado");

        CursoResposta resposta = cursoService.atualizarParcialmente(10L, dados);

        assertEquals("Nome Alterado", resposta.getNome());
        assertEquals("CUR-10", resposta.getCodigo());
        assertEquals(1L, resposta.getInstituicaoId());
    }

    @Test
    void atualizarParcialmenteCursoDeOutraInstituicaoEhNegado() {

        autenticar("ROLE_ADMIN_INSTITUICAO", 1L);

        CursoRequisicaoPatch dados = new CursoRequisicaoPatch();
        dados.setNome("Nome Alterado");

        assertThrows(AccessDeniedException.class, () -> cursoService.atualizarParcialmente(20L, dados));
    }

    @Test
    void atualizarParcialmenteNaoAlteraInstituicaoOriginal() {

        autenticar("ROLE_ADMINISTRADOR", null);

        Curso cursoOriginal = cursoRepository.findById(20L).orElseThrow();

        Instituicao instituicaoOriginal = cursoOriginal.getInstituicao();

        CursoRequisicaoPatch dados = new CursoRequisicaoPatch();
        dados.setNome("Curso Modificado");
        dados.setSituacao("INATIVO");

        CursoResposta resposta = cursoService.atualizarParcialmente(20L, dados);

        assertEquals("Curso Modificado", resposta.getNome());
        assertEquals("INATIVO", resposta.getSituacao());

        assertSame(instituicaoOriginal, cursoOriginal.getInstituicao());

        assertEquals(2L, resposta.getInstituicaoId());

        verify(instituicaoRepository, never()).findById(any());
    }

    @Test
    void codigoDuplicadoNoPatchEhNegado() {

        autenticar("ROLE_ADMINISTRADOR", null);

        when(cursoRepository.existsByCodigoIgnoreCaseAndIdNot("CUR-NOVO", 10L)).thenReturn(true);

        CursoRequisicaoPatch dados = new CursoRequisicaoPatch();
        dados.setCodigo("CUR-NOVO");

        assertThrows(RegraNegocioException.class, () -> cursoService.atualizarParcialmente(10L, dados));
    }

    // =====================================================
    // EXCLUSÃO
    // =====================================================

    @Test
    void excluirCursoDeOutraInstituicaoEhNegado() {

        autenticar("ROLE_COORDENADOR", 1L);

        assertThrows(AccessDeniedException.class, () -> cursoService.excluir(20L));

        verify(cursoRepository, never()).delete(any());

        cursoService.excluir(10L);

        verify(cursoRepository).delete(any());
    }

    @Test
    void excluirCursoComTurmasEhNegado() {

        autenticar("ROLE_ADMINISTRADOR", null);

        when(turmaRepository.existsByCurso_Id(10L)).thenReturn(true);

        assertThrows(RegraNegocioException.class, () -> cursoService.excluir(10L));

        verify(cursoRepository, never()).delete(any());
    }

    // =====================================================
    // JWT
    // =====================================================

    @Test
    void tokenSemInstituicaoEhNegado() {

        autenticar("ROLE_PROFESSOR", null);

        assertThrows(AccessDeniedException.class, () -> cursoService.listarTodos());
    }

    // =====================================================
    // MÉTODOS AUXILIARES
    // =====================================================

    private void autenticar(String perfil, Long instituicaoId) {

        Jwt.Builder token = Jwt.withTokenValue("token").header("alg", "HS512").subject("usuario").issuedAt(Instant.now()).expiresAt(Instant.now().plusSeconds(60));

        if (instituicaoId != null) {
            token.claim("instituicaoId", instituicaoId);
        }

        SecurityContextHolder.getContext().setAuthentication(new JwtAuthenticationToken(token.build(), List.of(new SimpleGrantedAuthority(perfil))));
    }

    private CursoRequisicao requisicaoCadastro(Long instituicaoId) {

        CursoRequisicao dados = new CursoRequisicao();

        dados.setCodigo("CUR-01");
        dados.setNome("Curso");
        dados.setSituacao("ATIVO");
        dados.setInstituicaoId(instituicaoId);

        return dados;
    }

    private CursoRequisicaoAtualizar requisicaoAtualizar() {

        CursoRequisicaoAtualizar dados = new CursoRequisicaoAtualizar();

        dados.setCodigo("CUR-NOVO");
        dados.setNome("Curso Atualizado");
        dados.setSituacao("ATIVO");

        return dados;
    }

    private Curso curso(Long id, Long instituicaoId) {

        Curso curso = new Curso();

        curso.setId(id);
        curso.setCodigo("CUR-" + id);
        curso.setNome("Curso " + id);
        curso.setSituacao("ATIVO");
        curso.setInstituicao(instituicao(instituicaoId));

        return curso;
    }

    private Instituicao instituicao(Long id) {

        Instituicao instituicao = new Instituicao();
        instituicao.setId(id);

        return instituicao;
    }
}
