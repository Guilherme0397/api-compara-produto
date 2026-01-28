package com.hackerrank.sample.specifications;

import com.hackerrank.sample.models.EspecificacaoModel;
import com.hackerrank.sample.models.ProdutoModel;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.GreaterThanOrEqual;
import net.kaczmarzyk.spring.data.jpa.domain.LessThanOrEqual;
import net.kaczmarzyk.spring.data.jpa.domain.LikeIgnoreCase;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class SpecificationTemplate {

    @And({
            @Spec(path = "nome", spec = LikeIgnoreCase.class),
            @Spec(path = "classificacao", spec = Equal.class, params = "classificacaIgual"),
            @Spec(path = "classificacao", spec = GreaterThanOrEqual.class, params = "classificacaoMinima"),
            @Spec(path = "classificacao", spec = LessThanOrEqual.class, params = "classificacaoMaxima"),
            @Spec(path = "preco", spec = Equal.class, params = "precoIgual"),
            @Spec(path = "preco", spec = LessThanOrEqual.class, params = "precoMaximo"),
            @Spec(path = "preco", spec = GreaterThanOrEqual.class, params = "precoMinimo"),
    })
    public interface ProdutoSpec extends Specification<ProdutoModel> {}

    public static Specification<ProdutoModel> produtosDaCategoria(final UUID categoryId) {
        return (root, query, cb) -> {
            return cb.equal(root.get("categoria").get("id"), categoryId);
        };
    }

    public static Specification<ProdutoModel> temEspecificacao(final String attributeName, final String attributeValue) {
        return (root, query, cb) -> {
            query.distinct(true);

            Join<ProdutoModel, EspecificacaoModel> specsJoin = root.join("especificacoes", JoinType.INNER);

            return cb.and(
                    cb.equal(specsJoin.get("nomeAtributo"), attributeName),
                    cb.equal(specsJoin.get("valorAtributo"), attributeValue)
            );
        };
    }

}