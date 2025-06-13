package upeu.edu.pe.ajedres.controller;

import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import upeu.edu.pe.ajedres.model.*;
import java.util.List;
import java.util.ArrayList;

public class TableroController {
    @FXML
    private GridPane tablero;
    
    private static final int TAMANO_CASILLA = 80;
    private static final int FILAS = 8;
    private static final int COLUMNAS = 8;
    private List<StackPane> casillas = new ArrayList<>();
    private int filaSeleccionada = -1;
    private int columnaSeleccionada = -1;
    private EstadoJuego estadoJuego;

    @FXML
    public void initialize() {
        estadoJuego = new EstadoJuego();
        crearTablero();
        actualizarTablero();
    }

    private void crearTablero() {
        for (int fila = 0; fila < FILAS; fila++) {
            for (int columna = 0; columna < COLUMNAS; columna++) {
                StackPane casilla = new StackPane();
                Rectangle rectangulo = new Rectangle(TAMANO_CASILLA, TAMANO_CASILLA);
                rectangulo.setFill((fila + columna) % 2 == 0 ? Color.WHITE : Color.GRAY);
                casilla.getChildren().add(rectangulo);
                
                final int f = fila;
                final int c = columna;
                casilla.setOnMouseClicked(event -> manejarClickCasilla(f, c));
                
                tablero.add(casilla, columna, fila);
                casillas.add(casilla);
            }
        }
    }

    private void actualizarTablero() {
        Pieza[][] piezas = estadoJuego.getTablero();
        for (int fila = 0; fila < FILAS; fila++) {
            for (int columna = 0; columna < COLUMNAS; columna++) {
                StackPane casilla = casillas.get(fila * COLUMNAS + columna);
                casilla.getChildren().clear();
                
                // Agregar el fondo de la casilla
                Rectangle rectangulo = new Rectangle(TAMANO_CASILLA, TAMANO_CASILLA);
                rectangulo.setFill((fila + columna) % 2 == 0 ? Color.WHITE : Color.GRAY);
                casilla.getChildren().add(rectangulo);
                
                // Agregar la pieza si existe
                Pieza pieza = piezas[fila][columna];
                if (pieza != null) {
                    casilla.getChildren().add(pieza.getImagen());
                }
            }
        }
    }

    private void manejarClickCasilla(int fila, int columna) {
        if (filaSeleccionada == -1) {
            // Primera selección
            Pieza pieza = estadoJuego.getTablero()[fila][columna];
            if (pieza != null && pieza.getColor() == estadoJuego.getTurnoActual()) {
                filaSeleccionada = fila;
                columnaSeleccionada = columna;
                marcarCasillaSeleccionada(fila, columna);
            }
        } else {
            // Segunda selección - intentar mover la pieza
            if (estadoJuego.moverPieza(filaSeleccionada, columnaSeleccionada, fila, columna)) {
                actualizarTablero();
                verificarFinJuego();
            }
            filaSeleccionada = -1;
            columnaSeleccionada = -1;
            actualizarTablero();
        }
    }

    private void marcarCasillaSeleccionada(int fila, int columna) {
        StackPane casilla = casillas.get(fila * COLUMNAS + columna);
        Rectangle rectangulo = (Rectangle) casilla.getChildren().get(0);
        rectangulo.setStroke(Color.YELLOW);
        rectangulo.setStrokeWidth(3);
    }

    private void verificarFinJuego() {
        // Aquí se implementaría la lógica para verificar jaque mate o tablas
    }

    @FXML
    private void nuevaPartida() {
        estadoJuego.reiniciar();
        actualizarTablero();
    }
} 