package com.hackerrank.sample.services.impl;

import com.hackerrank.sample.dtos.ProdutoDTO;
import com.hackerrank.sample.models.CategoriaModel;
import com.hackerrank.sample.models.ProdutoModel;
import com.hackerrank.sample.repositories.CategoriaRepository;
import com.hackerrank.sample.repositories.ProdutoRepository;
import com.hackerrank.sample.services.MapperService;
import com.hackerrank.sample.services.ProdutoService;
import lombok.extern.slf4j.Slf4j; // Importação do Lombok para o logger
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProdutoServiceImpl implements ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final CategoriaRepository categoriaRepository;
    private final MapperService mapperService;

    public ProdutoServiceImpl(ProdutoRepository produtoRepository, CategoriaRepository categoriaRepository, MapperService mapperService) {
        this.produtoRepository = produtoRepository;
        this.categoriaRepository = categoriaRepository;
        this.mapperService = mapperService;
        log.info("ProdutoServiceImpl inicializado.");
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProdutoDTO> getProdutosParaComparacao(List<UUID> ids) {
        log.debug("Iniciando busca de produtos para comparação. IDs: {}", ids);
        List<ProdutoModel> produtos = produtoRepository.findAllByIdsWithDetails(ids);
        log.info("Encontrados {} produtos para comparação.", produtos.size());

        return produtos.stream()
                .map(mapperService::toProdutoDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProdutoDTO> getProdutoDetalhado(UUID id) {
        log.debug("Buscando produto detalhado por ID: {}", id);
        Optional<ProdutoModel> produtoModelOpt = produtoRepository.findById(id);

        if (produtoModelOpt.isPresent()) {
            log.info("Produto detalhado {} encontrado.", id);
        } else {
            log.info("Produto detalhado {} não encontrado.", id);
        }

        return produtoModelOpt.map(mapperService::toProdutoDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProdutoDTO> listarProdutosComFiltroEPaginacao(Specification<ProdutoModel> spec, Pageable pageable) {
        log.debug("Listando produtos com filtro e paginação. Página: {}, Tamanho: {}", pageable.getPageNumber(), pageable.getPageSize());
        Page<ProdutoModel> produtoPage = produtoRepository.findAll(spec, pageable);
        log.info("Consulta de produtos concluída. Total de elementos: {}", produtoPage.getTotalElements());
        return produtoPage.map(mapperService::toProdutoDTO);
    }

    @Override
    @Transactional
    public ProdutoDTO criarProduto(ProdutoDTO produtoDTO) throws ChangeSetPersister.NotFoundException {
        log.info("Tentando criar produto: {}", produtoDTO.nome());

        CategoriaModel categoriaModel = getCategoriaModel(produtoDTO.categoria().idCategoria());
        log.debug("Categoria {} encontrada para o novo produto.", categoriaModel.getNome());

        ProdutoModel produtoModel = mapperService.toProdutoModel(produtoDTO, categoriaModel);
        ProdutoModel savedModel = produtoRepository.save(produtoModel);

        log.info("Produto {} criado com sucesso. ID gerado: {}", savedModel.getNome(), savedModel.getProdutoId());
        return mapperService.toProdutoDTO(savedModel);
    }

    @Override
    @Transactional
    public Optional<ProdutoDTO> atualizarProduto(UUID id, ProdutoDTO produtoDTO) throws ChangeSetPersister.NotFoundException {
        log.info("Tentando atualizar produto com ID: {}", id);

        if (!produtoRepository.existsById(id)) {
            log.warn("Falha na atualização: Produto com ID {} não existe.", id);
            throw new ChangeSetPersister.NotFoundException();
        }

        CategoriaModel categoriaModel = getCategoriaModel(produtoDTO.categoria().idCategoria());
        log.debug("Categoria {} encontrada para atualização do produto {}.", categoriaModel.getNome(), id);
        
        ProdutoModel produtoModel = mapperService.toProdutoModel(produtoDTO, categoriaModel);
        produtoModel.setProdutoId(id);

        ProdutoModel updatedModel = produtoRepository.save(produtoModel);
        log.info("Produto com ID {} atualizado com sucesso.", id);
        return Optional.of(mapperService.toProdutoDTO(updatedModel));
    }

    @Override
    @Transactional
    public void deletarProduto(UUID id) throws ChangeSetPersister.NotFoundException {
        log.warn("Tentando deletar produto com ID: {}", id);

        if (!produtoRepository.existsById(id)) {
            log.warn("Falha na deleção: Produto com ID {} não existe.", id);
            throw new ChangeSetPersister.NotFoundException();
        }

        produtoRepository.deleteById(id);
        log.info("Produto com ID {} deletado com sucesso.", id);
    }

    private CategoriaModel getCategoriaModel(UUID categoriaId) throws ChangeSetPersister.NotFoundException {
        log.debug("Buscando Categoria por ID: {}", categoriaId);
        try {
            return categoriaRepository.findById(categoriaId)
                    .orElseThrow(() -> {
                        log.error("Categoria com ID {} não encontrada.", categoriaId);
                        return new ChangeSetPersister.NotFoundException();
                    });
        } catch (ChangeSetPersister.NotFoundException e) {
            throw e;
        }
    }
}