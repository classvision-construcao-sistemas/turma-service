package br.ifg.urutai.turmaservice.controller;

import br.ifg.urutai.turmaservice.dto.*;
import br.ifg.urutai.turmaservice.service.TurmaService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/turmas")
@Tag(name = "Turmas", description = "Gerenciamento de turmas")
public class TurmaController {
    private final TurmaService turmaService;

    public TurmaController(TurmaService turmaService) {
        this.turmaService = turmaService;
    }

    @GetMapping
    @Operation(summary = "Listar turmas")
    public List<TurmaResposta> listarTodos() {
        return turmaService.listarTodas();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar por identificador")
    public TurmaResposta buscarPorId(@PathVariable Long id) {
        return turmaService.buscarPorId(id);
    }

    @PostMapping
    @Operation(summary = "Cadastrar")
    public ResponseEntity<TurmaResposta> cadastrar(@Valid @RequestBody TurmaRequisicao dados) {
        return ResponseEntity.status(HttpStatus.CREATED).body(turmaService.cadastrar(dados));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar")
    public TurmaResposta atualizar(@PathVariable Long id, @Valid @RequestBody TurmaRequisicao dados) {
        return turmaService.atualizar(id, dados);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Excluir")
    public void excluir(@PathVariable Long id) {
        turmaService.excluir(id);
    }

    @GetMapping("/curso/{cursoId}")
    @Operation(summary = "Listar turmas de um curso")
    public List<TurmaResposta> listarPorCurso(@PathVariable Long cursoId) {
        return turmaService.listarPorCurso(cursoId);
    }

    @PostMapping("/{id}/alunos")
    @Operation(summary = "Vincular aluno à turma")
    public ResponseEntity<TurmaResposta> vincularAluno(@PathVariable Long id, @Valid @RequestBody VinculoAlunoRequisicao dados) {
        return ResponseEntity.status(HttpStatus.CREATED).body(turmaService.vincularAluno(id, dados));
    }

    @PutMapping("/{id}/alunos/{alunoId}")
    @Operation(summary = "Atualizar situação de aluno vinculado")
    public TurmaResposta atualizarVinculo(@PathVariable Long id, @PathVariable Long alunoId, @Valid @RequestBody VinculoAlunoRequisicao dados) {
        return turmaService.atualizarVinculo(id, alunoId, dados);
    }

    @DeleteMapping("/{id}/alunos/{alunoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Desvincular aluno da turma")
    public void desvincularAluno(@PathVariable Long id, @PathVariable Long alunoId) {
        turmaService.desvincularAluno(id, alunoId);
    }

}
