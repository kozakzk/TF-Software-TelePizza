package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.EstoqueRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Ingrediente;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.ItemEstoque;

@Repository
public class EstoqueRepositoryJPA implements EstoqueRepository {

    private final ItemEstoqueRepositoryJPA itemEstoqueRepositoryJPA;

    @Autowired
    public EstoqueRepositoryJPA(ItemEstoqueRepositoryJPA itemEstoqueRepositoryJPA) {
        this.itemEstoqueRepositoryJPA = itemEstoqueRepositoryJPA;
    }

    @Override
    public List<ItemEstoque> recuperaTodos() {
        return itemEstoqueRepositoryJPA.findAll().stream()
                .map(this::converterParaItemEstoque)
                .collect(Collectors.toList());
    }

    @Override
    public ItemEstoque recuperaPorIngredienteId(long ingredienteId) {
        Optional<ItemEstoqueJPA> itemEstoqueJPA = itemEstoqueRepositoryJPA.findByIngredienteId(ingredienteId);
        return itemEstoqueJPA.map(this::converterParaItemEstoque).orElse(null);
    }

    @Override
    public void atualizaQuantidade(long ingredienteId, int quantidade) {
        Optional<ItemEstoqueJPA> itemEstoqueJPA = itemEstoqueRepositoryJPA.findByIngredienteId(ingredienteId);
        if (itemEstoqueJPA.isPresent()) {
            ItemEstoqueJPA item = itemEstoqueJPA.get();
            item.setQuantidade(quantidade);
            itemEstoqueRepositoryJPA.save(item);
        } else {
            throw new IllegalArgumentException("Item de estoque não encontrado para o ingrediente ID: " + ingredienteId);
        }
    }

    private ItemEstoque converterParaItemEstoque(ItemEstoqueJPA itemEstoqueJPA) {
        IngredienteJPA ingredienteJPA = itemEstoqueJPA.getIngrediente();
        Ingrediente ingrediente = new Ingrediente(ingredienteJPA.getId(), ingredienteJPA.getDescricao());
        ItemEstoque itemEstoque = new ItemEstoque(ingrediente, itemEstoqueJPA.getQuantidade());
        return itemEstoque;
    }
}

