package com.hackerrank.sample.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ProdutoDTO(
        UUID produtoId,

        @NotBlank(message = "O nome do produto é obrigatório")
        @Size(min = 1, max = 150, message = "O nome deve ter entre 1 e 150 caracteres")
        String nome,

        @Size(max = 255, message = "A URL da imagem deve ter no máximo 255 caracteres")
        String urlImagem,

        @NotBlank(message = "A descrição do produto é obrigatória")
        String descricao,

        @NotNull(message = "O preço é obrigatório")
        @DecimalMin(value = "0.01", message = "O preço deve ser maior que 0")
        @Digits(integer = 8, fraction = 2, message = "O preço deve ter no máximo 8 dígitos inteiros e 2 decimais")
        BigDecimal preco,

        @NotNull(message = "A classificação é obrigatória")
        @DecimalMin(value = "0.0", message = "A classificação deve ser maior ou igual a 0")
        @DecimalMax(value = "5.0", message = "A classificação deve ser menor ou igual a 5")
        @Digits(integer = 1, fraction = 2, message = "A classificação deve ter no máximo 1 dígito inteiro e 2 decimais")
        BigDecimal classificacao,

        @NotNull(message = "A categoria é obrigatória.")
        CategoriaDTO categoria,

        List<EspecificacaoDTO> especificacoes
) {
}

