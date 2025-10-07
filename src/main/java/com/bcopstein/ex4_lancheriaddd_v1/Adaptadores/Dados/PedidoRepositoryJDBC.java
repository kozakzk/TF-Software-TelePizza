package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class PedidoRepositoryJDBC {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Optional<Pedido> findById(Long id) {
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
                    return Optional.of(pedido);
                }
                return Optional.empty();
            } catch (java.sql.SQLException e) {
                return Optional.empty();
            }
        }, id);
    }
    
    public void delete(Long id) {
        String sql = "DELETE FROM pedidos WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}