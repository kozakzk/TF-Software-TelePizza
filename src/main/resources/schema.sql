DROP TABLE IF EXISTS "cardapio_produto" CASCADE;

DROP TABLE IF EXISTS "cardapios" CASCADE;

DROP TABLE IF EXISTS "ingredientes" CASCADE;

DROP TABLE IF EXISTS "itensestoque" CASCADE;

DROP TABLE IF EXISTS "receitas" CASCADE;

DROP TABLE IF EXISTS "receita_ingrediente" CASCADE;

DROP TABLE IF EXISTS "produtos" CASCADE;

DROP TABLE IF EXISTS "produto_receita" CASCADE;

DROP TABLE IF EXISTS "itens_estoque" CASCADE;

DROP TABLE IF EXISTS "clientes" CASCADE;

DROP TABLE IF EXISTS "usuarios" CASCADE;

DROP TABLE IF EXISTS "pedidos" CASCADE;

DROP TABLE IF EXISTS "itens_pedido" CASCADE;

DROP TYPE role_type;

CREATE TYPE role_type AS ENUM ('admin', 'cliente');

-- Tabela de Usuarios
CREATE TABLE IF NOT EXISTS usuarios (
    cpf VARCHAR(15) PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    celular VARCHAR(20) NOT NULL,
    endereco VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    senha VARCHAR(255) NOT NULL,
    role role_type NOT NULL DEFAULT 'cliente'
);

-- Tabela de Ingredientes
CREATE TABLE IF NOT EXISTS ingredientes (
    id BIGSERIAL PRIMARY KEY,
    descricao VARCHAR(255) NOT NULL
);

-- Tabela de Itens de Estoque
CREATE TABLE IF NOT EXISTS itens_estoque (
    id BIGSERIAL PRIMARY KEY,
    quantidade INT NOT NULL,
    ingrediente_id BIGINT REFERENCES ingredientes (id)
);

-- Tabela de Receitas
CREATE TABLE IF NOT EXISTS receitas (
    id BIGSERIAL PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL
);

-- Tabela de Relacionamento Receita x Ingrediente
CREATE TABLE IF NOT EXISTS receita_ingrediente (
    receita_id BIGINT NOT NULL REFERENCES receitas (id),
    ingrediente_id BIGINT NOT NULL REFERENCES ingredientes (id),
    PRIMARY KEY (receita_id, ingrediente_id)
);

-- Tabela de Produtos
CREATE TABLE IF NOT EXISTS produtos (
    id BIGSERIAL PRIMARY KEY,
    descricao VARCHAR(255) NOT NULL,
    preco NUMERIC(10, 2) NOT NULL
);

-- Tabela Produto x Receita
CREATE TABLE IF NOT EXISTS produto_receita (
    produto_id BIGINT NOT NULL REFERENCES produtos (id),
    receita_id BIGINT NOT NULL REFERENCES receitas (id),
    PRIMARY KEY (produto_id, receita_id)
);

-- Tabela de Cardápios
CREATE TABLE IF NOT EXISTS cardapios (
    id BIGSERIAL PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL
);

-- Tabela Cardápio x Produto
CREATE TABLE IF NOT EXISTS cardapio_produto (
    cardapio_id BIGINT NOT NULL REFERENCES cardapios (id),
    produto_id BIGINT NOT NULL REFERENCES produtos (id),
    PRIMARY KEY (cardapio_id, produto_id)
);

-- Tabela de Pedidos
CREATE TABLE IF NOT EXISTS pedidos (
    id BIGSERIAL PRIMARY KEY,
    cliente_cpf VARCHAR(15) NOT NULL REFERENCES usuarios (cpf),
    data_hora_pagamento TIMESTAMP,
    status VARCHAR(50) NOT NULL,
    valor NUMERIC(10, 2) NOT NULL,
    impostos NUMERIC(10, 2) NOT NULL,
    desconto NUMERIC(10, 2) NOT NULL,
    valor_cobrado NUMERIC(10, 2) NOT NULL
);

-- Tabela de Itens de Pedido
CREATE TABLE IF NOT EXISTS itens_pedido (
    pedido_id BIGINT NOT NULL REFERENCES pedidos (id),
    produto_id BIGINT NOT NULL REFERENCES produtos (id),
    quantidade INT NOT NULL,
    PRIMARY KEY (pedido_id, produto_id)
);