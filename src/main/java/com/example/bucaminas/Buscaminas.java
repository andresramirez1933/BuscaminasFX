package com.example.bucaminas;

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

	private static final int TAMANO_CELDA = 40;
    private int anchoTablero = 20; // Ancho predeterminado del tablero
    private int altoTablero = 15; // Alto predeterminado del tablero
    private Celda[][] tablero;
    private Scene escena;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage escenario) {
        escena = new Scene(crearDiseñoPrincipal());

        escenario.setScene(escena);
        escenario.setTitle("Buscaminas");
        escenario.show();
    }

    // Crea el diseño principal con una barra de menú y el tablero del juego
    private BorderPane crearDiseñoPrincipal() {
        BorderPane diseño = new BorderPane();
        diseño.setTop(crearBarraMenu());
        diseño.setCenter(crearTablero());
        return diseño;
    }

    // Crea el tablero del juego
    private Parent crearTablero() {
        Pane contenedorTablero = new Pane();
        contenedorTablero.setPrefSize(anchoTablero * TAMANO_CELDA, altoTablero * TAMANO_CELDA);

        tablero = new Celda[anchoTablero][altoTablero];

        // Poblar el tablero con celdas
        for (int y = 0; y < altoTablero; y++) {
            for (int x = 0; x < anchoTablero; x++) {
                Celda celda = new Celda(x, y, Math.random() < 0.2);
                tablero[x][y] = celda;
                contenedorTablero.getChildren().add(celda);
            }
        }

        // Configurar las bombas vecinas
        establecerConteoBombasVecinas();

        return contenedorTablero;
    }

    // Establece el conteo de bombas vecinas para cada celda
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

    // Obtiene las celdas vecinas de una celda dada
    private List<Celda> obtenerVecinas(Celda celda) {
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

    // Verifica si una posición está dentro de los límites del tablero
    private boolean estaDentroDelTablero(int x, int y) {
        return x >= 0 && x < anchoTablero && y >= 0 && y < altoTablero;
    }

    // Crea la barra de menú con opciones de tamaño del tablero
    private MenuBar crearBarraMenu() {
        MenuBar barraMenu = new MenuBar();
        Menu menuOpciones = new Menu("Opciones");

        // Crear elementos de menú para diferentes tamaños de tablero
        menuOpciones.getItems().addAll(
                crearElementoMenu("10x10", 10, 10),
                crearElementoMenu("20x15", 20, 15),
                crearElementoMenu("30x20", 30, 20)
        );

        barraMenu.getMenus().add(menuOpciones);
        return barraMenu;
    }

    // Crea un elemento de menú para establecer el tamaño del tablero
    private MenuItem crearElementoMenu(String etiqueta, int ancho, int alto) {
        MenuItem elemento = new MenuItem(etiqueta);
        elemento.setOnAction(e -> cambiarTamañoTablero(ancho, alto));
        return elemento;
    }

    // Cambia el tamaño del tablero y reinicia el juego
    private void cambiarTamañoTablero(int ancho, int alto) {
        this.anchoTablero = ancho;
        this.altoTablero = alto;
        reiniciarJuego();
    }

    // Reinicia el juego con el tamaño actual del tablero
    private void reiniciarJuego() {
        escena.setRoot(crearDiseñoPrincipal());
    }

    // Clase que representa cada celda del tablero
    private class Celda extends StackPane {
        private final int x, y;
        private final boolean tieneBomba;
        private boolean estaAbierta = false;

        private final Rectangle borde = new Rectangle(TAMANO_CELDA - 2, TAMANO_CELDA - 2);
        private final Text texto = new Text();

        public Celda(int x, int y, boolean tieneBomba) {
            this.x = x;
            this.y = y;
            this.tieneBomba = tieneBomba;

            configurarApariencia();
            configurarAccion();
        }

        // Configura la apariencia de la celda
        private void configurarApariencia() {
            borde.setStroke(Color.LIGHTGRAY);
            texto.setFont(Font.font(18));
            texto.setVisible(false);
            texto.setText(tieneBomba ? "X" : "");

            getChildren().addAll(borde, texto);
            setTranslateX(x * TAMANO_CELDA);
            setTranslateY(y * TAMANO_CELDA);
        }

        // Configura la acción al hacer clic en la celda
        private void configurarAccion() {
            setOnMouseClicked(e -> abrir());
        }

        // Abre la celda, revelando su contenido
        public void abrir() {
            if (estaAbierta) return;

            estaAbierta = true;
            texto.setVisible(true);
            borde.setFill(null);

            if (tieneBomba) {
                System.out.println("¡Game Over!");
                reiniciarJuego();
            } else if (texto.getText().isEmpty()) {
                obtenerVecinas(this).forEach(Celda::abrir);
            }
        }

        // Establece el texto de la celda
        public void establecerTexto(String valor) {
            texto.setText(valor);
        }
    }
}
