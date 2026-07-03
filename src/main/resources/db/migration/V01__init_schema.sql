CREATE TABLE usuario (
    codigo BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL
);

CREATE TABLE unidade (
    codigo BIGSERIAL PRIMARY KEY,
    identificacao VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE transportadora (
    codigo BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    cnpj VARCHAR(20) NOT NULL UNIQUE
);

CREATE TABLE encomenda (
    codigo BIGSERIAL PRIMARY KEY,
    descricao TEXT NOT NULL,
    data_recebimento TIMESTAMP NOT NULL,
    status VARCHAR(50) NOT NULL,
    codigo_unidade BIGINT NOT NULL,
    codigo_transportadora BIGINT NOT NULL,
    CONSTRAINT fk_encomenda_unidade FOREIGN KEY (codigo_unidade) REFERENCES unidade (codigo),
    CONSTRAINT fk_encomenda_transportadora FOREIGN KEY (codigo_transportadora) REFERENCES transportadora (codigo)
);

CREATE TABLE aviso_mural (
    codigo BIGSERIAL PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    conteudo TEXT NOT NULL,
    data_publicacao TIMESTAMP NOT NULL
);
