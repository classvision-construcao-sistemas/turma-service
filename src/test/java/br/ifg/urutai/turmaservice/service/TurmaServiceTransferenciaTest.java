
package br.ifg.urutai.turmaservice.service;

import br.ifg.urutai.turmaservice.dto.*;
import br.ifg.urutai.turmaservice.exception.RegraNegocioException;
import br.ifg.urutai.turmaservice.model.*;
import br.ifg.urutai.turmaservice.repository.TurmaRepository;
import br.ifg.urutai.turmaservice.security.ControleAcessoInstituicao;

import org.junit.jupiter.api.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TurmaServiceTransferenciaTest {

    private TurmaRepository turmaRepository;
    private CursoService cursoService;
    private TurmaService turmaService;
    private Turma turma;

    @BeforeEach
    void configurar() {

        turmaRepository = mock(TurmaRepository.class);
        cursoService = mock(CursoService.class);

        turmaService = new TurmaService(
                turmaRepository,
                cursoService,
                new ControleAcessoInstituicao()
        );

        turma = new Turma();
        turma.setId(100L);
        turma.setAno(2026);
        turma.setNome("Turma Original");
        turma.setTurno("MATUTINO");
        turma.setCurso(criarCurso(1L, 1L));

        when(turmaRepository.buscarPorIdComRelacionamentos(100L))
                .thenReturn(Optional.of(turma));

        // Cursos 1 e 2 pertencem à instituição 1.
        when(cursoService.obterCurso(1L))
                .thenReturn(criarCurso(1L, 1L));

        when(cursoService.obterCurso(2L))
                .thenReturn(criarCurso(2L, 1L));

        // Curso 3 pertence à instituição 2.
        when(cursoService.obterCurso(3L))
                .thenReturn(criarCurso(3L, 2L));
    }

    @AfterEach
    void limpar() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void administradorNaoPodeTransferirTurmaPorPut() {

        autenticar("ROLE_ADMINISTRADOR", null);

        assertThrows(
                RegraNegocioException.class,
                () -> turmaService.atualizar(
                        100L, requisicao(3L)
                )
        );

        assertEquals(1L, turma.getCurso().getId());
        assertEquals("Turma Original", turma.getNome());
    }

    @Test
    void administradorNaoPodeTransferirTurmaPorPatch() {

        autenticar("ROLE_ADMINISTRADOR", null);

        TurmaRequisicaoPatch dados =
                new TurmaRequisicaoPatch();

        dados.setCursoId(3L);
        dados.setNome("Turma Alterada");

        assertThrows(
                RegraNegocioException.class,
                () -> turmaService.atualizarParcialmente(
                        100L, dados
                )
        );

        assertEquals(1L, turma.getCurso().getId());
        assertEquals("Turma Original", turma.getNome());
    }

    @Test
    void administradorPodeTrocarCursoNaMesmaInstituicao() {

        autenticar("ROLE_ADMINISTRADOR", null);

        TurmaResposta resposta = turmaService.atualizar(
                100L, requisicao(2L)
        );

        assertEquals(2L, resposta.getCursoId());
        assertEquals(1L,
                turma.getCurso().getInstituicao().getId());
    }

    @Test
    void coordenadorPodeTrocarCursoNaMesmaInstituicaoPorPatch() {

        autenticar("ROLE_COORDENADOR", 1L);

        TurmaRequisicaoPatch dados =
                new TurmaRequisicaoPatch();

        dados.setCursoId(2L);

        TurmaResposta resposta =
                turmaService.atualizarParcialmente(100L, dados);

        assertEquals(2L, resposta.getCursoId());
        assertEquals(1L,
                turma.getCurso().getInstituicao().getId());
    }

    @Test
    void coordenadorNaoPodeTransferirParaOutraInstituicao() {

        autenticar("ROLE_COORDENADOR", 1L);

        assertThrows(
                AccessDeniedException.class,
                () -> turmaService.atualizar(
                        100L, requisicao(3L)
                )
        );

        assertEquals(1L, turma.getCurso().getId());
    }

    private TurmaRequisicao requisicao(Long cursoId) {

        TurmaRequisicao dados = new TurmaRequisicao();

        dados.setAno(2026);
        dados.setNome("Turma Atualizada");
        dados.setTurno("NOTURNO");
        dados.setCursoId(cursoId);

        return dados;
    }

    private Curso criarCurso(
            Long cursoId,
            Long instituicaoId) {

        Instituicao instituicao = new Instituicao();
        instituicao.setId(instituicaoId);

        Curso curso = new Curso();
        curso.setId(cursoId);
        curso.setCodigo("CUR-" + cursoId);
        curso.setNome("Curso " + cursoId);
        curso.setSituacao("ATIVO");
        curso.setInstituicao(instituicao);

        return curso;
    }

    private void autenticar(
            String perfil,
            Long instituicaoId) {

        Instant agora = Instant.now();

        Jwt.Builder token = Jwt.withTokenValue("token")
                .header("alg", "HS512")
                .subject("usuario")
                .issuedAt(agora)
                .expiresAt(agora.plusSeconds(60));

        if (instituicaoId != null) {
            token.claim("instituicaoId", instituicaoId);
        }

        SecurityContextHolder.getContext().setAuthentication(
                new JwtAuthenticationToken(
                        token.build(),
                        List.of(new SimpleGrantedAuthority(perfil))
                )
        );
    }
}
