package test.controller;

import exceptions.EntradaInvalidaException;
import model.Transacao;
import repository.RepositorioGenerico;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FinTrackerTest {

    private FinTracker finTracker;

    @BeforeEach
    void prepararFinTracker() {
        finTracker = new FinTracker(new RepositorioGenerico<>());
        finTracker.adicionarTransacao(new Transacao("Salário", 3200.00, true, LocalDate.of(2026, 10, 5)));
        finTracker.adicionarTransacao(new Transacao("Aluguel", 1200.00, false, LocalDate.of(2026, 10, 6)));
        finTracker.adicionarTransacao(new Transacao("Mercado", 348.75, false, LocalDate.of(2026, 10, 8)));
    }

    @Test
    void saldoEReceitasMenosDespesas() {
        assertEquals(1651.25, finTracker.calcularSaldoTotal(), 0.001);
    }

    @Test
    void saldoSemTransacoesEZero() {
        assertEquals(0, new FinTracker(new RepositorioGenerico<>()).calcularSaldoTotal(), 0.001);
    }

    @Test
    void totaisSaoCalculadosPorTipo() {
        assertEquals(3200.00, finTracker.calcularTotalReceitas(), 0.001);
        assertEquals(1548.75, finTracker.calcularTotalDespesas(), 0.001);
    }

    @Test
    void removerTransacaoAtualizaOSaldo() {
        Transacao aluguel = finTracker.listarTransacoes().get(1);

        assertTrue(finTracker.removerTransacao(aluguel));

        assertEquals(2851.25, finTracker.calcularSaldoTotal(), 0.001);
    }

    @Test
    void removerTransacaoQueNaoExisteDevolveFalse() {
        Transacao desconhecida = new Transacao("Cinema", 40, false, LocalDate.of(2026, 10, 9));

        assertFalse(finTracker.removerTransacao(desconhecida));
    }

    @Test
    void editarTransacaoAtualizaOSaldo() {
        Transacao mercado = finTracker.listarTransacoes().get(2);

        mercado.setValor(48.75);

        assertTrue(finTracker.atualizarTransacao(mercado));
        assertEquals(1951.25, finTracker.calcularSaldoTotal(), 0.001);
    }

    @Test
    void adicionarComValorInvalidoNaoGuardaNada() {
        assertThrows(EntradaInvalidaException.class,
                () -> finTracker.adicionarTransacao(new Transacao("Erro", -1, false, LocalDate.of(2026, 10, 9))));

        assertEquals(3, finTracker.listarTransacoes().size());
    }

    @Test
    void repositorioNuloEhRecusado() {
        assertThrows(NullPointerException.class, () -> new FinTracker(null));
    }
}
