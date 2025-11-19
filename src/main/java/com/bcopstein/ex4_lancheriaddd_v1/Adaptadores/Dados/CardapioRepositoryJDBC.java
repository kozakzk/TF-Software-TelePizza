package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.CardapioRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.ProdutosRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.CabecalhoCardapio;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cardapio;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Produto;

@Component
public class CardapioRepositoryJDBC implements CardapioRepository {

    private JdbcTemplate jdbcTemplate;
    private ProdutosRepository produtosRepository;

    @Autowired
    public CardapioRepositoryJDBC(JdbcTemplate jdbcTemplate, ProdutosRepository produtosRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.produtosRepository = produtosRepository;
    }

    @Override
    public Cardapio recuperaPorId(long id) {
        String sql = "SELECT id, titulo, active FROM cardapios WHERE id = ?";
        List<Cardapio> cardapios = this.jdbcTemplate.query(
                sql,
                ps -> ps.setLong(1, id),
                (rs, rowNum) -> new Cardapio(rs.getLong("id"), rs.getString("titulo"), rs.getBoolean("active"), null)
        );
        if (cardapios.isEmpty()) {
            return null;
        }
        Cardapio cardapio = cardapios.getFirst();
        List<Produto> produtos = produtosRepository.recuperaProdutosCardapio(id);
        cardapio.setProdutos(produtos);
        return cardapio;
    }

    @Override
    public List<Produto> indicacoesDoChef() {
        return List.of(produtosRepository.recuperaProdutoPorid(2L));
    }

    @Override
    public List<CabecalhoCardapio> cardapiosDisponiveis() {
        String sql = "SELECT id, titulo, active FROM cardapios";
        List<CabecalhoCardapio> cabCardapios = this.jdbcTemplate.query(
                sql,
                ps -> {
                },
                (rs, rowNum) -> new CabecalhoCardapio(rs.getLong("id"), rs.getString("titulo"), rs.getBoolean("active"))
        );
        return cabCardapios;
    }

    @Override
    public boolean ativaCardapio(long id) {
        String sqlDeactivate = "UPDATE cardapios SET active = FALSE";
        String sqlActivate = "UPDATE cardapios SET active = TRUE WHERE id = ?";
        this.jdbcTemplate.update(sqlDeactivate);
        int rowsAffectedActivate = this.jdbcTemplate.update(sqlActivate, id);
        return rowsAffectedActivate > 0;
    }

    @Override
    public Cardapio recuperaAtivo() {
        String sql = "SELECT id, titulo, active FROM cardapios WHERE active = TRUE";
        List<Cardapio> cardapios = this.jdbcTemplate.query(
                sql,
                ps -> {
                },
                (rs, rowNum) -> new Cardapio(rs.getLong("id"), rs.getString("titulo"), rs.getBoolean("active"), null)
        );
        if (cardapios.isEmpty()) {
            return null;
        }
        Cardapio cardapio = cardapios.get(0);
        List<Produto> produtos = produtosRepository.recuperaProdutosCardapio(cardapio.getId());
        cardapio.setProdutos(produtos);
        return cardapio;
    }
}
