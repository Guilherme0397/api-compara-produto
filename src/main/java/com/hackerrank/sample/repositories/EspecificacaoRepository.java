package com.hackerrank.sample.repositories;

import com.hackerrank.sample.models.EspecificacaoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EspecificacaoRepository extends JpaRepository<EspecificacaoModel, UUID> {
}
