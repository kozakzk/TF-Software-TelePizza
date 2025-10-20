package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Requests;

import java.time.LocalDateTime;

public record PedidoPorDataRequest(
        LocalDateTime dataInicio,
        LocalDateTime dataFim
        ) {

}
