package upeu.edu.pe.ajedres.model;

import java.util.ArrayList;
import java.util.List;

public class EstadoJuego {
    private Pieza[][] tablero;
    private Pieza.Color turnoActual;
    private List<String> historialMovimientos;

    public EstadoJuego() {
        tablero = new Pieza[8][8];
        turnoActual = Pieza.Color.BLANCO;
        historialMovimientos = new ArrayList<>();
        inicializarTablero();
    }

    private void inicializarTablero() {
        // Colocar peones
        for (int i = 0; i < 8; i++) {
            tablero[1][i] = new Pieza(Pieza.Tipo.PEON, Pieza.Color.NEGRO, 1, i);
            tablero[6][i] = new Pieza(Pieza.Tipo.PEON, Pieza.Color.BLANCO, 6, i);
        }

        // Colocar torres
        tablero[0][0] = new Pieza(Pieza.Tipo.TORRE, Pieza.Color.NEGRO, 0, 0);
        tablero[0][7] = new Pieza(Pieza.Tipo.TORRE, Pieza.Color.NEGRO, 0, 7);
        tablero[7][0] = new Pieza(Pieza.Tipo.TORRE, Pieza.Color.BLANCO, 7, 0);
        tablero[7][7] = new Pieza(Pieza.Tipo.TORRE, Pieza.Color.BLANCO, 7, 7);

        // Colocar caballos
        tablero[0][1] = new Pieza(Pieza.Tipo.CABALLO, Pieza.Color.NEGRO, 0, 1);
        tablero[0][6] = new Pieza(Pieza.Tipo.CABALLO, Pieza.Color.NEGRO, 0, 6);
        tablero[7][1] = new Pieza(Pieza.Tipo.CABALLO, Pieza.Color.BLANCO, 7, 1);
        tablero[7][6] = new Pieza(Pieza.Tipo.CABALLO, Pieza.Color.BLANCO, 7, 6);

        // Colocar alfiles
        tablero[0][2] = new Pieza(Pieza.Tipo.ALFIL, Pieza.Color.NEGRO, 0, 2);
        tablero[0][5] = new Pieza(Pieza.Tipo.ALFIL, Pieza.Color.NEGRO, 0, 5);
        tablero[7][2] = new Pieza(Pieza.Tipo.ALFIL, Pieza.Color.BLANCO, 7, 2);
        tablero[7][5] = new Pieza(Pieza.Tipo.ALFIL, Pieza.Color.BLANCO, 7, 5);

        // Colocar reinas
        tablero[0][3] = new Pieza(Pieza.Tipo.REINA, Pieza.Color.NEGRO, 0, 3);
        tablero[7][3] = new Pieza(Pieza.Tipo.REINA, Pieza.Color.BLANCO, 7, 3);

        // Colocar reyes
        tablero[0][4] = new Pieza(Pieza.Tipo.REY, Pieza.Color.NEGRO, 0, 4);
        tablero[7][4] = new Pieza(Pieza.Tipo.REY, Pieza.Color.BLANCO, 7, 4);
    }

    public boolean moverPieza(int filaOrigen, int columnaOrigen, int filaDestino, int columnaDestino) {
        Pieza pieza = tablero[filaOrigen][columnaOrigen];
        
        if (pieza == null || pieza.getColor() != turnoActual) {
            return false;
        }

        if (!pieza.esMovimientoValido(filaDestino, columnaDestino)) {
            return false;
        }

        // Realizar el movimiento
        tablero[filaDestino][columnaDestino] = pieza;
        tablero[filaOrigen][columnaOrigen] = null;
        pieza.setFila(filaDestino);
        pieza.setColumna(columnaDestino);

        // Registrar el movimiento
        String movimiento = String.format("%s,%d%d,%d%d", 
            pieza.getTipo().toString(),
            filaOrigen, columnaOrigen,
            filaDestino, columnaDestino);
        historialMovimientos.add(movimiento);

        // Cambiar el turno
        turnoActual = (turnoActual == Pieza.Color.BLANCO) ? 
                     Pieza.Color.NEGRO : Pieza.Color.BLANCO;

        return true;
    }

    public Pieza[][] getTablero() {
        return tablero;
    }

    public Pieza.Color getTurnoActual() {
        return turnoActual;
    }

    public List<String> getHistorialMovimientos() {
        return historialMovimientos;
    }

    public void reiniciar() {
        tablero = new Pieza[8][8];
        turnoActual = Pieza.Color.BLANCO;
        historialMovimientos.clear();
        inicializarTablero();
    }
} 