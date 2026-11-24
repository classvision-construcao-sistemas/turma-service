-- =====================================================================
-- Turma Service - DDL MySQL 8 (cursos, turmas e vínculos de alunos)
-- Espelha os mapeamentos JPA (ddl-auto=none) de br.ifg.urutai.turmaservice.model.
--
-- Depende de cv03_instituicoes, criada pelo cv-authentication-service
-- (docs/database/ddl/DDL-CV-Authentication-Service.sql). Os serviços do
-- ClassVision compartilham o mesmo banco MySQL.
-- =====================================================================

SET NAMES utf8mb4;

-- ---------------------------------------------------------------------
-- cv04_cursos
-- ---------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS cv04_cursos (
    id                    BIGINT       NOT NULL AUTO_INCREMENT,
    codigo                VARCHAR(50)  NOT NULL,
    nome                  VARCHAR(255) NOT NULL,
    situacao              VARCHAR(30)  NOT NULL,
    bigint_instituicao_id BIGINT       NOT NULL,
    CONSTRAINT pk_cv04_cursos PRIMARY KEY (id),
    CONSTRAINT uk_cv04_cursos_codigo UNIQUE (codigo),
    INDEX idx_cv04_cursos_instituicao (bigint_instituicao_id),
    CONSTRAINT fk_cv04_cursos_instituicao FOREIGN KEY (bigint_instituicao_id)
        REFERENCES cv03_instituicoes (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ---------------------------------------------------------------------
-- cv05_turmas
-- ---------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS cv05_turmas (
    id              BIGINT            NOT NULL AUTO_INCREMENT,
    ano             SMALLINT UNSIGNED NOT NULL,
    nome            VARCHAR(255)      NOT NULL,
    turno           VARCHAR(30)       NOT NULL,
    sala            VARCHAR(50)                NULL,
    inicio_aula     DATE                       NULL,
    fim_aula        DATE                       NULL,
    bigint_curso_id BIGINT            NOT NULL,
    CONSTRAINT pk_cv05_turmas PRIMARY KEY (id),
    INDEX idx_cv05_turmas_curso (bigint_curso_id),
    CONSTRAINT fk_cv05_turmas_curso FOREIGN KEY (bigint_curso_id)
        REFERENCES cv04_cursos (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ---------------------------------------------------------------------
-- cv09_turmas_alunos (@ElementCollection VinculoAluno)
-- aluno_id é a matrícula numérica do aluno (chave usada no reconhecimento facial).
-- ---------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS cv09_turmas_alunos (
    bigint_turma_id BIGINT      NOT NULL,
    aluno_id        BIGINT      NOT NULL,
    situacao        VARCHAR(30) NOT NULL,
    CONSTRAINT uk_cv09_turmas_alunos UNIQUE (bigint_turma_id, aluno_id),
    INDEX idx_cv09_turmas_alunos_aluno (aluno_id),
    CONSTRAINT fk_cv09_turmas_alunos_turma FOREIGN KEY (bigint_turma_id)
        REFERENCES cv05_turmas (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;
