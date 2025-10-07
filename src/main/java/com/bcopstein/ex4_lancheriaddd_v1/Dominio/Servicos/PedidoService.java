package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados.PedidoRepositoryJDBC;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PedidoService {
    
    @Autowired
    private PedidoRepositoryJDBC pedidoRepository;
    
    public boolean cancelarPedidoAprovadoNaoPago(Long id) {
        Optional<Pedido> pedidoOpt = pedidoRepository.findById(id);
        
        if (!pedidoOpt.isPresent()) {
            throw new IllegalArgumentException("Pedido com ID " + id + " não encontrado");
        }
        
        Pedido pedido = pedidoOpt.get();
        
        if (pedido.getStatus() == Pedido.Status.APROVADO && pedido.getDataHoraPagamento() == null) {
            pedidoRepository.delete(id);
            return true;
        }
        
        return false;
    }
}