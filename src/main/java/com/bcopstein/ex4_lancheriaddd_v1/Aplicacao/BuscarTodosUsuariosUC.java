package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.UsuarioResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.UsuarioService;

@Component
public class BuscarTodosUsuariosUC {

    private final UsuarioService usuarioService;

    @Autowired
    public BuscarTodosUsuariosUC(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    public Iterable<UsuarioResponse> run() {
        return usuarioService.buscarUsuarios();
    }
}
