package com.hackerrank.sample.repositories;

import com.hackerrank.sample.models.CategoriaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CategoriaRepository extends JpaRepository<CategoriaModel, UUID> {

}
