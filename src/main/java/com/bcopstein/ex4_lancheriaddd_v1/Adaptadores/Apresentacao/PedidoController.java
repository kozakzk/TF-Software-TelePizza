package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Apresentacao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.RecuperarStatusPedidoUC;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Requests.SubmeterPedidoRequest;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.PedidoResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.PedidoStatusResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.SubmeterPedidoUC;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final SubmeterPedidoUC submeterPedidoUC;
    private final RecuperarStatusPedidoUC recuperarStatusPedidoUC;

    @Autowired
    public PedidoController(SubmeterPedidoUC submeterPedidoUC, RecuperarStatusPedidoUC recuperarStatusPedidoUC) {
        this.submeterPedidoUC = submeterPedidoUC;
        this.recuperarStatusPedidoUC = recuperarStatusPedidoUC;
    }

    @PostMapping("/submeter")
    @CrossOrigin("*")
    public PedidoResponse submetePedido(@RequestBody SubmeterPedidoRequest request) {
        return submeterPedidoUC.run(request);
    }

    @GetMapping("/{id}/status")
    @CrossOrigin("*")
    public PedidoStatusResponse recuperaStatus(@PathVariable("id") long id) {
        PedidoStatusResponse resp = recuperarStatusPedidoUC.run(id);
        return new PedidoStatusResponse(resp.id(), resp.status());
    }
}
