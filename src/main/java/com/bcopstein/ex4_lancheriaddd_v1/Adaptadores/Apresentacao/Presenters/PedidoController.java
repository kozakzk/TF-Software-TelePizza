package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Apresentacao;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Apresentacao.Presenters.PedidoStatusPresenter;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.RecuperarStatusPedidoUC;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.PedidoStatusResponse;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {
    private final RecuperarStatusPedidoUC recuperarStatusPedidoUC;

    public PedidoController(RecuperarStatusPedidoUC recuperarStatusPedidoUC){
        this.recuperarStatusPedidoUC = recuperarStatusPedidoUC;
    }

    @GetMapping("/{id}/status")
    @CrossOrigin("*")
    public PedidoStatusPresenter recuperaStatus(@PathVariable("id") long id){
        PedidoStatusResponse resp = recuperarStatusPedidoUC.run(id);
        String statusStr = resp.status() == null ? "NAO_ENCONTRADO" : resp.status().name();
        return new PedidoStatusPresenter(resp.id(), statusStr);
    }
}