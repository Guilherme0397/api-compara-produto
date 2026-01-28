package com.hackerrank.sample.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record EspecificacaoDTO(
        String nomeAtributo,
        String valorAtributo
) {
}

