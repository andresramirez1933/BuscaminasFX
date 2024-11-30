package com.example.bucaminas.modelo;

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
        texto.setFont(Font.font(18));
        texto.setVisible(false);
        texto.setText(tieneBomba ? "X" : "");

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
            tablero.reiniciarJuego();
        } else if (texto.getText().isEmpty()) {
            tablero.obtenerVecinas(this).forEach(Celda::abrir);
        }
    }

    public void establecerTexto(String valor) {
        texto.setText(valor);
    }
}
