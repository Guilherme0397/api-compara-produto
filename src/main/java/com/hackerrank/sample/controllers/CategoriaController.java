package com.hackerrank.sample.controllers;

import com.hackerrank.sample.dtos.CategoriaDTO;
import com.hackerrank.sample.services.CategoriaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {
    private final CategoriaService categoriaService;

    @Autowired
    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping
    public ResponseEntity<List<CategoriaDTO>> listarTodas() {
        List<CategoriaDTO> categorias = categoriaService.listarTodas();
        return ResponseEntity.ok(categorias);
    }

    @GetMapping("/{categoriaId}")
    public ResponseEntity<CategoriaDTO> buscarPorId(@PathVariable(value = "categoriaId") UUID categoriaId) {
        Optional<CategoriaDTO> categoria = categoriaService.buscarPorId(categoriaId);
        return categoria.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CategoriaDTO> criarCategoria(@Valid @RequestBody CategoriaDTO categoriaDTO) {
        CategoriaDTO criada = categoriaService.criarCategoria(categoriaDTO);
        return ResponseEntity.created(URI.create("/api/categorias/" + criada.idCategoria())).body(criada);
    }

    @PutMapping("/{categoriaId}")
    public ResponseEntity<CategoriaDTO> atualizarCategoria(@PathVariable(value = "categoriaId") UUID categoriaId,
                                                           @Valid @RequestBody CategoriaDTO categoriaDTO) throws ChangeSetPersister.NotFoundException {
        Optional<CategoriaDTO> existente = categoriaService.buscarPorId(categoriaId);
        if (existente.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        categoriaService.deletarCategoria(categoriaId);
        CategoriaDTO atualizada = categoriaService.criarCategoria(new CategoriaDTO(categoriaId, categoriaDTO.nome()));
        return ResponseEntity.ok(atualizada);
    }

    @DeleteMapping("/{categoriaId}")
    public ResponseEntity<Void> deletarCategoria(@PathVariable(value = "categoriaId") UUID categoriaId) throws ChangeSetPersister.NotFoundException {
        Optional<CategoriaDTO> existente = categoriaService.buscarPorId(categoriaId);
        if (existente.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        categoriaService.deletarCategoria(categoriaId);
        return ResponseEntity.noContent().build();
    }
}
