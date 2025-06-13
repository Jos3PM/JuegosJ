package upeu.edu.pe.ajedres.controller;

import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import upeu.edu.pe.ajedres.model.*;
import java.util.List;
import java.util.ArrayList;

public class TableroController {
    @FXML
    private GridPane tablero;
    
    @FXML
    private Label turnoLabel;
    
    @FXML
    private ListView<String> historialList;
    
    @FXML
    private Label estadoJuegoLabel;
    
    private static final int TAMANO_CASILLA = 70;
    private static final int FILAS = 8;
    private static final int COLUMNAS = 8;
    private List<StackPane> casillas = new ArrayList<>();
    private int filaSeleccionada = -1;
    private int columnaSeleccionada = -1;
    private EstadoJuego estadoJuego;
    private ObservableList<String> historialObservable;

    @FXML
    public void initialize() {
        estadoJuego = new EstadoJuego();
        historialObservable = FXCollections.observableArrayList();
        historialList.setItems(historialObservable);
        crearTablero();
        actualizarTablero();
        actualizarInterfaz();
    }

    private void crearTablero() {
        tablero.getChildren().clear();
        casillas.clear();
        
        for (int fila = 0; fila < FILAS; fila++) {
            for (int columna = 0; columna < COLUMNAS; columna++) {
                StackPane casilla = new StackPane();
                Rectangle rectangulo = new Rectangle(TAMANO_CASILLA, TAMANO_CASILLA);
                
                // Colores alternados del tablero
                if ((fila + columna) % 2 == 0) {
                    rectangulo.setFill(Color.BEIGE);
                } else {
                    rectangulo.setFill(Color.SADDLEBROWN);
                }
                
                rectangulo.setStroke(Color.BLACK);
                rectangulo.setStrokeWidth(0.5);
                casilla.getChildren().add(rectangulo);
                
                final int f = fila;
                final int c = columna;
                casilla.setOnMouseClicked(event -> manejarClickCasilla(f, c));
                
                // Agregar efecto hover
                casilla.setOnMouseEntered(event -> {
                    rectangulo.setStrokeWidth(2);
                    rectangulo.setStroke(Color.GOLD);
                });
                
                casilla.setOnMouseExited(event -> {
                    if (f != filaSeleccionada || c != columnaSeleccionada) {
                        rectangulo.setStrokeWidth(0.5);
                        rectangulo.setStroke(Color.BLACK);
                    }
                });
                
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
                
                // Limpiar la casilla pero mantener el fondo
                while (casilla.getChildren().size() > 1) {
                    casilla.getChildren().remove(1);
                }
                
                // Agregar la pieza si existe
                Pieza pieza = piezas[fila][columna];
                if (pieza != null) {
                    casilla.getChildren().add(pieza.getImagen());
                }
            }
        }
    }

    private void manejarClickCasilla(int fila, int columna) {
        if (estadoJuego.isJuegoTerminado()) {
            mostrarAlerta("Juego Terminado", "El juego ha terminado. Inicia una nueva partida.");
            return;
        }

        if (filaSeleccionada == -1) {
            // Primera selección
            Pieza pieza = estadoJuego.getTablero()[fila][columna];
            if (pieza != null && pieza.getColor() == estadoJuego.getTurnoActual()) {
                filaSeleccionada = fila;
                columnaSeleccionada = columna;
                marcarCasillaSeleccionada(fila, columna, true);
            }
        } else {
            // Segunda selección - intentar mover la pieza
            boolean movimientoExitoso = estadoJuego.moverPieza(filaSeleccionada, columnaSeleccionada, fila, columna);
            
            if (movimientoExitoso) {
                actualizarTablero();
                actualizarHistorial();
                actualizarInterfaz();
                verificarFinJuego();
            } else {
                mostrarAlerta("Movimiento Inválido", "No puedes mover la pieza a esa posición.");
            }
            
            // Limpiar selección
            marcarCasillaSeleccionada(filaSeleccionada, columnaSeleccionada, false);
            filaSeleccionada = -1;
            columnaSeleccionada = -1;
        }
    }

    private void marcarCasillaSeleccionada(int fila, int columna, boolean seleccionar) {
        StackPane casilla = casillas.get(fila * COLUMNAS + columna);
        Rectangle rectangulo = (Rectangle) casilla.getChildren().get(0);
        
        if (seleccionar) {
            rectangulo.setStroke(Color.YELLOW);
            rectangulo.setStrokeWidth(4);
        } else {
            rectangulo.setStroke(Color.BLACK);
            rectangulo.setStrokeWidth(0.5);
        }
    }

    private void actualizarHistorial() {
        historialObservable.clear();
        List<String> movimientos = estadoJuego.getHistorialMovimientos();
        
        for (int i = 0; i < movimientos.size(); i += 2) {
            StringBuilder linea = new StringBuilder();
            int numeroMovimiento = (i / 2) + 1;
            linea.append(numeroMovimiento).append(". ");
            linea.append(movimientos.get(i));
            
            if (i + 1 < movimientos.size()) {
                linea.append(" ").append(movimientos.get(i + 1));
            }
            
            historialObservable.add(linea.toString());
        }
        
        // Hacer scroll al último movimiento
        if (!historialObservable.isEmpty()) {
            historialList.scrollTo(historialObservable.size() - 1);
        }
    }

    private void actualizarInterfaz() {
        // Actualizar etiqueta de turno
        String turno = estadoJuego.getTurnoActual() == Pieza.Color.BLANCO ? "Blancas" : "Negras";
        turnoLabel.setText("Turno: " + turno);
        
        // Actualizar estado del juego
        if (estadoJuego.isJuegoTerminado()) {
            estadoJuegoLabel.setText("¡Ganador: " + estadoJuego.getGanador() + "!");
            estadoJuegoLabel.setTextFill(Color.RED);
        } else {
            estadoJuegoLabel.setText("Juego en progreso");
            estadoJuegoLabel.setTextFill(Color.GREEN);
        }
    }

    private void verificarFinJuego() {
        if (estadoJuego.isJuegoTerminado()) {
            mostrarAlerta("¡Juego Terminado!", 
                         "¡Las " + estadoJuego.getGanador() + " han ganado la partida!");
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @FXML
    private void nuevaPartida() {
        estadoJuego.reiniciar();
        historialObservable.clear();
        filaSeleccionada = -1;
        columnaSeleccionada = -1;
        actualizarTablero();
        actualizarInterfaz();
    }

    @FXML
    private void mostrarAyuda() {
        String ayuda = "CÓMO JUGAR AJEDREZ:\n\n" +
                      "• Haz clic en una pieza para seleccionarla\n" +
                      "• Haz clic en una casilla válida para mover\n" +
                      "• Las blancas mueven primero\n" +
                      "• Captura el rey enemigo para ganar\n\n" +
                      "MOVIMIENTOS DE PIEZAS:\n" +
                      "• Peón: Una casilla adelante (dos en el primer movimiento)\n" +
                      "• Torre: Horizontal y vertical\n" +
                      "• Alfil: Diagonal\n" +
                      "• Caballo: En forma de L\n" +
                      "• Reina: Combina torre y alfil\n" +
                      "• Rey: Una casilla en cualquier dirección";
        
        mostrarAlerta("Ayuda - Ajedrez", ayuda);
    }
}