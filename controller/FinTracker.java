package controller;

import model.Transacao;
import repository.Repositorio;
import utils.UtilFinanceiro;

import java.util.List;
import java.util.Objects;

public class FinTracker {

    private final Repositorio<Transacao> repositorio;

    public FinTracker(Repositorio<Transacao> repositorio) {
        this.repositorio = Objects.requireNonNull(repositorio, "O repositório não pode ser nulo.");
    }

    public void adicionarTransacao(Transacao transacao) {
        repositorio.adicionar(transacao);
    }

    public boolean atualizarTransacao(Transacao transacao) {
        return repositorio.atualizar(transacao);
    }

    public boolean removerTransacao(Transacao transacao) {
        return repositorio.remover(transacao);
    }

    public List<Transacao> listarTransacoes() {
        return repositorio.listar();
    }

    public double calcularSaldoTotal() {
        return UtilFinanceiro.somar(listarTransacoes());
    }

    public double calcularTotalReceitas() {
        return UtilFinanceiro.somarPorTipo(listarTransacoes(), true);
    }

    public double calcularTotalDespesas() {
        return UtilFinanceiro.somarPorTipo(listarTransacoes(), false);
    }
}
