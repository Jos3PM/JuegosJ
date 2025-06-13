package upeu.edu.pe.ajedres.model;

import java.util.ArrayList;
import java.util.List;

public class EstadoJuego {
    private Pieza[][] tablero;
    private Pieza.Color turnoActual;
    private List<String> historialMovimientos;
    private boolean juegoTerminado;
    private String ganador;

    public EstadoJuego() {
        tablero = new Pieza[8][8];
        turnoActual = Pieza.Color.BLANCO;
        historialMovimientos = new ArrayList<>();
        juegoTerminado = false;
        ganador = null;
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
        if (juegoTerminado) {
            return false;
        }

        Pieza pieza = tablero[filaOrigen][columnaOrigen];
        
        if (pieza == null || pieza.getColor() != turnoActual) {
            return false;
        }

        if (!esMovimientoValido(filaOrigen, columnaOrigen, filaDestino, columnaDestino)) {
            return false;
        }

        // Guardar la pieza capturada para el historial
        Pieza piezaCapturada = tablero[filaDestino][columnaDestino];

        // Realizar el movimiento
        tablero[filaDestino][columnaDestino] = pieza;
        tablero[filaOrigen][columnaOrigen] = null;
        pieza.setFila(filaDestino);
        pieza.setColumna(columnaDestino);

        // Registrar el movimiento en el historial
        String movimiento = crearNotacionMovimiento(pieza, filaOrigen, columnaOrigen, 
                                                   filaDestino, columnaDestino, piezaCapturada);
        historialMovimientos.add(movimiento);

        // Cambiar el turno
        turnoActual = (turnoActual == Pieza.Color.BLANCO) ? 
                     Pieza.Color.NEGRO : Pieza.Color.BLANCO;

        // Verificar si el juego ha terminado
        verificarFinJuego();

        return true;
    }

    private boolean esMovimientoValido(int filaOrigen, int columnaOrigen, int filaDestino, int columnaDestino) {
        // Verificar límites del tablero
        if (filaDestino < 0 || filaDestino >= 8 || columnaDestino < 0 || columnaDestino >= 8) {
            return false;
        }

        Pieza pieza = tablero[filaOrigen][columnaOrigen];
        Pieza piezaDestino = tablero[filaDestino][columnaDestino];

        // No se puede capturar una pieza del mismo color
        if (piezaDestino != null && piezaDestino.getColor() == pieza.getColor()) {
            return false;
        }

        // Verificar si el movimiento es válido para la pieza
        if (!pieza.esMovimientoValido(filaDestino, columnaDestino)) {
            return false;
        }

        // Verificar si el camino está libre (excepto para el caballo)
        if (pieza.getTipo() != Pieza.Tipo.CABALLO) {
            if (!esCaminoLibre(filaOrigen, columnaOrigen, filaDestino, columnaDestino)) {
                return false;
            }
        }

        return true;
    }

    private boolean esCaminoLibre(int filaOrigen, int columnaOrigen, int filaDestino, int columnaDestino) {
        int deltaFila = Integer.signum(filaDestino - filaOrigen);
        int deltaColumna = Integer.signum(columnaDestino - columnaOrigen);
        
        int filaActual = filaOrigen + deltaFila;
        int columnaActual = columnaOrigen + deltaColumna;
        
        while (filaActual != filaDestino || columnaActual != columnaDestino) {
            if (tablero[filaActual][columnaActual] != null) {
                return false;
            }
            filaActual += deltaFila;
            columnaActual += deltaColumna;
        }
        
        return true;
    }

    private String crearNotacionMovimiento(Pieza pieza, int filaOrigen, int columnaOrigen, 
                                         int filaDestino, int columnaDestino, Pieza piezaCapturada) {
        StringBuilder sb = new StringBuilder();
        
        // Número de movimiento
        int numeroMovimiento = (historialMovimientos.size() / 2) + 1;
        if (pieza.getColor() == Pieza.Color.BLANCO) {
            sb.append(numeroMovimiento).append(". ");
        }
        
        // Tipo de pieza (excepto peón)
        if (pieza.getTipo() != Pieza.Tipo.PEON) {
            sb.append(pieza.getTipo().getSimbolo());
        }
        
        // Posición de origen (para peones solo si hay captura)
        if (pieza.getTipo() == Pieza.Tipo.PEON && piezaCapturada != null) {
            sb.append((char)('a' + columnaOrigen));
        }
        
        // Captura
        if (piezaCapturada != null) {
            sb.append("x");
        }
        
        // Posición de destino
        sb.append((char)('a' + columnaDestino));
        sb.append(8 - filaDestino);
        
        return sb.toString();
    }

    private void verificarFinJuego() {
        // Verificar si hay jaque mate o tablas
        // Por simplicidad, solo verificamos si quedan reyes
        boolean reyBlancoVivo = false;
        boolean reyNegroVivo = false;
        
        for (int fila = 0; fila < 8; fila++) {
            for (int columna = 0; columna < 8; columna++) {
                Pieza pieza = tablero[fila][columna];
                if (pieza != null && pieza.getTipo() == Pieza.Tipo.REY) {
                    if (pieza.getColor() == Pieza.Color.BLANCO) {
                        reyBlancoVivo = true;
                    } else {
                        reyNegroVivo = true;
                    }
                }
            }
        }
        
        if (!reyBlancoVivo) {
            juegoTerminado = true;
            ganador = "Negro";
        } else if (!reyNegroVivo) {
            juegoTerminado = true;
            ganador = "Blanco";
        }
    }

    // Getters y setters
    public Pieza[][] getTablero() { return tablero; }
    public Pieza.Color getTurnoActual() { return turnoActual; }
    public List<String> getHistorialMovimientos() { return historialMovimientos; }
    public boolean isJuegoTerminado() { return juegoTerminado; }
    public String getGanador() { return ganador; }

    public void reiniciar() {
        tablero = new Pieza[8][8];
        turnoActual = Pieza.Color.BLANCO;
        historialMovimientos.clear();
        juegoTerminado = false;
        ganador = null;
        inicializarTablero();
    }
}