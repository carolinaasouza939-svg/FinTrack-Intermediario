package br.com.capacita.fintrack.view;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public final class Alertas {

    private Alertas() {
    }

    public static void aviso(String mensagem) {
        exibir(AlertType.WARNING, mensagem);
    }

    public static void erro(String mensagem) {
        exibir(AlertType.ERROR, mensagem);
    }

    public static boolean confirmar(String mensagem) {
        Alert alerta = new Alert(AlertType.CONFIRMATION, mensagem, ButtonType.YES, ButtonType.NO);
        alerta.setTitle("Confirmação");
        alerta.setHeaderText(null);
        return alerta.showAndWait().orElse(ButtonType.NO) == ButtonType.YES;
    }

    private static void exibir(AlertType tipo, String mensagem) {
        Alert alerta = new Alert(tipo, mensagem);
        alerta.setHeaderText(null);
        alerta.showAndWait();
    }
}
