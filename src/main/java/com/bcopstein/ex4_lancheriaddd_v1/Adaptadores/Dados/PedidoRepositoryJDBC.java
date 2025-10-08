package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados;

import java.sql.PreparedStatement;
import java.sql.Statement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.PedidoRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.ItemPedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;

@Repository
public class PedidoRepositoryJDBC implements PedidoRepository {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public PedidoRepositoryJDBC(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public long salvaPedido(Pedido pedido) {
       
        String sqlPedido = "INSERT INTO pedidos (cliente_cpf, data_hora_pagamento, status, valor, impostos, desconto, valor_cobrado) VALUES (?, ?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sqlPedido, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, pedido.getCliente().getCpf());
            ps.setTimestamp(2, pedido.getDataHoraPagamento() != null ? java.sql.Timestamp.valueOf(pedido.getDataHoraPagamento()) : null);
            ps.setString(3, pedido.getStatus().name());
            ps.setDouble(4, pedido.getValor());
            ps.setDouble(5, pedido.getImpostos());
            ps.setDouble(6, pedido.getDesconto());
            ps.setDouble(7, pedido.getValorCobrado());
            return ps;
        }, keyHolder);

        long pedidoId = keyHolder.getKey().longValue();

        String sqlItens = "INSERT INTO itens_pedido (pedido_id, produto_id, quantidade) VALUES (?, ?, ?)";
        for (ItemPedido item : pedido.getItens()) {
            jdbcTemplate.update(sqlItens, pedidoId, item.getItem().getId(), item.getQuantidade());
        }

        return pedidoId;
    }

    @Override
    public int nroPedidosCliente(String cpf) {
        String sql = "SELECT COUNT(*) FROM pedidos WHERE cliente_cpf = ? AND data_hora_pagamento >= CURRENT_DATE - 20";
        Integer count = jdbcTemplate.queryForObject(sql, new Object[]{cpf}, Integer.class);
        return (count != null) ? count : 0;
    }
}