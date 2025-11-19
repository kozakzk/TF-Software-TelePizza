package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Usuario;

public interface UsuarioRepository {

    Usuario recuperaPorCpf(String cpf);

    Usuario buscarPorEmail(String email);

    Iterable<Usuario> listar();

    void salvar(Usuario usuario);

    void editar(Usuario usuario);

    void deletar(String cpf);
}
