package br.ifg.urutai.turmaservice.controller;

import br.ifg.urutai.turmaservice.dto.*;
import br.ifg.urutai.turmaservice.service.CursoService;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/cursos")
@Tag(name = "Cursos", description = "Gerenciamento de cursos")
public class CursoController {

    private static final String CONSULTA =
            "hasAnyRole('ADMINISTRADOR', 'ADMIN_INSTITUICAO', 'COORDENADOR', 'PROFESSOR', 'SECRETARIO', 'ASSISTENTE')";

    private static final String GERENCIAMENTO =
            "hasAnyRole('ADMINISTRADOR', 'ADMIN_INSTITUICAO', 'COORDENADOR')";

    private final CursoService cursoService;

    public CursoController(CursoService cursoService) {
        this.cursoService = cursoService;
    }

    @GetMapping
    @PreAuthorize(CONSULTA)
    @Operation(summary = "Listar cursos")
    public List<CursoResposta> listarTodos() {
        return cursoService.listarTodos();
    }

    @GetMapping("/{id}")
    @PreAuthorize(CONSULTA)
    @Operation(summary = "Buscar por identificador")
    public CursoResposta buscarPorId(@PathVariable Long id) {
        return cursoService.buscarPorId(id);
    }

    @PostMapping
    @PreAuthorize(GERENCIAMENTO)
    @Operation(summary = "Cadastrar")
    public ResponseEntity<CursoResposta> cadastrar(@Valid @RequestBody CursoRequisicao dados) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cursoService.cadastrar(dados));
    }

    @PutMapping("/{id}")
    @PreAuthorize(GERENCIAMENTO)
    @Operation(summary = "Atualizar")
    public CursoResposta atualizar(@PathVariable Long id, @Valid @RequestBody CursoRequisicao dados) {
        return cursoService.atualizar(id, dados);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize(GERENCIAMENTO)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Excluir")
    public void excluir(@PathVariable Long id) {
        cursoService.excluir(id);
    }

}
