package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Requests.ItemPedidoRequest;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Requests.SubmeterPedidoRequest;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.PedidoResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.ConfiguracaoRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.PedidoRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.ProdutosRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.UsuarioRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Produto;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Usuario;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.DescontoClienteFrequente;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.DescontosServicePadrao;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.EstoqueService;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.EstrategiaDesconto;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.ImpostosService;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.PagamentoService;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.PedidoService;

public class ServicosECasosDeUsoTest {

    private PedidoRepository pedidoRepo;
    private UsuarioRepository usuarioRepo;
    private ProdutosRepository produtosRepo;
    private EstoqueService estoqueService;
    private PagamentoService pagamentoService;
    private ImpostosService impostosService;
    private ConfiguracaoRepository configRepo;
    private DescontosServicePadrao descontosService;
    private PedidoService pedidoService;

    @BeforeEach
    void setUp() {
        pedidoRepo = mock(PedidoRepository.class);
        usuarioRepo = mock(UsuarioRepository.class);
        produtosRepo = mock(ProdutosRepository.class);
        estoqueService = mock(EstoqueService.class);
        pagamentoService = mock(PagamentoService.class);
        impostosService = new ImpostosService(); // Usando implementação real pois é lógica pura
        configRepo = mock(ConfiguracaoRepository.class);

        // Configura Map de estratégias
        Map<String, EstrategiaDesconto> estrategias = new HashMap<>();
        estrategias.put("ClienteFrequente", new DescontoClienteFrequente());
        // (Não precisamos adicionar todas para o teste, apenas a que vamos usar ou mockar)
        
        descontosService = new DescontosServicePadrao(pedidoRepo, configRepo, estrategias);
        
        pedidoService = new PedidoService(
            pedidoRepo, usuarioRepo, produtosRepo, 
            estoqueService, descontosService, pagamentoService, impostosService
        );
    }

    // -----------------------------------------------------------------------
    // ITEM 5.c: Testes para classe que implementa o Serviço de Descontos
    // -----------------------------------------------------------------------

    @Test
    void testServicoDescontosPadrao() {
        Usuario usuario = mock(Usuario.class);
        when(usuario.getCpf()).thenReturn("123");
        
        // Caso de Teste: Deve selecionar estratégia 'ClienteFrequente' quando configurado e aplicar regra
        when(configRepo.recuperaConfiguracao("ESTRATEGIA_DESCONTO")).thenReturn("ClienteFrequente");
        when(pedidoRepo.nroPedidosCliente("123")).thenReturn(5); // > 3, ganha desconto
        
        double percentual = descontosService.getPercentualDesconto(usuario);
        
        assertEquals(0.07, percentual, "Deve retornar 7% usando estratégia Frequente configurada");
        
        // Caso de Teste: Fallback para 'ClienteFrequente' se configuração for nula
        when(configRepo.recuperaConfiguracao("ESTRATEGIA_DESCONTO")).thenReturn(null);
        when(pedidoRepo.nroPedidosCliente("123")).thenReturn(2); // <= 3, sem desconto
        
        percentual = descontosService.getPercentualDesconto(usuario);
        assertEquals(0.0, percentual, "Deve usar Frequente como default e retornar 0%");
    }

    // -----------------------------------------------------------------------
    // ITEM 5.e: Testes para classe que implementa caso de uso "Submeter Pedido"
    // -----------------------------------------------------------------------

    @Test
    void testSubmeterPedidoUC() {
        SubmeterPedidoUC useCase = new SubmeterPedidoUC(pedidoService);
        
        // Mocks necessários para o fluxo de sucesso
        when(usuarioRepo.recuperaPorCpf(anyString())).thenReturn(mock(Usuario.class));
        when(produtosRepo.recuperaProdutoPorid(anyLong())).thenReturn(new Produto(1, "Pizza", null, 100));
        when(estoqueService.podeAtender(anyList())).thenReturn(true);
        when(pedidoRepo.salvaPedido(any(Pedido.class))).thenReturn(1L);
        
        // Configuração de desconto para o teste (0%)
        when(configRepo.recuperaConfiguracao("ESTRATEGIA_DESCONTO")).thenReturn("ClienteFrequente");
        
        SubmeterPedidoRequest request = new SubmeterPedidoRequest("123", List.of(new ItemPedidoRequest(1, 1)));
        
        // Caso de Teste: Submissão de pedido válida deve retornar PedidoResponse com valores calculados
        PedidoResponse response = useCase.run(request);
        
        assertNotNull(response);
        assertEquals(100.0, response.subtotal(), "Subtotal deve ser 100");
        assertEquals(10.0, response.impostos(), "Imposto deve ser 10% (10.0)");
        assertEquals(110.0, response.valorFinal(), "Final deve ser 100 + 10 = 110");
        assertEquals("APROVADO", response.status());
    }
}