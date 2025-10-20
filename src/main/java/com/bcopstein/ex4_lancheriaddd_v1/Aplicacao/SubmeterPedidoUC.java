package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Requests.SubmeterPedidoRequest;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.PedidoResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.PedidoService;

@Component
public class SubmeterPedidoUC {

    private PedidoService pedidoService;

    @Autowired
    public SubmeterPedidoUC(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    public PedidoResponse run(SubmeterPedidoRequest request) {
        Pedido pedidoCriado = pedidoService.submeterPedido(request);

        return new PedidoResponse(
                pedidoCriado.getId(),
                pedidoCriado.getStatus().name(),
                pedidoCriado.getValor(),
                pedidoCriado.getDesconto(),
                pedidoCriado.getImpostos(),
                pedidoCriado.getValorCobrado(),
                pedidoCriado.getDataHoraPagamento()
        );
    }
}
