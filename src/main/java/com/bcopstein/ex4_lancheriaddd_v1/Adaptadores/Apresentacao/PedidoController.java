package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Apresentacao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.BuscarPedidoPorDataUC;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.BuscarPedidosUC;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.CancelarPedidoUC;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.EntregarPedidoUC;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.PagarPedidoUC;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.RecuperarStatusPedidoUC;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Requests.PedidoPorDataRequest;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Requests.SubmeterPedidoRequest;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.PedidoResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.PedidoStatusResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.SubmeterPedidoUC;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final SubmeterPedidoUC submeterPedidoUC;
    private final RecuperarStatusPedidoUC recuperarStatusPedidoUC;
    private final CancelarPedidoUC cancelarPedidoUC;
    private final PagarPedidoUC pagarPedidoUC;
    private final BuscarPedidoPorDataUC buscarPedidoPorDataUC;
    private final BuscarPedidosUC buscarPedidosUC;
    private final EntregarPedidoUC entregarPedidoUC;

    @Autowired
    public PedidoController(SubmeterPedidoUC submeterPedidoUC, RecuperarStatusPedidoUC recuperarStatusPedidoUC, CancelarPedidoUC cancelarPedidoUC, PagarPedidoUC pagarPedidoUC, BuscarPedidoPorDataUC buscarPedidoPorDataUC, BuscarPedidosUC buscarPedidosUC, EntregarPedidoUC entregarPedidoUC) {
        this.submeterPedidoUC = submeterPedidoUC;
        this.recuperarStatusPedidoUC = recuperarStatusPedidoUC;
        this.cancelarPedidoUC = cancelarPedidoUC;
        this.pagarPedidoUC = pagarPedidoUC;
        this.buscarPedidoPorDataUC = buscarPedidoPorDataUC;
        this.entregarPedidoUC = entregarPedidoUC;
        this.buscarPedidosUC = buscarPedidosUC;
    }

    @PostMapping("/submeter")
    @CrossOrigin("*")
    public PedidoResponse submetePedido(@RequestBody SubmeterPedidoRequest request) {
        return submeterPedidoUC.run(request);
    }

    @GetMapping("/{id}/status")
    @CrossOrigin("*")
    public PedidoStatusResponse recuperaStatus(@PathVariable("id") long id) {
        try {
            PedidoStatusResponse resp = recuperarStatusPedidoUC.run(id);
            return new PedidoStatusResponse(resp.id(), resp.status(), null);
        } catch (Exception e) {
            System.out.println("Erro ao recuperar status do pedido: " + e.getMessage());
            return new PedidoStatusResponse(id, null, e.getMessage());
        }
    }

    @PatchMapping("/{id}/cancelar")
    public PedidoStatusResponse cancelarPedido(@PathVariable Long id) {
        try {
            return cancelarPedidoUC.run(id);
        } catch (Exception e) {
            System.out.println("Erro ao cancelar pedido: " + e.getMessage());
            return new PedidoStatusResponse(id, null, e.getMessage());
        }
    }

    @PatchMapping("/{id}/pagar")
    @CrossOrigin("*")
    public PedidoStatusResponse pagarPedido(@PathVariable Long id) {
        try {
            return pagarPedidoUC.run(id);
        } catch (Exception e) {
            System.out.println("Erro ao pagar pedido: " + e.getMessage());
            return new PedidoStatusResponse(id, null, e.getMessage());
        }
    }

    @GetMapping("/byDate")
    @CrossOrigin("*")
    public Iterable<PedidoResponse> listarPedidos(@RequestBody PedidoPorDataRequest request) {
        var pedidos = buscarPedidoPorDataUC.run(request.dataInicio(), request.dataFim());
        var responses = pedidos.stream()
                .map(p -> new PedidoResponse(
                p.getId(),
                p.getStatus().toString(),
                p.getValor(),
                p.getDesconto(),
                p.getImpostos(),
                p.getValorCobrado(),
                p.getDataHoraPagamento()
        ))
                .toList();
        return responses;
    }

    @GetMapping("/all")
    @CrossOrigin("*")
    public Iterable<PedidoResponse> listarTodosPedidos() {
        var pedidos = buscarPedidosUC.run();
        var responses = pedidos.stream()
                .map(p -> new PedidoResponse(
                p.getId(),
                p.getStatus().toString(),
                p.getValor(),
                p.getDesconto(),
                p.getImpostos(),
                p.getValorCobrado(),
                p.getDataHoraPagamento()
        ))
                .toList();
        return responses;
    }

    @PatchMapping("/{id}/entregar")
    @CrossOrigin("*")
    public PedidoStatusResponse entregarPedido(@PathVariable Long id) {
        try {
            return entregarPedidoUC.run(id);
        } catch (Exception e) {
            System.out.println("Erro ao entregar pedido: " + e.getMessage());
            return new PedidoStatusResponse(id, null, e.getMessage());
        }
    }
}
