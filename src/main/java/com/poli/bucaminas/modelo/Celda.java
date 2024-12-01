package com.poli.bucaminas.modelo;

import javafx.scene.control.Alert;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Celda extends StackPane {
    final int x, y;
    final boolean tieneBomba;
    private boolean estaAbierta = false;

    private final Rectangle borde;
    private final Text texto;
    private final Tablero tablero;

    public Celda(int x, int y, boolean tieneBomba, Tablero tablero) {
        this.x = x;
        this.y = y;
        this.tieneBomba = tieneBomba;
        this.tablero = tablero;

        borde = new Rectangle(Tablero.TAMANO_CELDA - 2, Tablero.TAMANO_CELDA - 2);
        texto = new Text();

        configurarApariencia();
        configurarAccion();
    }

    private void configurarApariencia() {
        borde.setStroke(Color.LIGHTGRAY);
        borde.setFill(Color.BLUE);
        texto.setFont(Font.font(18));
        texto.setVisible(false);
        texto.setText(tieneBomba ? ":(" : "");

        getChildren().addAll(borde, texto);
        setTranslateX(x * Tablero.TAMANO_CELDA);
        setTranslateY(y * Tablero.TAMANO_CELDA);
    }

    private void configurarAccion() {
        setOnMouseClicked(e -> abrir());
    }

    public void abrir() {
        if (estaAbierta) return;

        estaAbierta = true;
        texto.setVisible(true);
        borde.setFill(null);

        if (tieneBomba) {
            System.out.println("¡Game Over!");
//            tablero.reiniciarJuego();
            tablero.deshabilitarTablero(); // Detiene el juego
            tablero.revelarBombas(); // Revela todas las bombas
            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Game Over");
            alerta.setHeaderText(null);
            alerta.setContentText("¡Has perdido! Inicia una nueva partida.");
            alerta.showAndWait();
        } else if (texto.getText().isEmpty()) {
            tablero.obtenerVecinas(this).forEach(Celda::abrir);
        }
        // Verifica si el usuario ha ganado
        if (tablero.verificarVictoria()) {
            tablero.mostrarAlertaVictoria();
        }
    }

    public void establecerTexto(String valor) {
        texto.setText(valor);
    }

    public void revelar() {
        if (!estaAbierta) {
            estaAbierta = true;
            texto.setVisible(true);
            borde.setFill(null);
        }
    }

    public boolean estaAbierta() {
        return estaAbierta;
    }

}
