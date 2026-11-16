package br.ifg.urutai.turmaservice.dto;

public class CursoResposta {

    private Long id;
    private String codigo;
    private String nome;
    private String situacao;
    private Long instituicaoId;

    public CursoResposta() {
    }

    public CursoResposta(
            Long id,
            String codigo,
            String nome,
            String situacao,
            Long instituicaoId) {
        this.id = id;
        this.codigo = codigo;
        this.nome = nome;
        this.situacao = situacao;
        this.instituicaoId = instituicaoId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public Long getInstituicaoId() {
        return instituicaoId;
    }

    public void setInstituicaoId(Long instituicaoId) {
        this.instituicaoId = instituicaoId;
    }
}