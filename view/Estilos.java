package view;

import utils.Formatador;
import javafx.scene.control.Label;

public final class Estilos {

    private Estilos() {
    }
  
    public static void mostrarSaldo(Label rotulo, String prefixo, double saldo) {
        rotulo.setText(prefixo + Formatador.moeda(saldo));
        rotulo.getStyleClass().removeAll("positivo", "negativo");
        rotulo.getStyleClass().add(saldo >= 0 ? "positivo" : "negativo");
    }
}
