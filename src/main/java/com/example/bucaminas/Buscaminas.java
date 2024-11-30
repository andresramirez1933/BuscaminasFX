package com.example.bucaminas;

import com.example.bucaminas.modelo.Tablero;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

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
