package com.hackerrank.sample.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "TB_PRODUTOS")
public class ProdutoModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID produtoId;

    @Column(nullable = false, length = 150)
    private String nome;

    @Column(nullable = true, length = 255)
    private String urlImagem;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String descricao;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;

    @Column(nullable = false, precision = 3, scale = 2)
    private BigDecimal classificacao;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_categoria", nullable = false)
    private CategoriaModel categoria;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(mappedBy = "produto", fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @Fetch(FetchMode.SUBSELECT)
    private Set<EspecificacaoModel> especificacoes;

    public UUID getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(UUID produtoId) {
        this.produtoId = produtoId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getUrlImagem() {
        return urlImagem;
    }

    public void setUrlImagem(String urlImagem) {
        this.urlImagem = urlImagem;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public BigDecimal getClassificacao() {
        return classificacao;
    }

    public void setClassificacao(BigDecimal classificacao) {
        this.classificacao = classificacao;
    }

    public CategoriaModel getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaModel categoria) {
        this.categoria = categoria;
    }

    public Set<EspecificacaoModel> getEspecificacoes() {
        return especificacoes;
    }

    public void setEspecificacoes(Set<EspecificacaoModel> especificacoes) {
        this.especificacoes = especificacoes;
    }
}
