package br.com.capacita.fintrack.dao;

import java.sql.Connection;
import java.sql.SQLException;

/** Quem sabe abrir uma conexão. Nos testes, entra uma conexão com SQLite em memória. */
@FunctionalInterface
public interface FabricaConexao {

    Connection abrir() throws SQLException;
}
