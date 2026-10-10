package test.utils;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class FormatadorTest {

    @Test
    void moedaUsaFormatoBrasileiro() {
        assertEquals("R$ 1.651,25", Formatador.moeda(1651.25));
    }

    @Test
    void moedaNegativaTemSinal() {
        assertEquals("-R$ 10,00", Formatador.moeda(-10));
    }

    @Test
    void dataUsaDiaMesAno() {
        assertEquals("05/10/2026", Formatador.data(LocalDate.of(2026, 10, 5)));
    }

    @Test
    void lerDataEntendeDiaMesAno() {
        assertEquals(LocalDate.of(2026, 10, 5), Formatador.lerData("05/10/2026"));
    }

    @Test
    void lerDataInvalidaOuVaziaDevolveNull() {
        assertNull(Formatador.lerData("32/13/2026"));
        assertNull(Formatador.lerData("  "));
        assertNull(Formatador.lerData(null));
    }

    @Test
    void tipoEmTexto() {
        assertEquals("Receita", Formatador.tipo(true));
        assertEquals("Despesa", Formatador.tipo(false));
    }
}
