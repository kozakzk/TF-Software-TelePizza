package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses;

import java.time.LocalDateTime;

public record PedidoResponse(
        long idPedido,
        String status,
        double subtotal,
        double desconto,
        double impostos,
        double valorFinal,
        LocalDateTime dataHoraPagamento
        ) {

}
