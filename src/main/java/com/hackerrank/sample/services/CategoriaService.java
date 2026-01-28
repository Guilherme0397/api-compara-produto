package com.hackerrank.sample.services;

import com.hackerrank.sample.dtos.CategoriaDTO;
import com.hackerrank.sample.models.CategoriaModel;
import org.springframework.data.crossstore.ChangeSetPersister;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoriaService {

    List<CategoriaDTO> listarTodas();
    Optional<CategoriaDTO> buscarPorId(UUID id);
    CategoriaDTO criarCategoria(CategoriaDTO categoriaDTO);
    void deletarCategoria(UUID id) throws ChangeSetPersister.NotFoundException;
}
