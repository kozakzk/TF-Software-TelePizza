package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;

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

    private final JdbcTemplate jdbcTemplate;

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
            PreparedStatement ps = connection.prepareStatement(sqlPedido, new String[]{"id"});
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

    @Override
    public Pedido recuperaPorId(long id) {
        String sql = "SELECT status, data_hora_pagamento FROM pedidos WHERE id = ?";

        return jdbcTemplate.query(sql, rs -> {
            try {
                if (rs.next()) {
                    Pedido.Status status = Pedido.Status.valueOf(rs.getString("status"));
                    java.sql.Timestamp timestamp = rs.getTimestamp("data_hora_pagamento");
                    Pedido pedido = new Pedido(
                            id,
                            null,
                            timestamp != null ? timestamp.toLocalDateTime() : null,
                            null,
                            status,
                            0, 0, 0, 0
                    );
                    return pedido;
                }
                return null;
            } catch (java.sql.SQLException e) {
                return null;
            }
        }, id);
    }

    @Override
    public Pedido atualizaPedido(Pedido pedido) {
        String sql = "UPDATE pedidos SET data_hora_pagamento = ?, status = ?, valor = ?, impostos = ?, desconto = ?, valor_cobrado = ? WHERE id = ?";
        jdbcTemplate.update(sql,
                pedido.getDataHoraPagamento() != null ? java.sql.Timestamp.valueOf(pedido.getDataHoraPagamento()) : null,
                pedido.getStatus().name(),
                pedido.getValor(),
                pedido.getImpostos(),
                pedido.getDesconto(),
                pedido.getValorCobrado(),
                pedido.getId());
        Pedido recuperado = recuperaPorId(pedido.getId());
        return recuperado;
    }

    @Override
    public List<Pedido> listarPedidosEntregues(LocalDateTime dataInicio, LocalDateTime dataFim) {
        String sql = "SELECT id, data_hora_pagamento, status, valor, impostos, desconto, valor_cobrado FROM pedidos WHERE status = ? AND data_hora_pagamento BETWEEN ? AND ?";
        return jdbcTemplate.query(sql, new Object[]{Pedido.Status.ENTREGUE.name(), java.sql.Timestamp.valueOf(dataInicio), java.sql.Timestamp.valueOf(dataFim)},
                (rs, rowNum) -> {
                    long id = rs.getLong("id");
                    java.sql.Timestamp timestamp = rs.getTimestamp("data_hora_pagamento");
                    Pedido.Status status = Pedido.Status.valueOf(rs.getString("status"));
                    double valor = rs.getDouble("valor");
                    double impostos = rs.getDouble("impostos");
                    double desconto = rs.getDouble("desconto");
                    double valorCobrado = rs.getDouble("valor_cobrado");
                    return new Pedido(id, null, timestamp.toLocalDateTime(), null, status, valor, impostos, desconto, valorCobrado);
                });
    }

    @Override
    public List<Pedido> listarPedidos() {
        String sql = "SELECT id, data_hora_pagamento, status, valor, impostos, desconto, valor_cobrado FROM pedidos";
        return jdbcTemplate.query(sql,
                (rs, rowNum) -> {
                    long id = rs.getLong("id");
                    java.sql.Timestamp timestamp = rs.getTimestamp("data_hora_pagamento");
                    LocalDateTime dataHoraPagamento = null;
                    if (timestamp != null) {
                        dataHoraPagamento = timestamp.toLocalDateTime();
                    }
                    Pedido.Status status = Pedido.Status.valueOf(rs.getString("status"));
                    double valor = rs.getDouble("valor");
                    double impostos = rs.getDouble("impostos");
                    double desconto = rs.getDouble("desconto");
                    double valorCobrado = rs.getDouble("valor_cobrado");
                    return new Pedido(id, null, dataHoraPagamento, null, status, valor, impostos, desconto, valorCobrado);
                });
    }

    @Override
    public double valorTotalGastoCliente(String cpf, int dias) {
        // Soma o valor cobrado de pedidos PAGOS ou ENTREGUES nos últimos X dias
        String sql = "SELECT SUM(valor_cobrado) FROM pedidos WHERE cliente_cpf = ? AND status IN ('PAGO', 'ENTREGUE') AND data_hora_pagamento >= CURRENT_DATE - " + dias;
        
        Double total = jdbcTemplate.queryForObject(sql, Double.class, cpf);
        return (total != null) ? total : 0.0;
    }
}
