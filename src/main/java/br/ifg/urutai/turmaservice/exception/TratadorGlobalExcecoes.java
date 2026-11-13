package br.ifg.urutai.turmaservice.exception;
import java.time.Instant;
import java.util.*;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.dao.DataIntegrityViolationException;
import jakarta.servlet.http.HttpServletRequest;
@RestControllerAdvice
public class TratadorGlobalExcecoes {
 public record ErroResposta(Instant dataHora,int status,String mensagem,String caminho,Map<String,String> campos) {}
 @ExceptionHandler(RecursoNaoEncontradoException.class)
 public ResponseEntity<ErroResposta> recursoNaoEncontrado(RecursoNaoEncontradoException erro,HttpServletRequest requisicao){return criar(HttpStatus.NOT_FOUND,erro.getMessage(),requisicao,Map.of());}
 @ExceptionHandler(RegraNegocioException.class)
 public ResponseEntity<ErroResposta> regraNegocio(RegraNegocioException erro,HttpServletRequest requisicao){return criar(HttpStatus.CONFLICT,erro.getMessage(),requisicao,Map.of());}
 @ExceptionHandler(DataIntegrityViolationException.class)
 public ResponseEntity<ErroResposta> integridade(DataIntegrityViolationException erro,HttpServletRequest requisicao){return criar(HttpStatus.CONFLICT,"Operação viola uma restrição do banco de dados",requisicao,Map.of());}
 @ExceptionHandler(MethodArgumentNotValidException.class)
 public ResponseEntity<ErroResposta> validacao(MethodArgumentNotValidException erro,HttpServletRequest requisicao){
  Map<String,String> campos=new LinkedHashMap<>();
  erro.getBindingResult().getFieldErrors().forEach(campo->campos.putIfAbsent(campo.getField(),campo.getDefaultMessage()));
  return criar(HttpStatus.BAD_REQUEST,"Dados inválidos",requisicao,campos);
 }
 @ExceptionHandler(org.springframework.http.converter.HttpMessageNotReadableException.class)
 public ResponseEntity<ErroResposta> jsonInvalido(Exception erro,HttpServletRequest requisicao){return criar(HttpStatus.BAD_REQUEST,"Corpo da requisição inválido",requisicao,Map.of());}
 private ResponseEntity<ErroResposta> criar(HttpStatus status,String mensagem,HttpServletRequest requisicao,Map<String,String> campos){return ResponseEntity.status(status).body(new ErroResposta(Instant.now(),status.value(),mensagem,requisicao.getRequestURI(),campos));}
}
