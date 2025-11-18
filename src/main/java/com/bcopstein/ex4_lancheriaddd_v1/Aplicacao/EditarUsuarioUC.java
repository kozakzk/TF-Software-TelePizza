package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Requests.EditarUsuarioRequest;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.UsuarioResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.UsuarioService;

@Component
public class EditarUsuarioUC {

    private final UsuarioService usuarioService;

    @Autowired
    public EditarUsuarioUC(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    public UsuarioResponse run(String cpf, EditarUsuarioRequest request) {
        return usuarioService.editarUsuario(cpf, request);
    }
}
