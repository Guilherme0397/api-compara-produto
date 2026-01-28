package com.hackerrank.sample.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "TB_CATEGORIAS")
public class CategoriaModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idCategoria;

    @Column(nullable = false, unique = true, length = 100)
    private String nome;

    @OneToMany(mappedBy = "categoria", fetch = FetchType.LAZY)
    private Set<ProdutoModel> produtos;

    public UUID getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(UUID idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Set<ProdutoModel> getProdutos() {
        return produtos;
    }

    public void setProdutos(Set<ProdutoModel> produtos) {
        this.produtos = produtos;
    }
}
