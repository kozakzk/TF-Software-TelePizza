package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados;

import java.time.LocalDateTime;
import java.util.List;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;

public interface PedidoRepository {

    long salvaPedido(Pedido pedido);

    Pedido recuperaPorId(long id);

    Pedido atualizaPedido(Pedido pedido);

    int nroPedidosCliente(String cpf);

    List<Pedido> listarPedidosEntregues(LocalDateTime dataInicio, LocalDateTime dataFim);

    List<Pedido> listarPedidos();

    double valorTotalGastoCliente(String cpf, int dias); 
}
