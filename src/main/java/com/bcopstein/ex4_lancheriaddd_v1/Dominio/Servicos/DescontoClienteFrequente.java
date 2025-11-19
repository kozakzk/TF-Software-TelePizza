package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import org.springframework.stereotype.Component;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.PedidoRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Usuario;

@Component("ClienteFrequente")
public class DescontoClienteFrequente implements EstrategiaDesconto {
    @Override
    public double calcularDesconto(Usuario usuario, PedidoRepository pedidoRepository) {
        int nroPedidos = pedidoRepository.nroPedidosCliente(usuario.getCpf());
        return (nroPedidos > 3) ? 0.07 : 0.0;
    }
}