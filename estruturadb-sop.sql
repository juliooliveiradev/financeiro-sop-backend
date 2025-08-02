CREATE TABLE despesas (
    protocolo VARCHAR(20) PRIMARY KEY,
    tipo VARCHAR(50),
    data_protocolo TIMESTAMP NOT NULL,
    data_vencimento DATE NOT NULL,
    credor VARCHAR(100) NOT NULL,
    descricao TEXT NOT NULL,
    valor DECIMAL(12, 2) NOT NULL,
    status VARCHAR(50)
);

CREATE TABLE empenhos (
    numero VARCHAR(20) PRIMARY KEY,
    data DATE NOT NULL,
    valor DECIMAL(12,2) NOT NULL,
    observacao TEXT,
    despesa_id VARCHAR(20) NOT NULL,
    CONSTRAINT fk_despesa FOREIGN KEY (despesa_id) REFERENCES despesas(protocolo) ON DELETE RESTRICT
);

CREATE TABLE pagamentos (
    numero VARCHAR(20) PRIMARY KEY,
    data DATE NOT NULL,
    valor DECIMAL(12,2) NOT NULL,
    observacao TEXT,
    empenho_id VARCHAR(20) NOT NULL,
    CONSTRAINT fk_empenho FOREIGN KEY (empenho_id) REFERENCES empenhos(numero) ON DELETE RESTRICT
);
