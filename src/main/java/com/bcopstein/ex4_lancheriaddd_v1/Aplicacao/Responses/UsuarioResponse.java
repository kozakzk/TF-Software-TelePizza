package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses;

public record UsuarioResponse(long id, String email, String cpf, String celular, String endereco, String role, String nome, String error) {

}
