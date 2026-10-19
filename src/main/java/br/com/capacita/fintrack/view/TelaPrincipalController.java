package br.com.capacita.fintrack.view;

import br.com.capacita.fintrack.controller.FinTracker;
import br.com.capacita.fintrack.exceptions.RepositorioException;
import br.com.capacita.fintrack.model.Transacao;
import br.com.capacita.fintrack.utils.Formatador;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class TelaPrincipalController implements TelaController {

    @FXML
    private TableView<Transacao> tabelaTransacoes;

    @FXML
    private TableColumn<Transacao, String> colunaData;

    @FXML
    private TableColumn<Transacao, String> colunaDescricao;

    @FXML
    private TableColumn<Transacao, String> colunaTipo;

    @FXML
    private TableColumn<Transacao, String> colunaValor;

    @FXML
    private Label rotuloSaldo;

    private Navegador navegador;

    @Override
    public void iniciar(Navegador navegador) {
        this.navegador = navegador;

        colunaData.setCellValueFactory(celula ->
                new SimpleStringProperty(Formatador.data(celula.getValue().getData())));
        colunaDescricao.setCellValueFactory(celula ->
                new SimpleStringProperty(celula.getValue().getDescricao()));
        colunaTipo.setCellValueFactory(celula ->
                new SimpleStringProperty(Formatador.tipo(celula.getValue().isEhReceita())));
        colunaValor.setCellValueFactory(celula ->
                new SimpleStringProperty(Formatador.moeda(celula.getValue().getValor())));

        colunaTipo.setCellFactory(coluna -> new TableCell<>() {
            @Override
            protected void updateItem(String tipo, boolean vazio) {
                super.updateItem(tipo, vazio);
                getStyleClass().removeAll("receita", "despesa");
                if (vazio || tipo == null) {
                    setText(null);
                    return;
                }
                setText(tipo);
                getStyleClass().add("Receita".equals(tipo) ? "receita" : "despesa");
            }
        });

        atualizarTela();
    }

    private void atualizarTela() {
        FinTracker finTracker = navegador.getFinTracker();

        try {
            tabelaTransacoes.setItems(FXCollections.observableArrayList(finTracker.listarTransacoes()));
            Estilos.mostrarSaldo(rotuloSaldo, "Saldo: ", finTracker.calcularSaldoTotal());
        } catch (RepositorioException e) {
            Alertas.erro(e.getMessage() + "\n" + e.detalhe());
        }
    }

    @FXML
    private void aoClicarNova() {
        navegador.irParaNovaTransacao();
    }

    @FXML
    private void aoClicarEditar() {
        Transacao selecionada = tabelaTransacoes.getSelectionModel().getSelectedItem();

        if (selecionada == null) {
            Alertas.aviso("Selecione uma transação na tabela.");
            return;
        }

        navegador.irParaEdicao(selecionada);
    }

    @FXML
    private void aoClicarRemover() {
        Transacao selecionada = tabelaTransacoes.getSelectionModel().getSelectedItem();

        if (selecionada == null) {
            Alertas.aviso("Selecione uma transação na tabela.");
            return;
        }

        if (!Alertas.confirmar("Remover \"" + selecionada.getDescricao() + "\" ("
                + Formatador.moeda(selecionada.getValor()) + ")?")) {
            return;
        }

        try {
            navegador.getFinTracker().removerTransacao(selecionada);
            atualizarTela();
        } catch (RepositorioException e) {
            Alertas.erro(e.getMessage() + "\n" + e.detalhe());
        }
    }

    @FXML
    private void aoClicarRelatorio() {
        navegador.irParaRelatorio();
    }
}
