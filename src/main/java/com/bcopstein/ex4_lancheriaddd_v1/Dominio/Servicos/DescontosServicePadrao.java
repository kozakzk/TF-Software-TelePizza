package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.PedidoRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Usuario;

@Service
public class DescontosServicePadrao implements DescontosService {

    private PedidoRepository pedidoRepository;

    @Autowired
    public DescontosServicePadrao(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    @Override
    public double getPercentualDesconto(Usuario cliente) {
        int nroPedidos = pedidoRepository.nroPedidosCliente(cliente.getCpf());
        if (nroPedidos > 3) {
            return 0.07;
        }
        return 0.0;
    }
}
