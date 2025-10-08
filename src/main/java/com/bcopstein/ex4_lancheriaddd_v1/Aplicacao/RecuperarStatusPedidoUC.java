package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.PedidoStatusResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.CozinhaService;

@Component
public class RecuperarStatusPedidoUC {
    private final CozinhaService cozinhaService;

    @Autowired
    public RecuperarStatusPedidoUC(CozinhaService cozinhaService){
        this.cozinhaService = cozinhaService;
    }

    public PedidoStatusResponse run(long pedidoId){
        Pedido.Status status = cozinhaService.recuperaStatusPedido(pedidoId);
        return new PedidoStatusResponse(pedidoId, status);
    }
}


