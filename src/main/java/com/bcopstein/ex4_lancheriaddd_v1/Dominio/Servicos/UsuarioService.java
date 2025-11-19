package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Requests.CriarUsuarioRequest;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Requests.EditarUsuarioRequest;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Requests.LoginRequest;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.LoginResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.UsuarioResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Configuracao.CriaJwt;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.UsuarioRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Role;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Usuario;

@Service
public class UsuarioService {

    private final UsuarioRepository repo;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CriaJwt jwt;

    @Autowired
    public UsuarioService(UsuarioRepository repo) {
        this.repo = repo;
    }

    public UsuarioResponse criarUsuario(CriarUsuarioRequest r) {
        String senhaHash = passwordEncoder.encode(r.senha());
        Usuario usuario = new Usuario(
                r.cpf(),
                r.nome(),
                r.celular(),
                r.endereco(),
                r.email(),
                senhaHash,
                Role.valueOf(r.role().toUpperCase())
        );

        repo.salvar(usuario);
        return new UsuarioResponse(0, usuario.getEmail(), usuario.getCpf(), usuario.getCelular(), usuario.getEndereco(), usuario.getRole().name(), usuario.getNome(), null);
    }

    public UsuarioResponse buscarUsuario(String cpf) {
        Usuario u = repo.recuperaPorCpf(cpf);
        if (u == null) {
            return new UsuarioResponse(0, null, null, null, null, null, null, "Usuário não encontrado");
        }

        return new UsuarioResponse(0, u.getEmail(), u.getCpf(), u.getCelular(), u.getEndereco(), u.getRole().name(), u.getNome(), null);
    }

    public Iterable<UsuarioResponse> buscarUsuarios() {
        return StreamSupport.stream(repo.listar().spliterator(), false)
                .map(u -> new UsuarioResponse(0, u.getEmail(), u.getCpf(), u.getCelular(), u.getEndereco(), u.getRole().name(), u.getNome(), null))
                .toList();
    }

    public UsuarioResponse editarUsuario(String cpf, EditarUsuarioRequest r) {
        Usuario u = repo.recuperaPorCpf(cpf);
        if (u == null) {
            return new UsuarioResponse(0, null, null, null, null, null, null, "Usuário não encontrado");
        }

        String senhaHash = passwordEncoder.encode(r.senha());

        u.setRole(Role.valueOf(r.role().toUpperCase()));
        u = new Usuario(cpf, r.nome(), r.celular(), r.endereco(), r.email(), senhaHash, u.getRole());

        repo.editar(u);

        return new UsuarioResponse(0, u.getEmail(), u.getCpf(), u.getCelular(), u.getEndereco(), u.getRole().name(), u.getNome(), null);
    }

    public UsuarioResponse deletarUsuario(String cpf) {
        repo.deletar(cpf);
        return new UsuarioResponse(0, "Deletado", null, null, null, null, null, null);
    }

    public LoginResponse login(LoginRequest r) {
        Usuario u = repo.buscarPorEmail(r.email());

        if (u == null || !passwordEncoder.matches(r.senha(), u.getSenha())) {
            return new LoginResponse(null, "Credenciais inválidas");
        }

        String token = jwt.gerarToken(u.getEmail(), u.getRole().name());

        return new LoginResponse(token, null);
    }
}
