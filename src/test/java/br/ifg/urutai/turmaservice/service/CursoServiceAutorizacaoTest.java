package br.ifg.urutai.turmaservice.service;

import br.ifg.urutai.turmaservice.dto.CursoRequisicao;
import br.ifg.urutai.turmaservice.dto.CursoResposta;
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

        cursoService = new CursoService(
                cursoRepository,
                turmaRepository,
                instituicaoRepository,
                new ControleAcessoInstituicao()
        );

        when(cursoRepository.findById(10L)).thenReturn(Optional.of(curso(10L, 1L)));
        when(cursoRepository.findById(20L)).thenReturn(Optional.of(curso(20L, 2L)));
        when(instituicaoRepository.findById(any())).thenAnswer(chamada ->
                Optional.of(instituicao(chamada.getArgument(0))));
        when(cursoRepository.save(any())).thenAnswer(chamada -> chamada.getArgument(0));
    }

    @AfterEach
    void limpar() {
        SecurityContextHolder.clearContext();
    }

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

    @Test
    void cadastrarEmOutraInstituicaoEhNegado() {
        autenticar("ROLE_COORDENADOR", 1L);

        assertThrows(AccessDeniedException.class, () -> cursoService.cadastrar(requisicao(2L)));
        verify(cursoRepository, never()).save(any());

        assertEquals(1L, cursoService.cadastrar(requisicao(1L)).getInstituicaoId());
    }

    @Test
    void atualizarCursoDeOutraInstituicaoOuMoverParaOutraEhNegado() {
        autenticar("ROLE_ADMIN_INSTITUICAO", 1L);

        assertThrows(AccessDeniedException.class, () -> cursoService.atualizar(20L, requisicao(1L)));
        assertThrows(AccessDeniedException.class, () -> cursoService.atualizar(10L, requisicao(2L)));

        assertEquals(1L, cursoService.atualizar(10L, requisicao(1L)).getInstituicaoId());
    }

    @Test
    void excluirCursoDeOutraInstituicaoEhNegado() {
        autenticar("ROLE_COORDENADOR", 1L);

        assertThrows(AccessDeniedException.class, () -> cursoService.excluir(20L));
        verify(cursoRepository, never()).delete(any());

        cursoService.excluir(10L);
        verify(cursoRepository).delete(any());
    }

    @Test
    void tokenSemInstituicaoEhNegado() {
        autenticar("ROLE_PROFESSOR", null);

        assertThrows(AccessDeniedException.class, () -> cursoService.listarTodos());
    }

    private void autenticar(String perfil, Long instituicaoId) {
        Jwt.Builder token = Jwt.withTokenValue("token")
                .header("alg", "HS512")
                .subject("usuario")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(60));

        if (instituicaoId != null) {
            token.claim("instituicaoId", instituicaoId);
        }

        SecurityContextHolder.getContext().setAuthentication(
                new JwtAuthenticationToken(token.build(), List.of(new SimpleGrantedAuthority(perfil)))
        );
    }

    private CursoRequisicao requisicao(Long instituicaoId) {
        CursoRequisicao dados = new CursoRequisicao();
        dados.setCodigo("CUR-01");
        dados.setNome("Curso");
        dados.setSituacao("ATIVO");
        dados.setInstituicaoId(instituicaoId);
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
