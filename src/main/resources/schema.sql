CREATE TABLE USUARIOS
(
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(255) NOT NULL,
    cpf_cnpj VARCHAR(255) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    senha VARCHAR(255) NOT NULL,
    perfil VARCHAR(255) NOT NULL,
    instante_criacao DATETIME NOT NULL DEFAULT NOW(),
    dinheiro DECIMAL(12, 2) NOT NULL DEFAULT 0.0
);

CREATE TABLE MOVIMENTACOES
(
    id                   BIGINT PRIMARY KEY AUTO_INCREMENT,
    usuario_id           BIGINT         NOT NULL,
    valor_atual           DECIMAL(12, 2) NOT NULL,
    valor_transacao       DECIMAL(12, 2) NOT NULL,
    novo_saldo            DECIMAL(12, 2) NOT NULL,
    instante_movimentacao DATETIME       NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_movimentacao_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios (id)
);