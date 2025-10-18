package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import org.springframework.stereotype.Service;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;

@Service
public class PagamentoServiceFake implements PagamentoService {
    
    @Override
    public boolean efetuaPagamento(Pedido pedido) {
        //Fake = todo pagamento é bem sucedido
        System.out.println("Serviço de Domínio (FAKE): Efetuando pagamento para o pedido " + pedido.getId() + " -> Aprovado!");
        return true;
    }
}