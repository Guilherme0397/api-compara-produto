package com.hackerrank.sample.services.impl;

import com.hackerrank.sample.dtos.CategoriaDTO;
import com.hackerrank.sample.models.CategoriaModel;
import com.hackerrank.sample.repositories.CategoriaRepository;
import com.hackerrank.sample.services.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CategoriaServiceImpl implements CategoriaService {
    private final CategoriaRepository categoriaRepository;

    @Autowired
    public CategoriaServiceImpl(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @Override
    public List<CategoriaDTO> listarTodas() {
        return categoriaRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<CategoriaDTO> buscarPorId(UUID id) {
        return categoriaRepository.findById(id).map(this::toDTO);
    }

    @Override
    public CategoriaDTO criarCategoria(CategoriaDTO categoriaDTO) {
        CategoriaModel model = new CategoriaModel();
        model.setNome(categoriaDTO.nome());
        CategoriaModel saved = categoriaRepository.save(model);
        return toDTO(saved);
    }

    @Override
    public void deletarCategoria(UUID id) throws ChangeSetPersister.NotFoundException {
        if (!categoriaRepository.existsById(id)) {
            throw new ChangeSetPersister.NotFoundException();
        }
        categoriaRepository.deleteById(id);
    }

    private CategoriaDTO toDTO(CategoriaModel model) {
        return new CategoriaDTO(model.getIdCategoria(), model.getNome());
    }
}
