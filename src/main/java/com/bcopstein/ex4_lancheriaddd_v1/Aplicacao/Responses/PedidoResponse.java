package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses;

public record PedidoResponse(
    long idPedido,
    String status,
    double subtotal,
    double desconto,
    double impostos,
    double valorFinal
) {}