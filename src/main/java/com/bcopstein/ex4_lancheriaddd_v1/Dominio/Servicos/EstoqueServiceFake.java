package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import java.util.List;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.ItemPedido;

// @Service - Desabilitado: serviço fake substituído por EstoqueServiceImpl
public class EstoqueServiceFake implements EstoqueService {

    @Override
    public boolean podeAtender(List<ItemPedido> itens) {
        // Versão Fake: Sempre retorna que o estoque é suficiente.
        System.out.println("Serviço de Domínio (FAKE): Verificando estoque -> Suficiente!");
        return true;
    }
}