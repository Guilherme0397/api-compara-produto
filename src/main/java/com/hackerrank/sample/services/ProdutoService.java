package com.hackerrank.sample.services;

import com.hackerrank.sample.dtos.ProdutoDTO;
import com.hackerrank.sample.models.ProdutoModel;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProdutoService {

    List<ProdutoDTO> getProdutosParaComparacao(List<UUID> ids);
    Optional<ProdutoDTO> getProdutoDetalhado(UUID id);
    Page<ProdutoDTO> listarProdutosComFiltroEPaginacao(Specification<ProdutoModel> spec, Pageable pageable);
    ProdutoDTO criarProduto(ProdutoDTO produtoDTO) throws ChangeSetPersister.NotFoundException;;
    Optional<ProdutoDTO> atualizarProduto(UUID id, ProdutoDTO produtoDTO) throws ChangeSetPersister.NotFoundException;
    void deletarProduto(UUID id) throws ChangeSetPersister.NotFoundException;;
}
