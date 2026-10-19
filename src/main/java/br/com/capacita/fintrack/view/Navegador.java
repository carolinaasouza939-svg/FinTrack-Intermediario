package br.com.capacita.fintrack.view;

import br.com.capacita.fintrack.controller.FinTracker;
import br.com.capacita.fintrack.model.Transacao;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.UncheckedIOException;

public class Navegador {

    private final Stage palco;
    private final FinTracker finTracker;
    private Transacao transacaoEmEdicao; // null = cadastrando uma nova

    public Navegador(Stage palco, FinTracker finTracker) {
        this.palco = palco;
        this.finTracker = finTracker;
    }

    public FinTracker getFinTracker() {
        return finTracker;
    }

    public Transacao getTransacaoEmEdicao() {
        return transacaoEmEdicao;
    }

    public void irParaPrincipal() {
        transacaoEmEdicao = null;
        trocarTela("TelaPrincipal.fxml", "FinTrack - Transações");
    }

    public void irParaNovaTransacao() {
        transacaoEmEdicao = null;
        trocarTela("TelaNovaTransacao.fxml", "FinTrack - Nova transação");
    }

    public void irParaEdicao(Transacao transacao) {
        transacaoEmEdicao = transacao;
        trocarTela("TelaNovaTransacao.fxml", "FinTrack - Editar transação");
    }

    public void irParaRelatorio() {
        transacaoEmEdicao = null;
        trocarTela("TelaRelatorio.fxml", "FinTrack - Relatório");
    }

    private void trocarTela(String arquivoFxml, String titulo) {
        try {
            FXMLLoader carregador = new FXMLLoader(Navegador.class.getResource(arquivoFxml));
            Parent raiz = carregador.load();

            TelaController controller = carregador.getController();
            controller.iniciar(this);

            palco.setTitle(titulo);
            palco.setScene(new Scene(raiz));
            palco.show();

        } catch (IOException e) {
            throw new UncheckedIOException("Não foi possível abrir a tela " + arquivoFxml, e);
        }
    }
}
