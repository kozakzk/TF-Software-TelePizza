package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados;

public interface ConfiguracaoRepository {
    String recuperaConfiguracao(String chave);
    void defineConfiguracao(String chave, String valor);
}