package br.com.capacita.fintrack.model;

import br.com.capacita.fintrack.exceptions.EntradaInvalidaException;

import java.time.LocalDate;

/** Herança: uma transação que se repete todo mês (ex.: aluguel, internet, salário). */
public class TransacaoMensal extends Transacao {

    private final int diaVencimento;

    public TransacaoMensal(String descricao, double valor, boolean ehReceita,
                           LocalDate data, int diaVencimento) {
        super(descricao, valor, ehReceita, data);
        if (diaVencimento < 1 || diaVencimento > 31) {
            throw new EntradaInvalidaException("O dia de vencimento deve estar entre 1 e 31.");
        }
        this.diaVencimento = diaVencimento;
    }

    public int getDiaVencimento() {
        return diaVencimento;
    }

    @Override
    public String toString() {
        return super.toString() + " (todo dia " + diaVencimento + ")";
    }
}
