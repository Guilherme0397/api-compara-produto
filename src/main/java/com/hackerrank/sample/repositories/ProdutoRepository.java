package com.hackerrank.sample.repositories;

import com.hackerrank.sample.models.ProdutoModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ProdutoRepository extends JpaRepository<ProdutoModel, UUID>, JpaSpecificationExecutor<ProdutoModel> {

    @Query("SELECT DISTINCT p FROM ProdutoModel p LEFT JOIN FETCH p.especificacoes s LEFT JOIN FETCH p.categoria c WHERE p.id IN (:ids)")
    List<ProdutoModel> findAllByIdsWithDetails(@Param("ids") List<UUID> ids);
}

