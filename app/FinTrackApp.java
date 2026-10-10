package app;

import controller.FinTracker;
import view.Alertas;
import view.Navegador;
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
