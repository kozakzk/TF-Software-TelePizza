-- Inserção dos clientes
INSERT INTO
    clientes (
        cpf,
        nome,
        celular,
        endereco,
        email
    )
VALUES (
        '9001',
        'Huguinho Pato',
        '51985744566',
        'Rua das Flores, 100',
        'huguinho.pato@email.com'
    ),
    (
        '9002',
        'Luizinho Pato',
        '5199172079',
        'Av. Central, 200',
        'zezinho.pato@email.com'
    );

-- Inserção dos ingredientes
INSERT INTO
    ingredientes (descricao)
VALUES ('Disco de pizza'),
    ('Porcao de tomate'),
    ('Porcao de mussarela'),
    ('Porcao de presunto'),
    ('Porcao de calabresa'),
    ('Molho de tomate (200ml)'),
    ('Porcao de azeitona'),
    ('Porcao de oregano'),
    ('Porcao de cebola');

-- Inserção dos itens de estoque
INSERT INTO
    itens_estoque (quantidade, ingrediente_id)
VALUES (30, 1),
    (30, 2),
    (30, 3),
    (30, 4),
    (30, 5),
    (30, 6),
    (30, 7),
    (30, 8),
    (30, 9);

-- Inserção das receitas
INSERT INTO
    receitas (titulo)
VALUES ('Pizza calabresa'),
    ('Pizza queijo e presunto'),
    ('Pizza margherita');

-- Associação dos ingredientes às receitas
INSERT INTO
    receita_ingrediente (receita_id, ingrediente_id)
VALUES
    -- Pizza calabresa
    (1, 1),
    (1, 6),
    (1, 3),
    (1, 5),
    -- Pizza queijo e presunto
    (2, 1),
    (2, 6),
    (2, 3),
    (2, 4),
    -- Pizza margherita
    (3, 1),
    (3, 6),
    (3, 3),
    (3, 8);

-- Inserção dos produtos
INSERT INTO
    produtos (descricao, preco)
VALUES ('Pizza calabresa', 55.00),
    (
        'Pizza queijo e presunto',
        60.00
    ),
    ('Pizza margherita', 40.00);

-- Associação dos produtos às receitas
INSERT INTO
    produto_receita (produto_id, receita_id)
VALUES (1, 1),
    (2, 2),
    (3, 3);

-- Inserção dos cardápios
INSERT INTO
    cardapios (titulo)
VALUES ('Cardapio de Agosto'),
    ('Cardapio de Setembro');

-- Associação dos cardápios com os produtos
INSERT INTO
    cardapio_produto (cardapio_id, produto_id)
VALUES (1, 1),
    (1, 2),
    (1, 3),
    (2, 1),
    (2, 3);

-- Pedidos
INSERT INTO
    pedidos (
        cliente_cpf,
        status,
        valor,
        impostos,
        desconto,
        valor_cobrado
    )
VALUES (
        '9001',
        'NOVO',
        55.00,
        5.50,
        0,
        60.50
    ),
    (
        '9002',
        'APROVADO',
        100.00,
        10.00,
        5.00,
        105.00
    ),
    (
        '9001',
        'CANCELADO',
        40.00,
        4.00,
        0,
        44.00
    );

-- Ajuste das sequências
SELECT setval (
        pg_get_serial_sequence ('pedidos', 'id'), COALESCE(MAX(id), 0) + 1, false
    )
FROM pedidos;

SELECT setval (
        pg_get_serial_sequence ('ingredientes', 'id'), COALESCE(MAX(id), 0) + 1, false
    )
FROM ingredientes;

SELECT setval (
        pg_get_serial_sequence ('itens_estoque', 'id'), COALESCE(MAX(id), 0) + 1, false
    )
FROM itens_estoque;

SELECT setval (
        pg_get_serial_sequence ('receitas', 'id'), COALESCE(MAX(id), 0) + 1, false
    )
FROM receitas;

SELECT setval (
        pg_get_serial_sequence ('produtos', 'id'), COALESCE(MAX(id), 0) + 1, false
    )
FROM produtos;

SELECT setval (
        pg_get_serial_sequence ('cardapios', 'id'), COALESCE(MAX(id), 0) + 1, false
    )
FROM cardapios;