package com.hackerrank.sample.controllers;

import com.hackerrank.sample.dtos.ProdutoDTO;
import com.hackerrank.sample.models.ProdutoModel;
import com.hackerrank.sample.services.ProdutoService;
import com.hackerrank.sample.specifications.SpecificationTemplate;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    @Autowired
    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
        log.info("ProdutoController inicializado.");
    }

    @GetMapping
    public ResponseEntity<Page<ProdutoDTO>> listarProdutos(SpecificationTemplate.ProdutoSpec spec, Pageable pageable) {
        log.debug("Requisição para listar produtos recebida. Filtros: {}, Paginação: {}", spec, pageable);

        Page<ProdutoDTO> produtosPage = produtoService.listarProdutosComFiltroEPaginacao(spec, pageable);

        log.info("Produtos listados com sucesso. Total de elementos: {}", produtosPage.getTotalElements());
        return ResponseEntity.ok(produtosPage);
    }

    @GetMapping("/categoria/{idCategoria}")
    public ResponseEntity<Page<ProdutoDTO>> buscarProdutosPorCategoria(
            @PathVariable(value = "idCategoria") UUID idCategoria,
            SpecificationTemplate.ProdutoSpec spec,
            Pageable pageable) {

        log.debug("Buscando produtos por categoria. ID Categoria: {}, Filtros adicionais: {}", idCategoria, spec);

        Specification<ProdutoModel> categoriaSpec = SpecificationTemplate.produtosDaCategoria(idCategoria);
        Specification<ProdutoModel> combinedSpec = categoriaSpec.and(spec);

        Page<ProdutoDTO> produtosPage = produtoService.listarProdutosComFiltroEPaginacao(combinedSpec, pageable);

        log.info("Produtos encontrados para a categoria {}. Total na página: {}", idCategoria, produtosPage.getNumberOfElements());
        return ResponseEntity.ok(produtosPage);
    }

    @GetMapping("/categoria/{idCategoria}/filtrar")
    public ResponseEntity<Page<ProdutoDTO>> buscarProdutosComFiltrosMultiplos(
            @PathVariable(value = "idCategoria") UUID idCategoria,
            @RequestParam(value = "nomeAtributo", required = false) String nomeAtributo,
            @RequestParam(value = "valorAtributo", required = false) String valorAtributo,
            @Nullable SpecificationTemplate.ProdutoSpec spec,
            Pageable pageable) {

        log.debug("Buscando produtos com filtros múltiplos. ID Categoria: {}, Atributo: {}={}, Filtros base: {}",
                idCategoria, nomeAtributo, valorAtributo, spec);

        Specification<ProdutoModel> combinedSpec = Specification.where(spec);

        combinedSpec = combinedSpec.and(SpecificationTemplate.produtosDaCategoria(idCategoria));

        if (nomeAtributo != null && valorAtributo != null && !valorAtributo.isEmpty()) {
            log.debug("Adicionando filtro de especificação: {}: {}", nomeAtributo, valorAtributo);
            combinedSpec = combinedSpec.and(SpecificationTemplate.temEspecificacao(nomeAtributo, valorAtributo));
        }

        Page<ProdutoDTO> produtosPage = produtoService.listarProdutosComFiltroEPaginacao(combinedSpec, pageable);

        log.info("Busca com filtros múltiplos concluída. Total de produtos: {}", produtosPage.getNumberOfElements());
        return ResponseEntity.ok(produtosPage);
    }


    @GetMapping("/{produtoId}")
    public ResponseEntity<ProdutoDTO> getProdutoDetalhado(@PathVariable(value = "produtoId") UUID produtoId) throws ChangeSetPersister.NotFoundException {
        log.debug("Buscando detalhes do produto. ID: {}", produtoId);

        ProdutoDTO produtoDTO = produtoService.getProdutoDetalhado(produtoId)
                .orElseThrow(ChangeSetPersister.NotFoundException::new);

        log.info("Detalhes do produto {} encontrados com sucesso.", produtoId);

        return ResponseEntity.ok(produtoDTO);
    }

    @GetMapping("/comparar")
    public ResponseEntity<List<ProdutoDTO>> compararProdutos(@RequestParam @NotEmpty List<UUID> ids) {
        log.debug("Requisição para comparação de produtos recebida. IDs: {}", ids);
        List<ProdutoDTO> produtos = produtoService.getProdutosParaComparacao(ids);
        log.info("Comparação de {} produtos concluída.", produtos.size());
        return ResponseEntity.ok(produtos);
    }

    @PostMapping
    public ResponseEntity<ProdutoDTO> criarProduto(@Valid @RequestBody ProdutoDTO produtoDTO) throws ChangeSetPersister.NotFoundException {
        log.info("Requisição para criar novo produto recebida. Nome: {}", produtoDTO.nome());

        ProdutoDTO novoProduto = produtoService.criarProduto(produtoDTO);

        log.info("Produto criado com sucesso. ID: {}", novoProduto.produtoId());

        return ResponseEntity.status(HttpStatus.CREATED).body(novoProduto);
    }

    @PutMapping("/{produtoId}")
    public ResponseEntity<ProdutoDTO> atualizarProduto(
            @PathVariable(value = "produtoId") UUID produtoId,
            @Valid @RequestBody ProdutoDTO produtoDTO)
            throws ChangeSetPersister.NotFoundException {

        log.info("Requisição para atualizar produto. ID: {}", produtoId);

        Optional<ProdutoDTO> produtoAtualizado = produtoService.atualizarProduto(produtoId, produtoDTO);

        log.info("Produto {} atualizado com sucesso.", produtoId);

        return ResponseEntity.ok(produtoAtualizado.get());
    }

    @DeleteMapping("/{produtoId}")
    public ResponseEntity<Object> deletarProduto(@PathVariable(value = "produtoId") UUID produtoId) throws ChangeSetPersister.NotFoundException {
        log.warn("Requisição para deletar produto recebida. ID: {}", produtoId);

        produtoService.deletarProduto(produtoId);

        log.info("Produto {} deletado com sucesso.", produtoId);

        return ResponseEntity.status(HttpStatus.OK).body("Produto deletado com sucesso.");
    }
}