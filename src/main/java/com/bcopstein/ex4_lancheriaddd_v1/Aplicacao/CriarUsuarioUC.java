package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Requests.CriarUsuarioRequest;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.UsuarioResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.UsuarioService;

@Component
public class CriarUsuarioUC {

    private final UsuarioService usuarioService;

    @Autowired
    public CriarUsuarioUC(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    public UsuarioResponse run(CriarUsuarioRequest request) {
        return usuarioService.criarUsuario(request);
    }
}
