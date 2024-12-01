package com.poli.bucaminas.modelo;

import com.poli.bucaminas.ui.MenuJuego;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;


import java.util.ArrayList;
import java.util.List;

public class Tablero {
    public static final int TAMANO_CELDA = 40;
    private int anchoTablero;
    private int altoTablero;
    private Celda[][] tablero;
    private BorderPane diseño;

    public Tablero(int ancho, int alto, int tamanoCelda) {
        this.anchoTablero = ancho;
        this.altoTablero = alto;
    }

    public Parent crearDiseñoPrincipal() {
        diseño = new BorderPane();
        diseño.setTop(new MenuJuego(this).crearBarraMenu());
        diseño.setCenter(crearTablero());
        return diseño;
    }

    private Pane crearTablero() {
        Pane contenedorTablero = new Pane();
        contenedorTablero.setPrefSize(anchoTablero * TAMANO_CELDA, altoTablero * TAMANO_CELDA);

        tablero = new Celda[anchoTablero][altoTablero];

        for (int y = 0; y < altoTablero; y++) {
            for (int x = 0; x < anchoTablero; x++) {
                Celda celda = new Celda(x, y, Math.random() < 0.15, this);
                tablero[x][y] = celda;
                contenedorTablero.getChildren().add(celda);
            }
        }

        establecerConteoBombasVecinas();

        return contenedorTablero;
    }

    private void establecerConteoBombasVecinas() {
        for (int y = 0; y < altoTablero; y++) {
            for (int x = 0; x < anchoTablero; x++) {
                Celda celda = tablero[x][y];
                if (!celda.tieneBomba) {
                    long bombasVecinas = obtenerVecinas(celda).stream().filter(c -> c.tieneBomba).count();
                    if (bombasVecinas > 0) {
                        celda.establecerTexto(String.valueOf(bombasVecinas));
                    }
                }
            }
        }
    }

    public List<Celda> obtenerVecinas(Celda celda) {
        List<Celda> vecinas = new ArrayList<>();
        int[] deltas = {-1, -1, -1, 0, -1, 1, 0, -1, 0, 1, 1, -1, 1, 0, 1, 1};

        for (int i = 0; i < deltas.length; i += 2) {
            int nuevoX = celda.x + deltas[i];
            int nuevoY = celda.y + deltas[i + 1];
            if (estaDentroDelTablero(nuevoX, nuevoY)) {
                vecinas.add(tablero[nuevoX][nuevoY]);
            }
        }
        return vecinas;
    }

    private boolean estaDentroDelTablero(int x, int y) {
        return x >= 0 && x < anchoTablero && y >= 0 && y < altoTablero;
    }

    public void cambiarTamañoTablero(int ancho, int alto) {
        this.anchoTablero = ancho;
        this.altoTablero = alto;
        reiniciarJuego();
    }

    public void reiniciarJuego() {
        diseño.setCenter(crearTablero());
    }

    public void deshabilitarTablero() {
        for (int y = 0; y < altoTablero; y++) {
            for (int x = 0; x < anchoTablero; x++) {
                tablero[x][y].setDisable(true);
            }
        }
    }

    public void revelarBombas() {
        for (int y = 0; y < altoTablero; y++) {
            for (int x = 0; x < anchoTablero; x++) {
                Celda celda = tablero[x][y];
                if (celda.tieneBomba) {
                    celda.revelar();
                }
            }
        }
    }

    public boolean verificarVictoria() {
        for (int y = 0; y < altoTablero; y++) {
            for (int x = 0; x < anchoTablero; x++) {
                Celda celda = tablero[x][y];
                // El usuario gana si todas las celdas sin bombas están abiertas
                if (!celda.tieneBomba && !celda.estaAbierta()) {
                    return false;
                }
            }
        }
        return true;
    }

    public void mostrarAlertaVictoria() {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("¡Victoria!");
        alerta.setHeaderText(null);
        alerta.setContentText("¡Felicidades! Has descubierto todas las celdas sin bombas.");
        alerta.showAndWait();

        reiniciarJuego();
    }

}
