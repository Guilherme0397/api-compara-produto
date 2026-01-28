package com.hackerrank.sample.services;

import com.hackerrank.sample.dtos.ProdutoDTO;
import com.hackerrank.sample.models.CategoriaModel;
import com.hackerrank.sample.models.ProdutoModel;

public interface MapperService {

    ProdutoDTO toProdutoDTO(ProdutoModel produtoModel);
    ProdutoModel toProdutoModel(ProdutoDTO produtoDTO, CategoriaModel categoriaModel);
}

