package com.hackerrank.sample.services.impl;

import com.hackerrank.sample.dtos.CategoriaDTO;
import com.hackerrank.sample.dtos.EspecificacaoDTO;
import com.hackerrank.sample.dtos.ProdutoDTO;
import com.hackerrank.sample.models.CategoriaModel;
import com.hackerrank.sample.models.EspecificacaoModel;
import com.hackerrank.sample.models.ProdutoModel;
import com.hackerrank.sample.services.MapperService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MapperServiceImpl implements MapperService {

    @Override
    public ProdutoDTO toProdutoDTO(ProdutoModel model) {
        ProdutoDTO dto = new ProdutoDTO(
                model.getProdutoId(), model.getNome(), model.getUrlImagem(), model.getDescricao(),
                model.getPreco(), model.getClassificacao(), null, null
        );

        if (model.getCategoria() != null) {
            dto = new ProdutoDTO(dto.produtoId(), dto.nome(), dto.urlImagem(), dto.descricao(),
                    dto.preco(), dto.classificacao(),
                    new CategoriaDTO(model.getCategoria().getIdCategoria(), model.getCategoria().getNome()),
                    dto.especificacoes());
        }

        if (model.getEspecificacoes() != null && !model.getEspecificacoes().isEmpty()) {
            List<EspecificacaoDTO> specs = model.getEspecificacoes().stream()
                    .map(s -> new EspecificacaoDTO(s.getNomeAtributo().toString(), s.getValorAtributo()))
                    .collect(Collectors.toList());

            dto = new ProdutoDTO(dto.produtoId(), dto.nome(), dto.urlImagem(), dto.descricao(),
                    dto.preco(), dto.classificacao(),
                    dto.categoria(), specs);
        }

        return dto;
    }

    @Override
    public ProdutoModel toProdutoModel(ProdutoDTO dto, CategoriaModel categoriaModel) {
        ProdutoModel model = new ProdutoModel();
        BeanUtils.copyProperties(dto, model);

        model.setCategoria(categoriaModel);

        if (dto.especificacoes() != null && !dto.especificacoes().isEmpty()) {
            Set<EspecificacaoModel> especificacoesModelSet = dto.especificacoes().stream()
                    .map(especDto -> toEspecificacaoModel(especDto, model))
                    .collect(Collectors.toSet());

            model.setEspecificacoes(especificacoesModelSet);
        }
        return model;
    }

    private EspecificacaoModel toEspecificacaoModel(EspecificacaoDTO especificacaoDTO, ProdutoModel produtoModel) {
        EspecificacaoModel model = new EspecificacaoModel();
        model.setNomeAtributo(especificacaoDTO.nomeAtributo());
        model.setValorAtributo(especificacaoDTO.valorAtributo());
        model.setProduto(produtoModel);
        return model;
    }
}