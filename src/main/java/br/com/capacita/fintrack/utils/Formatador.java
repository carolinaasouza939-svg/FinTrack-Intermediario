package br.com.capacita.fintrack.utils;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

public final class Formatador {

    private static final Locale BRASIL = Locale.forLanguageTag("pt-BR");
    private static final DateTimeFormatter DATA_BR = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private Formatador() {
    }

    public static String moeda(double valor) {
        // O Java separa "R$" do número com um espaço especial (U+00A0); trocamos por espaço comum.
        return NumberFormat.getCurrencyInstance(BRASIL).format(valor).replace('\u00A0', ' ');
    }

    public static String data(LocalDate data) {
        return DATA_BR.format(data);
    }

    /** Lê "dd/MM/aaaa". Devolve null se o texto estiver vazio ou for inválido. */
    public static LocalDate lerData(String texto) {
        if (texto == null || texto.isBlank()) {
            return null;
        }
        try {
            return LocalDate.parse(texto.trim(), DATA_BR);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    public static String tipo(boolean ehReceita) {
        return ehReceita ? "Receita" : "Despesa";
    }
}
