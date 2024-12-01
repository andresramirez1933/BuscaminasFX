package com.poli.bucaminas;

import com.poli.bucaminas.modelo.Tablero;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Buscaminas extends Application {


    private Scene escena;
    private static final int TAMANO_CELDA = 40;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage escenario) {
        Tablero tablero = new Tablero(20, 15, TAMANO_CELDA);
        escena = new Scene(tablero.crearDiseñoPrincipal());

        escenario.setScene(escena);
        escenario.setTitle("Buscaminas");
        escenario.show();
    }
}
