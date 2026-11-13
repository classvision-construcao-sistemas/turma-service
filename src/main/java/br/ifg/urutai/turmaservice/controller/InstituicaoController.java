package br.ifg.urutai.turmaservice.controller;

import br.ifg.urutai.turmaservice.dto.*;
import br.ifg.urutai.turmaservice.service.InstituicaoService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/instituicoes")
@Tag(name = "Instituições", description = "Gerenciamento de instituições")
public class InstituicaoController {
    private final InstituicaoService instituicaoService;

    public InstituicaoController(InstituicaoService instituicaoService) {
        this.instituicaoService = instituicaoService;
    }

    @GetMapping
    @Operation(summary = "Listar instituições")
    public List<InstituicaoResposta> listarTodos() {
        return instituicaoService.listarTodos();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar por identificador")
    public InstituicaoResposta buscarPorId(@PathVariable Long id) {
        return instituicaoService.buscarPorId(id);
    }

    @PostMapping
    @Operation(summary = "Cadastrar")
    public ResponseEntity<InstituicaoResposta> cadastrar(@Valid @RequestBody InstituicaoRequisicao dados) {
        return ResponseEntity.status(HttpStatus.CREATED).body(instituicaoService.cadastrar(dados));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar")
    public InstituicaoResposta atualizar(@PathVariable Long id, @Valid @RequestBody InstituicaoRequisicao dados) {
        return instituicaoService.atualizar(id, dados);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Excluir")
    public void excluir(@PathVariable Long id) {
        instituicaoService.excluir(id);
    }

}
