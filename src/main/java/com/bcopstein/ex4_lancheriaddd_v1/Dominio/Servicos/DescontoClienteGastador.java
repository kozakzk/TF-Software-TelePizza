package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import org.springframework.stereotype.Component;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.PedidoRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Usuario;

@Component("ClienteGastador")
public class DescontoClienteGastador implements EstrategiaDesconto {
    @Override
    public double calcularDesconto(Usuario usuario, PedidoRepository pedidoRepository) {
        double totalGasto = pedidoRepository.valorTotalGastoCliente(usuario.getCpf(), 30);
        return (totalGasto > 500.00) ? 0.15 : 0.0;
    }
}