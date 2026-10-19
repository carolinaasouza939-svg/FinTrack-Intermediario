package br.com.capacita.fintrack.dao;

import br.com.capacita.fintrack.exceptions.RepositorioException;
import br.com.capacita.fintrack.model.Transacao;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TransacaoDAOTest {

    private static final String URL = "jdbc:sqlite:file:fintrack_teste?mode=memory&cache=shared";

    private Connection guardiao;
    private TransacaoDAO dao;

    @BeforeEach
    void criarBancoEmMemoria() throws SQLException {
        guardiao = DriverManager.getConnection(URL);

        try (Statement comando = guardiao.createStatement()) {
            comando.execute("""
                    CREATE TABLE transacoes (
                        id        INTEGER PRIMARY KEY AUTOINCREMENT,
                        descricao TEXT NOT NULL,
                        valor     REAL NOT NULL,
                        tipo      TEXT NOT NULL,
                        data      DATE NOT NULL
                    )
                    """);
        }

        dao = new TransacaoDAO(() -> DriverManager.getConnection(URL));
    }

    @AfterEach
    void derrubarBanco() throws SQLException {
        guardiao.close();
    }

    @Test
    void adicionarGravaATransacaoEPreencheOId() {
        Transacao salario = new Transacao("Salário", 3200.00, true, LocalDate.of(2026, 10, 5));

        dao.adicionar(salario);

        assertNotEquals(0, salario.getId());
        assertEquals(1, dao.listar().size());
    }

    @Test
    void listarDevolveOsDadosQueForamGravados() {
        dao.adicionar(new Transacao("Mercado", 348.75, false, LocalDate.of(2026, 10, 8)));

        Transacao lida = dao.listar().get(0);

        assertEquals("Mercado", lida.getDescricao());
        assertEquals(348.75, lida.getValor(), 0.001);
        assertFalse(lida.isEhReceita());
        assertEquals(LocalDate.of(2026, 10, 8), lida.getData());
    }

    @Test
    void receitaVoltaComoReceita() {
        dao.adicionar(new Transacao("Salário", 3200, true, LocalDate.of(2026, 10, 5)));

        assertTrue(dao.listar().get(0).isEhReceita());
    }

    @Test
    void listarDevolveAMaisRecentePrimeiro() {
        dao.adicionar(new Transacao("Aluguel", 1200.00, false, LocalDate.of(2026, 10, 6)));
        dao.adicionar(new Transacao("Mercado", 348.75, false, LocalDate.of(2026, 10, 8)));

        List<Transacao> lista = dao.listar();

        assertEquals("Mercado", lista.get(0).getDescricao());
        assertEquals("Aluguel", lista.get(1).getDescricao());
    }

    @Test
    void atualizarAlteraSoATransacaoCerta() {
        Transacao aluguel = new Transacao("Aluguel", 1200.00, false, LocalDate.of(2026, 10, 6));
        Transacao mercado = new Transacao("Mercado", 348.75, false, LocalDate.of(2026, 10, 8));
        dao.adicionar(aluguel);
        dao.adicionar(mercado);

        mercado.setDescricao("Feira");
        mercado.setValor(80);

        assertTrue(dao.atualizar(mercado));

        List<Transacao> lista = dao.listar();
        assertEquals("Feira", lista.get(0).getDescricao());
        assertEquals(80, lista.get(0).getValor(), 0.001);
        assertEquals("Aluguel", lista.get(1).getDescricao());
        assertEquals(1200, lista.get(1).getValor(), 0.001);
    }

    @Test
    void atualizarTransacaoQueNuncaFoiSalvaDevolveFalse() {
        assertFalse(dao.atualizar(new Transacao("Cinema", 40, false, LocalDate.of(2026, 10, 9))));
    }

    @Test
    void removerTransacaoExistenteDevolveTrueEApagaDoBanco() {
        Transacao aluguel = new Transacao("Aluguel", 1200.00, false, LocalDate.of(2026, 10, 6));
        dao.adicionar(aluguel);

        assertTrue(dao.remover(aluguel));
        assertTrue(dao.listar().isEmpty());
    }

    @Test
    void removerApagaSoUmaQuandoHaDuasIguais() {
        Transacao primeira = new Transacao("Café", 5, false, LocalDate.of(2026, 10, 8));
        Transacao segunda = new Transacao("Café", 5, false, LocalDate.of(2026, 10, 8));
        dao.adicionar(primeira);
        dao.adicionar(segunda);

        assertTrue(dao.remover(primeira));

        assertEquals(1, dao.listar().size());
    }

    @Test
    void removerTransacaoQueNuncaFoiSalvaDevolveFalse() {
        assertFalse(dao.remover(new Transacao("Cinema", 40, false, LocalDate.of(2026, 10, 9))));
    }

    @Test
    void descricaoComAspasNaoQuebraOSql() {
        dao.adicionar(new Transacao("x'); DROP TABLE transacoes; --", 10, false, LocalDate.of(2026, 10, 9)));

        assertEquals(1, dao.listar().size());
        assertEquals("x'); DROP TABLE transacoes; --", dao.listar().get(0).getDescricao());
    }

    @Test
    void bancoForaDoArLancaRepositorioException() {
        TransacaoDAO daoSemBanco = new TransacaoDAO(() -> {
            throw new SQLException("banco fora do ar");
        });

        RepositorioException erro = assertThrows(RepositorioException.class, daoSemBanco::listar);
        assertEquals("banco fora do ar", erro.detalhe());
    }
}
