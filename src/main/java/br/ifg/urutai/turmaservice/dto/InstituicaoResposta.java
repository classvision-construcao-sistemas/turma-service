package br.ifg.urutai.turmaservice.dto;
import java.time.Instant;
public record InstituicaoResposta(Long id,String nome,String cnpj,String telefone,String situacao,Instant criadoEm,Instant atualizadoEm) {}
