package br.com.capacita.fintrack.dao;

import br.com.capacita.fintrack.exceptions.RepositorioException;
import br.com.capacita.fintrack.model.Transacao;
import br.com.capacita.fintrack.repository.Repositorio;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/** Isola toda a persistência (JDBC). Só usa PreparedStatement, então não há SQL Injection. */
public class TransacaoDAO implements Repositorio<Transacao> {

    private static final String RECEITA = "RECEITA";
    private static final String DESPESA = "DESPESA";

    private final FabricaConexao fabrica;

    public TransacaoDAO(FabricaConexao fabrica) {
        this.fabrica = fabrica;
    }

    @Override
    public void adicionar(Transacao transacao) {
        String sql = "INSERT INTO transacoes (descricao, valor, tipo, data) VALUES (?, ?, ?, ?)";

        try (Connection conexao = fabrica.abrir();
             PreparedStatement comando = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            preencher(comando, transacao);
            comando.executeUpdate();

            // Devolve o id gerado pelo banco para o objeto (necessário para editar/remover depois).
            try (ResultSet chaves = comando.getGeneratedKeys()) {
                if (chaves.next()) {
                    transacao.setId(chaves.getInt(1));
                }
            }

        } catch (SQLException e) {
            throw new RepositorioException("Não foi possível salvar a transação.", e);
        }
    }

    @Override
    public boolean atualizar(Transacao transacao) {
        String sql = "UPDATE transacoes SET descricao = ?, valor = ?, tipo = ?, data = ? WHERE id = ?";

        try (Connection conexao = fabrica.abrir();
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            preencher(comando, transacao);
            comando.setInt(5, transacao.getId());
            return comando.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RepositorioException("Não foi possível atualizar a transação.", e);
        }
    }

    @Override
    public boolean remover(Transacao transacao) {
        String sql = "DELETE FROM transacoes WHERE id = ?";

        try (Connection conexao = fabrica.abrir();
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setInt(1, transacao.getId());
            return comando.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RepositorioException("Não foi possível remover a transação.", e);
        }
    }

    @Override
    public List<Transacao> listar() {
        String sql = "SELECT id, descricao, valor, tipo, data FROM transacoes ORDER BY data DESC, id DESC";
        List<Transacao> transacoes = new ArrayList<>();

        try (Connection conexao = fabrica.abrir();
             PreparedStatement comando = conexao.prepareStatement(sql);
             ResultSet resultado = comando.executeQuery()) {

            while (resultado.next()) {
                transacoes.add(new Transacao(
                        resultado.getInt("id"),
                        resultado.getString("descricao"),
                        resultado.getDouble("valor"),
                        RECEITA.equalsIgnoreCase(resultado.getString("tipo")),
                        resultado.getDate("data").toLocalDate()));
            }

        } catch (SQLException e) {
            throw new RepositorioException("Não foi possível listar as transações.", e);
        }

        return transacoes;
    }

    private void preencher(PreparedStatement comando, Transacao transacao) throws SQLException {
        comando.setString(1, transacao.getDescricao());
        comando.setDouble(2, transacao.getValor());
        comando.setString(3, transacao.isEhReceita() ? RECEITA : DESPESA);
        comando.setDate(4, Date.valueOf(transacao.getData()));
    }
}
