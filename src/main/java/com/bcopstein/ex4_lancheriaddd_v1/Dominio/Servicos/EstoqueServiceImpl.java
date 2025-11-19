package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.EstoqueRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.IngredientesRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Ingrediente;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.ItemEstoque;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.ItemPedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Produto;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Receita;

@Service
public class EstoqueServiceImpl implements EstoqueService {

    private final EstoqueRepository estoqueRepository;
    private final IngredientesRepository ingredientesRepository;

    @Autowired
    public EstoqueServiceImpl(EstoqueRepository estoqueRepository, IngredientesRepository ingredientesRepository) {
        this.estoqueRepository = estoqueRepository;
        this.ingredientesRepository = ingredientesRepository;
    }

    @Override
    public boolean podeAtender(List<ItemPedido> itens) {
        Map<Long, Integer> ingredientesNecessarios = new HashMap<>();

        for (ItemPedido itemPedido : itens) {
            Produto produto = itemPedido.getItem();
            int quantidadeProduto = itemPedido.getQuantidade();
            Receita receita = produto.getReceita();

            if (receita == null) {
                System.out.println("Produto " + produto.getId() + " não possui receita!");
                return false;
            }

            List<Ingrediente> ingredientesReceita = receita.getIngredientes();
            for (Ingrediente ingrediente : ingredientesReceita) {
                long ingredienteId = ingrediente.getId();
                int quantidadeNecessaria = quantidadeProduto;
                ingredientesNecessarios.merge(ingredienteId, quantidadeNecessaria, Integer::sum);
            }
        }

        for (Map.Entry<Long, Integer> entry : ingredientesNecessarios.entrySet()) {
            long ingredienteId = entry.getKey();
            int quantidadeNecessaria = entry.getValue();

            ItemEstoque itemEstoque = estoqueRepository.recuperaPorIngredienteId(ingredienteId);
            if (itemEstoque == null) {
                System.out.println("Ingrediente ID " + ingredienteId + " não encontrado no estoque!");
                return false;
            }

            int quantidadeDisponivel = itemEstoque.getQuantidade();
            if (quantidadeDisponivel < quantidadeNecessaria) {
                System.out.println("Estoque insuficiente para ingrediente ID " + ingredienteId
                        + ". Necessário: " + quantidadeNecessaria
                        + ", Disponível: " + quantidadeDisponivel);
                return false;
            }
        }

        System.out.println("Serviço de Domínio: Verificando estoque -> Suficiente!");
        return true;
    }
}
