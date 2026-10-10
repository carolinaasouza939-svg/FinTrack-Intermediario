package br.com.capacita.fintrack.app;

import br.com.capacita.fintrack.controller.FinTracker;
import br.com.capacita.fintrack.view.Alertas;
import br.com.capacita.fintrack.view.Navegador;
import javafx.application.Application;
import javafx.stage.Stage;

public class FinTrackApp extends Application {

    @Override
    public void start(Stage palco) {
        FinTracker finTracker = new FinTracker(FabricaRepositorio.criar(Alertas::aviso));
        new Navegador(palco, finTracker).irParaPrincipal();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
