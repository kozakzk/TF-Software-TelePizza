package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Requests.LoginRequest;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.LoginResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.UsuarioService;

@Component
public class LoginUsuarioUC {

    private final UsuarioService usuarioService;

    @Autowired
    public LoginUsuarioUC(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    public LoginResponse run(LoginRequest request) {
        return usuarioService.login(request);
    }
}
