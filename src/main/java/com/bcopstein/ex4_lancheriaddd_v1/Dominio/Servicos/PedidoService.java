package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Requests.SubmeterPedidoRequest;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.ClienteRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.PedidoRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.ProdutosRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.ItemPedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Produto;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final ProdutosRepository produtosRepository;
    private final EstoqueService estoqueService;
    private final DescontosService descontosService;
    private final CozinhaService cozinhaService;

    @Autowired
    public PedidoService(PedidoRepository pedidoRepository, ClienteRepository clienteRepository,
            ProdutosRepository produtosRepository, EstoqueService estoqueService,
            DescontosService descontosService, CozinhaService cozinhaService) {
        this.pedidoRepository = pedidoRepository;
        this.clienteRepository = clienteRepository;
        this.produtosRepository = produtosRepository;
        this.estoqueService = estoqueService;
        this.descontosService = descontosService;
        this.cozinhaService = cozinhaService;
    }

    public Pedido submeterPedido(SubmeterPedidoRequest request) {
        Cliente cliente = clienteRepository.recuperaPorCpf(request.cpfCliente());
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente não encontrado!");
        }

        List<ItemPedido> itens = request.itens().stream()
                .map(itemReq -> {
                    Produto produto = produtosRepository.recuperaProdutoPorid(itemReq.idProduto());
                    if (produto == null) {
                        throw new IllegalArgumentException("Produto não encontrado: " + itemReq.idProduto());
                    }
                    return new ItemPedido(produto, itemReq.quantidade());
                })
                .collect(Collectors.toList());

        boolean estoqueDisponivel = estoqueService.podeAtender(itens);

        if (!estoqueDisponivel) {
            throw new IllegalStateException("Pedido negado por falta de estoque.");
        }

        double subtotal = itens.stream()
                .mapToDouble(item -> item.getItem().getPreco() * item.getQuantidade())
                .sum();

        double percentualDesconto = descontosService.getPercentualDesconto(cliente);
        double desconto = subtotal * percentualDesconto;
        double impostos = subtotal * 0.10;
        double valorFinal = subtotal - desconto + impostos;

        Pedido novoPedido = new Pedido(
                0L,
                cliente,
                null,
                itens,
                Pedido.Status.APROVADO,
                subtotal,
                impostos,
                desconto,
                valorFinal
        );

        long novoPedidoId = pedidoRepository.salvaPedido(novoPedido);
        novoPedido.setId(novoPedidoId);

        cozinhaService.chegadaDePedido(novoPedido);

        return novoPedido;
    }
}
