package com.hackerrank.sample.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "TB_ESPECIFICACOES")
public class EspecificacaoModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID EspecificacaoId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_produto", nullable = false)
    private ProdutoModel produto;

    @Column(nullable = false, length = 100)
    private String nomeAtributo;

    @Column(nullable = false, length = 255)
    private String valorAtributo;

    public UUID getEspecificacaoId() {
        return EspecificacaoId;
    }

    public void setEspecificacaoId(UUID especificacaoId) {
        this.EspecificacaoId = especificacaoId;
    }

    public ProdutoModel getProduto() {
        return produto;
    }

    public void setProduto(ProdutoModel produto) {
        this.produto = produto;
    }

    public String getNomeAtributo() {
        return nomeAtributo;
    }

    public void setNomeAtributo(String nomeAtributo) {
        this.nomeAtributo = nomeAtributo;
    }

    public String getValorAtributo() {
        return valorAtributo;
    }

    public void setValorAtributo(String valorAtributo) {
        this.valorAtributo = valorAtributo;
    }
}
