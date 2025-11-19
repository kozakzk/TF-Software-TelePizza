package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.ConfiguracaoRepository;

@Repository
public class ConfiguracaoRepositoryJDBC implements ConfiguracaoRepository {
    private final JdbcTemplate jdbcTemplate;

    public ConfiguracaoRepositoryJDBC(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public String recuperaConfiguracao(String chave) {
        String sql = "SELECT valor FROM configuracoes WHERE chave = ?";
        try {
            return jdbcTemplate.queryForObject(sql, String.class, chave);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void defineConfiguracao(String chave, String valor) {
        String sql = "INSERT INTO configuracoes (chave, valor) VALUES (?, ?) " +
                     "ON CONFLICT (chave) DO UPDATE SET valor = ?";
        jdbcTemplate.update(sql, chave, valor, valor);
    }
}