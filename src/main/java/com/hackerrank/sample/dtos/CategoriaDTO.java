package com.hackerrank.sample.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CategoriaDTO(
        UUID idCategoria,

        @NotBlank(message = "O nome do produto é obrigatório")
        @Size(min = 1, max = 150, message = "O nome deve ter entre 1 e 150 caracteres")
        String nome
) {
}