package br.com.capacita.fintrack.view;

import br.com.capacita.fintrack.exceptions.EntradaInvalidaException;
import br.com.capacita.fintrack.exceptions.RepositorioException;
import br.com.capacita.fintrack.model.Transacao;
import br.com.capacita.fintrack.utils.Formatador;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

import java.time.LocalDate;

public class TelaNovaTransacaoController implements TelaController {

    @FXML
    private Label rotuloTitulo;

    @FXML
    private TextField campoDescricao;

    @FXML
    private TextField campoValor;

    @FXML
    private DatePicker seletorData;

    @FXML
    private RadioButton radioReceita;

    @FXML
    private RadioButton radioDespesa;

    private Navegador navegador;
    private Transacao emEdicao;

    @Override
    public void iniciar(Navegador navegador) {
        this.navegador = navegador;
        this.emEdicao = navegador.getTransacaoEmEdicao();

        seletorData.setConverter(new StringConverter<>() {
            @Override
            public String toString(LocalDate data) {
                return data == null ? "" : Formatador.data(data);
            }

            @Override
            public LocalDate fromString(String texto) {
                return Formatador.lerData(texto);
            }
        });
        seletorData.setPromptText("dd/mm/aaaa");

        if (emEdicao == null) {
            seletorData.setValue(LocalDate.now());
            radioDespesa.setSelected(true);
            return;
        }

        rotuloTitulo.setText("Editar transação");
        campoDescricao.setText(emEdicao.getDescricao());
        campoValor.setText(String.format("%.2f", emEdicao.getValor()).replace('.', ','));
        seletorData.setValue(emEdicao.getData());
        radioReceita.setSelected(emEdicao.isEhReceita());
        radioDespesa.setSelected(!emEdicao.isEhReceita());
    }

    @FXML
    private void aoClicarSalvar() {
        try {
            double valor = Double.parseDouble(campoValor.getText().trim().replace(',', '.'));
            boolean ehReceita = radioReceita.isSelected();

            Transacao validada = new Transacao(campoDescricao.getText(), valor, ehReceita, seletorData.getValue());

            if (emEdicao == null) {
                navegador.getFinTracker().adicionarTransacao(validada);
            } else {
                emEdicao.setDescricao(validada.getDescricao());
                emEdicao.setValor(validada.getValor());
                emEdicao.setEhReceita(validada.isEhReceita());
                emEdicao.setData(validada.getData());
                navegador.getFinTracker().atualizarTransacao(emEdicao);
            }

            navegador.irParaPrincipal();

        } catch (NumberFormatException e) {
            Alertas.aviso("Digite o valor como número. Exemplo: 49,90");
        } catch (EntradaInvalidaException e) {
            Alertas.aviso(e.getMessage());
        } catch (RepositorioException e) {
            Alertas.erro(e.getMessage() + "\n" + e.detalhe());
        }
    }

    @FXML
    private void aoClicarCancelar() {
        navegador.irParaPrincipal();
    }
}
