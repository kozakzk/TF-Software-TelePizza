package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ItemEstoqueRepositoryJPA extends JpaRepository<ItemEstoqueJPA, Long> {
    Optional<ItemEstoqueJPA> findByIngredienteId(Long ingredienteId);
}

