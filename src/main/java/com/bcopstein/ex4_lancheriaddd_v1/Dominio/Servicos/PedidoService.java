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

    @Autowired
    public PedidoService(PedidoRepository pedidoRepository, ClienteRepository clienteRepository, 
                         ProdutosRepository produtosRepository, EstoqueService estoqueService,
                         DescontosService descontosService) {
        this.pedidoRepository = pedidoRepository;
        this.clienteRepository = clienteRepository;
        this.produtosRepository = produtosRepository;
        this.estoqueService = estoqueService;
        this.descontosService = descontosService;
    }

    public Pedido submeterPedido(SubmeterPedidoRequest request) {
        // 1. Recupera o cliente a partir do CPF
        Cliente cliente = clienteRepository.recuperaPorCpf(request.cpfCliente());
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente não encontrado!");
        }

        // 2. Converte os itens da requisição para itens de domínio (com o objeto Produto completo)
        List<ItemPedido> itens = request.itens().stream()
                .map(itemReq -> {
                    Produto produto = produtosRepository.recuperaProdutoPorid(itemReq.idProduto());
                    if (produto == null) {
                        throw new IllegalArgumentException("Produto não encontrado: " + itemReq.idProduto());
                    }
                    return new ItemPedido(produto, itemReq.quantidade());
                })
                .collect(Collectors.toList());

        // 3. Lógica de Negócio: Verificação de Estoque
        boolean estoqueDisponivel = estoqueService.podeAtender(itens);
        
        if (!estoqueDisponivel) {
             // CORREÇÃO: Em vez de retornar uma String, lançamos uma exceção para indicar a falha.
             // O nosso EstoqueService (Fake) nunca deixará este trecho ser executado por enquanto.
             throw new IllegalStateException("Pedido negado por falta de estoque.");
        }

        // 4. Lógica de Negócio: Cálculo de Custos
        double subtotal = itens.stream()
            .mapToDouble(item -> item.getItem().getPreco() * item.getQuantidade())
            .sum();

        double percentualDesconto = descontosService.getPercentualDesconto(cliente);
        double desconto = subtotal * percentualDesconto;
        double impostos = subtotal * 0.10; // Imposto fixo de 10%
        double valorFinal = subtotal - desconto + impostos;
        
        // 5. Cria a entidade de domínio Pedido
        Pedido novoPedido = new Pedido(
            0L, // ID temporário, será gerado pelo banco
            cliente,
            null, // Sem data de pagamento ainda
            itens,
            Pedido.Status.APROVADO, // Status definido após verificação de estoque
            subtotal,
            impostos,
            desconto,
            valorFinal
        );

        // 6. Persiste o pedido no banco de dados
        long novoPedidoId = pedidoRepository.salvaPedido(novoPedido);
        // Atualiza o objeto com o ID real gerado pelo banco
        novoPedido.setId(novoPedidoId);

        return novoPedido;
    }
}