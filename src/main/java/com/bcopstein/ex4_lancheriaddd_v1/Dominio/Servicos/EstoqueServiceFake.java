package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import java.util.List;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.ItemPedido;

public class EstoqueServiceFake implements EstoqueService {

    @Override
    public boolean podeAtender(List<ItemPedido> itens) {
        System.out.println("Serviço de Domínio (FAKE): Verificando estoque -> Suficiente!");
        return true;
    }
}
