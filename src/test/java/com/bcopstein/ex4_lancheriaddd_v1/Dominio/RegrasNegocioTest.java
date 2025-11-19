package com.bcopstein.ex4_lancheriaddd_v1.Dominio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.PedidoRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Usuario;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.DescontoClienteFrequente;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.DescontoClienteGastador;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.ImpostosService;

public class RegrasNegocioTest {

    private PedidoRepository pedidoRepository;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        pedidoRepository = mock(PedidoRepository.class);
        usuario = mock(Usuario.class);
        when(usuario.getCpf()).thenReturn("12345678900");
    }

    // -----------------------------------------------------------------------
    // ITEM 5.a: Testes para classes que implementam estratégias de desconto
    // -----------------------------------------------------------------------

    @Test
    void testDescontoClienteFrequente() {
        DescontoClienteFrequente estrategia = new DescontoClienteFrequente();

        // Caso de Teste: Cliente com mais de 3 pedidos deve receber 7% de desconto
        when(pedidoRepository.nroPedidosCliente(anyString())).thenReturn(4);
        double desconto = estrategia.calcularDesconto(usuario, pedidoRepository);
        assertEquals(0.07, desconto, 0.001, "Deve aplicar 7% de desconto para clientes frequentes");

        // Caso de Teste: Cliente com 3 ou menos pedidos não recebe desconto
        when(pedidoRepository.nroPedidosCliente(anyString())).thenReturn(3);
        desconto = estrategia.calcularDesconto(usuario, pedidoRepository);
        assertEquals(0.0, desconto, 0.001, "Não deve aplicar desconto para clientes não frequentes");
    }

    @Test
    void testDescontoClienteGastador() {
        DescontoClienteGastador estrategia = new DescontoClienteGastador();

        // Caso de Teste: Cliente que gastou mais de R$ 500,00 deve receber 15% de desconto
        when(pedidoRepository.valorTotalGastoCliente(anyString(), eq(30))).thenReturn(500.01);
        double desconto = estrategia.calcularDesconto(usuario, pedidoRepository);
        assertEquals(0.15, desconto, 0.001, "Deve aplicar 15% de desconto para clientes gastadores");

        // Caso de Teste: Cliente que gastou R$ 500,00 ou menos não recebe desconto
        when(pedidoRepository.valorTotalGastoCliente(anyString(), eq(30))).thenReturn(500.00);
        desconto = estrategia.calcularDesconto(usuario, pedidoRepository);
        assertEquals(0.0, desconto, 0.001, "Não deve aplicar desconto se gasto for insuficiente");
    }

    // -----------------------------------------------------------------------
    // ITEM 5.b e 5.d: Testes para classe que implementa cálculo/serviço de impostos
    // -----------------------------------------------------------------------

    @Test
    void testCalculoImpostos() {
        ImpostosService servico = new ImpostosService();

        // Caso de Teste: O imposto deve ser 10% do valor total dos itens
        double valorItens = 100.00;
        double impostoCalculado = servico.calcularImpostos(valorItens);
        
        assertEquals(10.00, impostoCalculado, 0.001, "O imposto deve ser 10% do valor");
    }
}