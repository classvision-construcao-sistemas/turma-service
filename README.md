# Class Vision — Turma Service

API REST Spring Boot 4 / Java 21 para cursos, turmas e vínculos de alunos. O projeto mantém o mapeamento das tabelas `cv03_instituicoes`, `cv04_cursos`, `cv05_turmas` e `cv09_turmas_alunos` do modelo original.

## Execução

O schema não é criado pelo Hibernate (`ddl-auto=none`). Num banco novo, execute
primeiro o DDL do `cv-authentication-service` (cria `cv03_instituicoes`) e depois
[`docs/ddl-turma.sql`](docs/ddl-turma.sql). Os serviços do ClassVision usam o mesmo MySQL.

Variáveis (podem ficar num arquivo `.env` na raiz do projeto, não versionado):

| Variável | Obrigatória | Descrição | Padrão |
|---|:---:|---|---|
| `DB_HOST`, `DB_NAME`, `DB_USER`, `DB_PASSWORD` | Sim | MySQL compartilhado | — |
| `DB_PORT` | Não | Porta do MySQL | `3306` |
| `JWT_SECRET` | Sim | O mesmo segredo do `cv-authentication-service` | — |
| `PORT` | Não | Porta HTTP | `8080` (use `8081` ao lado do auth) |

```bash
PORT=8081 ./mvnw spring-boot:run
```

No Windows: `mvnw.cmd spring-boot:run`. Para subir todo o ClassVision de uma vez, use o
`docker compose` da pasta `infra/`.

Swagger UI: `http://localhost:8081/swagger-ui.html`
OpenAPI JSON: `http://localhost:8081/v3/api-docs`

## Endpoints

Todos exigem `Authorization: Bearer <token>` emitido pelo `cv-authentication-service`.
Usuários que não são `ADMINISTRADOR` só enxergam cursos e turmas da própria instituição
(claim `instituicaoId`).

| Método | Rota | Função | Perfis |
|---|---|---|---|
| GET | `/cursos`, `/cursos/{id}` | Listar/consultar cursos | todos |
| POST, PUT, PATCH, DELETE | `/cursos`, `/cursos/{id}` | Cadastrar/atualizar/excluir curso | ADMINISTRADOR, ADMIN_INSTITUICAO, COORDENADOR |
| GET | `/turmas`, `/turmas/{id}`, `/turmas/curso/{cursoId}` | Listar/consultar turmas | autenticado |
| POST, PUT, PATCH, DELETE | `/turmas`, `/turmas/{id}` | Cadastrar/atualizar/excluir turma | ADMINISTRADOR, ADMIN_INSTITUICAO, COORDENADOR |
| POST | `/turmas/{id}/alunos` | Vincular aluno | + SECRETARIO |
| PUT, DELETE | `/turmas/{id}/alunos/{alunoId}` | Atualizar/remover vínculo | + SECRETARIO |

### Exemplos

Cadastrar curso:
```json
{"codigo":"ADS","nome":"Análise e Desenvolvimento de Sistemas","situacao":"ATIVO","instituicaoId":1}
```
Cadastrar turma:
```json
{"ano":2026,"nome":"ADS 2026 A","turno":"NOTURNO","sala":"A-10","inicioAula":"2026-02-01","fimAula":"2026-12-01","cursoId":1}
```
Vincular aluno (`alunoId` = matrícula numérica usada no reconhecimento facial; `ATIVO` ou
`MATRICULADO` contam como ativos na auditoria):
```json
{"alunoId":2024001,"situacao":"ATIVO"}
```

## Cuidados

- O vínculo de aluno é `@ElementCollection`, conforme modelo original; `alunoId` referencia identificador externo, não uma entidade local de aluno.
- `Instituicao` corrige a grafia da classe antiga `Intituicao`, sem renomear a tabela.
- O CRUD de instituições não estabelece relação com cursoService/turmaRespository porque ela não existe nos modelos fornecidos.
- O esquema MySQL precisa ter os nomes/colunas de `docs/ddl-turma.sql`, idênticos aos mapeamentos.
