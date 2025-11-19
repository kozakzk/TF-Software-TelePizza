package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao;

import org.springframework.stereotype.Component;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.ConfiguracaoRepository;

@Component
public class DefinirEstrategiaDescontoUC {
    private final ConfiguracaoRepository repo;

    public DefinirEstrategiaDescontoUC(ConfiguracaoRepository repo) {
        this.repo = repo;
    }

    public void run(String novaEstrategia) {
        if (!novaEstrategia.equals("ClienteFrequente") && !novaEstrategia.equals("ClienteGastador")) {
            throw new IllegalArgumentException("Estratégia inválida. Use 'ClienteFrequente' ou 'ClienteGastador'.");
        }
        repo.defineConfiguracao("ESTRATEGIA_DESCONTO", novaEstrategia);
    }
}