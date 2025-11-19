package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.ConfiguracaoRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.PedidoRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Usuario;

@Service
public class DescontosServicePadrao implements DescontosService {
    private final PedidoRepository pedidoRepository;
    private final ConfiguracaoRepository configuracaoRepository;
    private final Map<String, EstrategiaDesconto> estrategias;

    @Autowired
    public DescontosServicePadrao(PedidoRepository pedidoRepository,
                                  ConfiguracaoRepository configuracaoRepository,
                                  Map<String, EstrategiaDesconto> estrategias) {
        this.pedidoRepository = pedidoRepository;
        this.configuracaoRepository = configuracaoRepository;
        this.estrategias = estrategias;
    }

    @Override
    public double getPercentualDesconto(Usuario cliente) {
        // 1. Descobre qual estratégia está ativa no banco
        String estrategiaAtiva = configuracaoRepository.recuperaConfiguracao("ESTRATEGIA_DESCONTO");
        
        if (estrategiaAtiva == null) {
            estrategiaAtiva = "ClienteFrequente"; // Fallback padrão
        }

        // 2. Seleciona a implementação correta do Map
        // O Spring cria um Map onde a chave é o nome do componente (ex: "ClienteGastador")
        EstrategiaDesconto estrategia = estrategias.get(estrategiaAtiva);

        if (estrategia == null) {
             // Caso configurem um nome errado no banco, usa o padrão
             estrategia = estrategias.get("ClienteFrequente");
        }

        // 3. Calcula
        return estrategia.calcularDesconto(cliente, pedidoRepository);
    }
}