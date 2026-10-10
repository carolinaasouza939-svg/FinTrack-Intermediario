package dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


public final class ConexaoPostgres {

    private static final String ARQUIVO_CONFIG = "db.properties";

    private ConexaoPostgres() {
    }

    public static Connection conectar() throws SQLException {
        Properties config = carregarConfiguracao();
        return DriverManager.getConnection(
                config.getProperty("url"),
                config.getProperty("usuario"),
                config.getProperty("senha"));
    }

    static Properties carregarConfiguracao() {
        Properties config = new Properties();
        config.setProperty("url", "jdbc:postgresql://localhost:5432/fintrack_db");
        config.setProperty("usuario", "postgres");
        config.setProperty("senha", "");

        try (FileInputStream arquivo = new FileInputStream(ARQUIVO_CONFIG)) {
            config.load(arquivo);
        } catch (IOException e) {
        }

        sobrescrever(config, "url", System.getenv("FINTRACK_DB_URL"));
        sobrescrever(config, "usuario", System.getenv("FINTRACK_DB_USER"));
        sobrescrever(config, "senha", System.getenv("FINTRACK_DB_PASSWORD"));
        return config;
    }

    private static void sobrescrever(Properties config, String chave, String valor) {
        if (valor != null && !valor.isEmpty()) {
            config.setProperty(chave, valor);
        }
    }
}
