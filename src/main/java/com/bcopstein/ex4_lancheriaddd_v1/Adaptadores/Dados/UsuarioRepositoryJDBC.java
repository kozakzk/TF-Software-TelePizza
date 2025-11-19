package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados;

import org.springframework.stereotype.Repository;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.UsuarioRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Usuario;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Role;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.dao.EmptyResultDataAccessException;

@Repository
public class UsuarioRepositoryJDBC implements UsuarioRepository {

    private final JdbcTemplate jdbc;

    public UsuarioRepositoryJDBC(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public Usuario recuperaPorCpf(String cpf) {
        String sql = """
                SELECT cpf, nome, celular, endereco, email, senha, role
                FROM usuarios
                WHERE cpf = ?
                """;

        try {
            return jdbc.queryForObject(sql,
                    (rs, rowNum) -> new Usuario(
                            rs.getString("cpf"),
                            rs.getString("nome"),
                            rs.getString("celular"),
                            rs.getString("endereco"),
                            rs.getString("email"),
                            rs.getString("senha"),
                            Role.valueOf(rs.getString("role"))
                    ),
                    cpf);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public void salvar(Usuario usuario) {
        String sql = """
                INSERT INTO usuarios (cpf, nome, celular, endereco, email, senha, role)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        jdbc.update(sql,
                usuario.getCpf(),
                usuario.getNome(),
                usuario.getCelular(),
                usuario.getEndereco(),
                usuario.getEmail(),
                usuario.getSenha(),
                usuario.getRole().name());
    }

    @Override
    public void editar(Usuario usuario) {
        String sql = """
                UPDATE usuarios
                SET nome = ?, celular = ?, endereco = ?, email = ?, role = ?
                WHERE cpf = ?
                """;

        jdbc.update(sql,
                usuario.getNome(),
                usuario.getCelular(),
                usuario.getEndereco(),
                usuario.getEmail(),
                usuario.getRole().name(),
                usuario.getCpf());
    }

    @Override
    public void deletar(String cpf) {
        jdbc.update("DELETE FROM usuarios WHERE cpf = ?", cpf);
    }

    @Override
    public Iterable<Usuario> listar() {
        String sql = """
                SELECT cpf, nome, celular, endereco, email, senha, role
                FROM usuarios
                """;

        return jdbc.query(sql,
                (rs, rowNum) -> new Usuario(
                        rs.getString("cpf"),
                        rs.getString("nome"),
                        rs.getString("celular"),
                        rs.getString("endereco"),
                        rs.getString("email"),
                        rs.getString("senha"),
                        Role.valueOf(rs.getString("role"))
                ));
    }

    @Override
    public Usuario buscarPorEmail(String email) {
        String sql = """
                SELECT cpf, nome, celular, endereco, email, senha, role
                FROM usuarios
                WHERE email = ?
                """;

        try {
            return jdbc.queryForObject(sql,
                    (rs, rowNum) -> new Usuario(
                            rs.getString("cpf"),
                            rs.getString("nome"),
                            rs.getString("celular"),
                            rs.getString("endereco"),
                            rs.getString("email"),
                            rs.getString("senha"),
                            Role.valueOf(rs.getString("role"))
                    ),
                    email);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
