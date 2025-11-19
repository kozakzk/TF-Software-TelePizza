package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.CardapioService;

@Component
public class AtivaCardapioUC {

    private CardapioService cardapioService;

    @Autowired
    public AtivaCardapioUC(CardapioService cardapioService) {
        this.cardapioService = cardapioService;
    }

    public boolean run(long idCardapio) {
        boolean ativado = cardapioService.ativaCardapio(idCardapio);
        return ativado;
    }
}
