package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Apresentacao;

import org.springframework.web.bind.annotation.*;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.DefinirEstrategiaDescontoUC;

@RestController
@RequestMapping("/configuracoes")
public class ConfiguracaoController {
    private final DefinirEstrategiaDescontoUC definirEstrategiaUC;

    public ConfiguracaoController(DefinirEstrategiaDescontoUC definirEstrategiaUC) {
        this.definirEstrategiaUC = definirEstrategiaUC;
    }

    @PatchMapping("/desconto")
    public String definirEstrategiaDesconto(@RequestParam String estrategia) {
        try {
            definirEstrategiaUC.run(estrategia);
            return "Estratégia de desconto atualizada para: " + estrategia;
        } catch (IllegalArgumentException e) {
            return "Erro: " + e.getMessage();
        }
    }
}