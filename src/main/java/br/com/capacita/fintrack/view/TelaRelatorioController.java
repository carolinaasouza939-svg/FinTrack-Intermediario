package br.com.capacita.fintrack.view;

import br.com.capacita.fintrack.controller.FinTracker;
import br.com.capacita.fintrack.exceptions.RepositorioException;
import br.com.capacita.fintrack.utils.Formatador;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class TelaRelatorioController implements TelaController {

    @FXML
    private Label rotuloReceitas;

    @FXML
    private Label rotuloDespesas;

    @FXML
    private Label rotuloSaldo;

    @FXML
    private Label rotuloQuantidade;

    private Navegador navegador;

    @Override
    public void iniciar(Navegador navegador) {
        this.navegador = navegador;
        FinTracker finTracker = navegador.getFinTracker();

        try {
            rotuloReceitas.setText(Formatador.moeda(finTracker.calcularTotalReceitas()));
            rotuloDespesas.setText(Formatador.moeda(finTracker.calcularTotalDespesas()));
            Estilos.mostrarSaldo(rotuloSaldo, "", finTracker.calcularSaldoTotal());
            rotuloQuantidade.setText(String.valueOf(finTracker.listarTransacoes().size()));
        } catch (RepositorioException e) {
            Alertas.erro(e.getMessage() + "\n" + e.detalhe());
        }
    }

    @FXML
    private void aoClicarVoltar() {
        navegador.irParaPrincipal();
    }
}
