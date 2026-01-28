package com.hackerrank.sample.service.impl;

import com.hackerrank.sample.dtos.CategoriaDTO;
import com.hackerrank.sample.dtos.ProdutoDTO;
import com.hackerrank.sample.models.CategoriaModel;
import com.hackerrank.sample.models.ProdutoModel;
import com.hackerrank.sample.repositories.CategoriaRepository;
import com.hackerrank.sample.repositories.ProdutoRepository;
import com.hackerrank.sample.services.MapperService;
import com.hackerrank.sample.services.impl.ProdutoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.crossstore.ChangeSetPersister;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

class ProdutoServiceImplTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private CategoriaRepository categoriaRepository;

    @Mock
    private MapperService mapperService;

    @InjectMocks
    private ProdutoServiceImpl produtoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveRetornarProdutosParaComparacaoComSucesso() {
        // Cenário
        UUID id = UUID.randomUUID();
        ProdutoModel produtoModel = new ProdutoModel();
        ProdutoDTO produtoDTO = new ProdutoDTO(id, "Nome", "url", "desc", BigDecimal.TEN, BigDecimal.valueOf(4.5),
                new CategoriaDTO(UUID.randomUUID(), "Categoria A"), List.of());

        when(produtoRepository.findAllByIdsWithDetails(List.of(id)))
                .thenReturn(List.of(produtoModel));
        when(mapperService.toProdutoDTO(produtoModel)).thenReturn(produtoDTO);

        List<ProdutoDTO> result = produtoService.getProdutosParaComparacao(List.of(id));

        assertEquals(1, result.size());
        assertEquals(id, result.getFirst().produtoId());
    }

    @Test
    void deveRetornarProdutoDetalhadoComSucesso() {
        UUID id = UUID.randomUUID();
        ProdutoModel produtoModel = new ProdutoModel();
        ProdutoDTO produtoDTO = new ProdutoDTO(id, "Nome", "url", "desc", BigDecimal.TEN, BigDecimal.valueOf(4.5),
                new CategoriaDTO(UUID.randomUUID(), "Categoria A"), List.of());

        when(produtoRepository.findById(id)).thenReturn(Optional.of(produtoModel));
        when(mapperService.toProdutoDTO(produtoModel)).thenReturn(produtoDTO);


        Optional<ProdutoDTO> result = produtoService.getProdutoDetalhado(id);

        assertTrue(result.isPresent());
        assertEquals(id, result.get().produtoId());
    }

    @Test
    void deveCriarProdutoComSucesso() throws ChangeSetPersister.NotFoundException {

        UUID categoriaId = UUID.randomUUID();
        CategoriaModel categoriaModel = new CategoriaModel();

        ProdutoDTO produtoDTO = new ProdutoDTO(UUID.randomUUID(), "Nome", "url", "desc", BigDecimal.TEN, BigDecimal.valueOf(4.5),
                new CategoriaDTO(categoriaId, "Categoria"), List.of());
        ProdutoModel produtoModel = new ProdutoModel();
        ProdutoModel savedModel = new ProdutoModel();

        when(categoriaRepository.findById(categoriaId)).thenReturn(Optional.of(categoriaModel));
        when(mapperService.toProdutoModel(produtoDTO, categoriaModel)).thenReturn(produtoModel);
        when(produtoRepository.save(produtoModel)).thenReturn(savedModel);
        when(mapperService.toProdutoDTO(savedModel)).thenReturn(produtoDTO);

        ProdutoDTO result = produtoService.criarProduto(produtoDTO);

        assertEquals(produtoDTO, result);
    }

    @Test
    void deveAtualizarProdutoComSucesso() throws ChangeSetPersister.NotFoundException {
        UUID id = UUID.randomUUID();
        UUID categoriaId = UUID.randomUUID();
        CategoriaModel categoriaModel = new CategoriaModel();
        ProdutoDTO produtoDTO = new ProdutoDTO(id, "Nome Atualizado", "url", "desc", BigDecimal.valueOf(20.0), BigDecimal.valueOf(4.5),
                new CategoriaDTO(categoriaId, "Categoria"), List.of());
        ProdutoModel produtoModel = new ProdutoModel();
        ProdutoModel updatedModel = new ProdutoModel();

        when(produtoRepository.existsById(id)).thenReturn(true);
        when(categoriaRepository.findById(categoriaId)).thenReturn(Optional.of(categoriaModel));
        when(mapperService.toProdutoModel(produtoDTO, categoriaModel)).thenReturn(produtoModel);
        when(produtoRepository.save(produtoModel)).thenReturn(updatedModel);
        when(mapperService.toProdutoDTO(updatedModel)).thenReturn(produtoDTO);

        Optional<ProdutoDTO> result = produtoService.atualizarProduto(id, produtoDTO);

        assertTrue(result.isPresent());
        assertEquals(produtoDTO, result.get());
    }

    @Test
    void deveDeletarProdutoComSucesso() throws ChangeSetPersister.NotFoundException {
        UUID id = UUID.randomUUID();

        when(produtoRepository.existsById(id)).thenReturn(true);

        produtoService.deletarProduto(id);

        verify(produtoRepository, times(1)).deleteById(id);
    }
}