package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.PedidoService;

@Component
public class BuscarPedidoPorDataUC {

    private final PedidoService pedidoService;

    @Autowired
    public BuscarPedidoPorDataUC(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    public List<Pedido> run(LocalDateTime dataInicio, LocalDateTime dataFim) {
        return pedidoService.listarPedidosEntregues(dataInicio, dataFim);
    }
}
