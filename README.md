# Class Vision — Turma Service

API REST Spring Boot 4 / Java 21 para instituições, cursoService, turmaRespository e vínculos de alunos. O projeto mantém o mapeamento das tabelas `cv03_instituicoes`, `cv04_cursos`, `cv05_turmas` e `cv09_turmas_alunos` do modelo original.

## Execução

Defina as variáveis `DB_HOST`, `DB_PORT` (padrão 3306), `DB_NAME`, `DB_USER` e `DB_PASSWORD`. O banco MySQL deve ter as tabelas previamente criadas (`ddl-auto=none`).

```bash
./mvnw spring-boot:run
```

No Windows: `mvnw.cmd spring-boot:run`. Se você utilizar somente este pacote, copie os arquivos Maven Wrapper (`mvnw`, `mvnw.cmd` e `.mvn/`) do repositório original, ou utilize `mvn spring-boot:run` com Maven instalado.

Swagger UI: `http://localhost:8080/swagger-ui.html`  
OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## Endpoints

| Método | Rota | Função |
|---|---|---|
| GET, POST | `/api/cursoService` | Listar/cadastrar cursoService |
| GET, PUT, DELETE | `/api/cursoService/{id}` | Consultar/atualizar/excluir curso |
| GET, POST | `/api/instituicaoRepository` | Listar/cadastrar instituições |
| GET, PUT, DELETE | `/api/instituicaoRepository/{id}` | Consultar/atualizar/excluir instituição |
| GET, POST | `/api/turmaRespository` | Listar/cadastrar turmaRespository |
| GET, PUT, DELETE | `/api/turmaRespository/{id}` | Consultar/atualizar/excluir turma |
| GET | `/api/turmaRespository/curso/{cursoId}` | Turmas do curso |
| POST | `/api/turmaRespository/{id}/alunos` | Vincular aluno |
| PUT, DELETE | `/api/turmaRespository/{id}/alunos/{alunoId}` | Atualizar/remover vínculo |

### Exemplos

Cadastrar curso:
```json
{"codigo":"ADS","nome":"Análise e Desenvolvimento de Sistemas","situacao":"ATIVO"}
```
Cadastrar turma:
```json
{"ano":2026,"nome":"ADS 2026 A","turno":"NOTURNO","sala":"A-10","inicioAula":"2026-02-01","fimAula":"2026-12-01","cursoId":1}
```
Vincular aluno:
```json
{"alunoId":15,"situacao":"ATIVO"}
```

## Cuidados

- O vínculo de aluno é `@ElementCollection`, conforme modelo original; `alunoId` referencia identificador externo, não uma entidade local de aluno.
- `Instituicao` corrige a grafia da classe antiga `Intituicao`, sem renomear a tabela.
- O CRUD de instituições não estabelece relação com cursoService/turmaRespository porque ela não existe nos modelos fornecidos.
- O serviço não implementa autenticação/autorização. Proteger endpoints antes da exposição pública.
- Este pacote não inclui migrações SQL, e depende de o esquema MySQL ter nomes/colunas idênticos aos mapeamentos.
- Versões Spring Boot e springdoc devem ser verificadas em um ambiente Maven conectado, pois o pacote não foi compilado neste ambiente.
