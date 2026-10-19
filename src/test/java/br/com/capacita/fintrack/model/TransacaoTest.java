package br.com.capacita.fintrack.model;

import br.com.capacita.fintrack.exceptions.EntradaInvalidaException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TransacaoTest {

    private LocalDate hoje;

    @BeforeEach
    void prepararData() {
        hoje = LocalDate.of(2026, 10, 5);
    }

    @Test
    void receitaTemValorComSinalPositivo() {
        assertEquals(3200.00, new Transacao("Salário", 3200.00, true, hoje).getValorComSinal(), 0.001);
    }

    @Test
    void despesaTemValorComSinalNegativo() {
        assertEquals(-1200.00, new Transacao("Aluguel", 1200.00, false, hoje).getValorComSinal(), 0.001);
    }

    @Test
    void descricaoEhGuardadaSemEspacosNasPontas() {
        assertEquals("Mercado", new Transacao("  Mercado  ", 10, false, hoje).getDescricao());
    }

    @Test
    void valorZeroLancaExcecao() {
        assertThrows(EntradaInvalidaException.class, () -> new Transacao("Mercado", 0, false, hoje));
    }

    @Test
    void valorNegativoLancaExcecao() {
        assertThrows(EntradaInvalidaException.class, () -> new Transacao("Mercado", -50, false, hoje));
    }

    @Test
    void valorNaoNumericoLancaExcecao() {
        assertThrows(EntradaInvalidaException.class, () -> new Transacao("Mercado", Double.NaN, false, hoje));
    }

    @Test
    void descricaoEmBrancoLancaExcecao() {
        assertThrows(EntradaInvalidaException.class, () -> new Transacao("   ", 50, false, hoje));
    }

    @Test
    void descricaoNulaLancaExcecao() {
        assertThrows(EntradaInvalidaException.class, () -> new Transacao(null, 50, false, hoje));
    }

    @Test
    void descricaoMaiorQueOLimiteDoBancoLancaExcecao() {
        String longa = "x".repeat(Transacao.TAMANHO_MAXIMO_DESCRICAO + 1);

        assertThrows(EntradaInvalidaException.class, () -> new Transacao(longa, 50, false, hoje));
    }

    @Test
    void dataNulaLancaExcecao() {
        assertThrows(EntradaInvalidaException.class, () -> new Transacao("Mercado", 50, false, null));
    }

    @Test
    void transacaoMensalRejeitaDiaDeVencimentoInvalido() {
        assertThrows(EntradaInvalidaException.class,
                () -> new TransacaoMensal("Internet", 99.90, false, hoje, 32));
        assertThrows(EntradaInvalidaException.class,
                () -> new TransacaoMensal("Internet", 99.90, false, hoje, 0));
    }

    @Test
    void transacaoMensalGuardaODiaDeVencimento() {
        assertEquals(10, new TransacaoMensal("Internet", 99.90, false, hoje, 10).getDiaVencimento());
    }
}
