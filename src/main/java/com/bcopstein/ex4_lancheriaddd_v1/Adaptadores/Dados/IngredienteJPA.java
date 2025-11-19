package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados;

import jakarta.persistence.*;

@Entity
@Table(name = "ingredientes")
public class IngredienteJPA {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "descricao", nullable = false)
    private String descricao;

    public IngredienteJPA() {
    }

    public IngredienteJPA(Long id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}

