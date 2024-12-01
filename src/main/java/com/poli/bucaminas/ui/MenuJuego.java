package com.poli.bucaminas.ui;

import com.poli.bucaminas.modelo.Tablero;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

public class MenuJuego {
    private final Tablero tablero;

    public MenuJuego(Tablero tablero) {
        this.tablero = tablero;
    }

    public MenuBar crearBarraMenu() {
        MenuBar barraMenu = new MenuBar();
        Menu menuOpciones = new Menu("Juego nuevo");

        menuOpciones.getItems().addAll(
                crearElementoMenu("10x10", 10, 10),
                crearElementoMenu("20x15", 20, 15),
                crearElementoMenu("30x20", 30, 20)
        );

        barraMenu.getMenus().add(menuOpciones);
        return barraMenu;
    }

    private MenuItem crearElementoMenu(String etiqueta, int ancho, int alto) {
        MenuItem elemento = new MenuItem(etiqueta);
        elemento.setOnAction(e -> tablero.cambiarTamañoTablero(ancho, alto));
        return elemento;
    }
}
