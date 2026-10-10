-- PostgreSQL
-- 1) Crie o banco (uma vez):   CREATE DATABASE fintrack_db;
-- 2) Conectado em fintrack_db, rode este script.

CREATE TABLE IF NOT EXISTS transacoes (
    id        SERIAL PRIMARY KEY,
    descricao VARCHAR(100)  NOT NULL,
    valor     DECIMAL(10,2) NOT NULL,
    tipo      VARCHAR(10)   NOT NULL,   -- 'RECEITA' ou 'DESPESA'
    data      DATE          NOT NULL
);

-- Dados de exemplo (opcional)
INSERT INTO transacoes (descricao, valor, tipo, data) VALUES
('Salário de outubro', 5200.00, 'RECEITA', '2026-10-05'),
('Aluguel',            1000.00, 'DESPESA', '2026-10-06'),
('Mercado',             348.55, 'DESPESA', '2026-10-08');
