package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Apresentacao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.BuscarTodosUsuariosUC;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.BuscarUsuarioUC;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.CriarUsuarioUC;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.DeletarUsuarioUC;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.EditarUsuarioUC;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.LoginUsuarioUC;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Requests.CriarUsuarioRequest;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Requests.EditarUsuarioRequest;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Requests.LoginRequest;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.LoginResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.UsuarioResponse;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin("*")
public class UsuarioController {

    private final CriarUsuarioUC criarUsuarioUC;
    private final EditarUsuarioUC editarUsuarioUC;
    private final BuscarUsuarioUC buscarUsuarioUC;
    private final BuscarTodosUsuariosUC buscarTodosUsuariosUC;
    private final DeletarUsuarioUC deletarUsuarioUC;
    private final LoginUsuarioUC loginUsuarioUC;

    @Autowired
    public UsuarioController(
            CriarUsuarioUC criarUsuarioUC,
            EditarUsuarioUC editarUsuarioUC,
            BuscarUsuarioUC buscarUsuarioUC,
            BuscarTodosUsuariosUC buscarTodosUsuariosUC,
            DeletarUsuarioUC deletarUsuarioUC,
            LoginUsuarioUC loginUsuarioUC
    ) {
        this.criarUsuarioUC = criarUsuarioUC;
        this.editarUsuarioUC = editarUsuarioUC;
        this.buscarUsuarioUC = buscarUsuarioUC;
        this.buscarTodosUsuariosUC = buscarTodosUsuariosUC;
        this.deletarUsuarioUC = deletarUsuarioUC;
        this.loginUsuarioUC = loginUsuarioUC;
    }

    @PostMapping
    public UsuarioResponse criar(@RequestBody CriarUsuarioRequest request) {
        try {
            return criarUsuarioUC.run(request);
        } catch (Exception e) {
            return new UsuarioResponse(0, null, null, null, null, null, null, e.getMessage());
        }
    }

    @GetMapping("/{cpf}")
    public UsuarioResponse buscar(@PathVariable String cpf) {
        try {
            return buscarUsuarioUC.run(cpf);
        } catch (Exception e) {
            return new UsuarioResponse(0, null, null, null, null, null, null, e.getMessage());
        }
    }

    @GetMapping("/all")
    public Iterable<UsuarioResponse> listarTodos() {
        return buscarTodosUsuariosUC.run();
    }

    @PatchMapping("/{cpf}")
    public UsuarioResponse editar(@PathVariable String cpf, @RequestBody EditarUsuarioRequest request) {
        try {
            return editarUsuarioUC.run(cpf, request);
        } catch (Exception e) {
            return new UsuarioResponse(0, null, null, null, null, null, null, e.getMessage());
        }
    }

    @DeleteMapping("/{cpf}")
    public String deletar(@PathVariable String cpf) {
        try {
            deletarUsuarioUC.run(cpf);
            return "Usuário deletado com sucesso.";
        } catch (Exception e) {
            return "Erro ao deletar usuário: " + e.getMessage();
        }
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        try {
            return loginUsuarioUC.run(request);
        } catch (Exception e) {
            return new LoginResponse(null, "Erro: " + e.getMessage());
        }
    }
}
