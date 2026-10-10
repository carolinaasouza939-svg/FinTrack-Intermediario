package utils;

import model.Transacao;

import java.util.List;

public final class UtilFinanceiro {

    private UtilFinanceiro() {
    }

    public static double somar(List<? extends Transacao> transacoes) {
        double total = 0;
        for (Transacao transacao : transacoes) {
            total += transacao.getValorComSinal();
        }
        return total;
    }

    public static double somarPorTipo(List<? extends Transacao> transacoes, boolean ehReceita) {
        double total = 0;
        for (Transacao transacao : transacoes) {
            if (transacao.isEhReceita() == ehReceita) {
                total += transacao.getValor();
            }
        }
        return total;
    }
}
