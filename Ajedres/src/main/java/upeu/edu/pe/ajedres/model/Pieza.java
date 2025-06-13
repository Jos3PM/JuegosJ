package upeu.edu.pe.ajedres.model;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Pieza {
    public enum Tipo {
        PEON, TORRE, CABALLO, ALFIL, REINA, REY
    }

    public enum Color {
        BLANCO, NEGRO
    }

    private Tipo tipo;
    private Color color;
    private ImageView imagen;
    private int fila;
    private int columna;

    public Pieza(Tipo tipo, Color color, int fila, int columna) {
        this.tipo = tipo;
        this.color = color;
        this.fila = fila;
        this.columna = columna;
        cargarImagen();
    }

    private void cargarImagen() {
        String rutaImagen = "/images/" + color.toString().toLowerCase() + "_" + 
                          tipo.toString().toLowerCase() + ".png";
        try {
            Image image = new Image(getClass().getResourceAsStream(rutaImagen));
            imagen = new ImageView(image);
            imagen.setFitWidth(60);
            imagen.setFitHeight(60);
        } catch (Exception e) {
            System.err.println("Error al cargar la imagen: " + rutaImagen);
            e.printStackTrace();
        }
    }

    public boolean esMovimientoValido(int filaDestino, int columnaDestino) {
        switch (tipo) {
            case PEON:
                return esMovimientoValidoPeon(filaDestino, columnaDestino);
            case TORRE:
                return esMovimientoValidoTorre(filaDestino, columnaDestino);
            case CABALLO:
                return esMovimientoValidoCaballo(filaDestino, columnaDestino);
            case ALFIL:
                return esMovimientoValidoAlfil(filaDestino, columnaDestino);
            case REINA:
                return esMovimientoValidoReina(filaDestino, columnaDestino);
            case REY:
                return esMovimientoValidoRey(filaDestino, columnaDestino);
            default:
                return false;
        }
    }

    private boolean esMovimientoValidoPeon(int filaDestino, int columnaDestino) {
        int direccion = (color == Color.BLANCO) ? -1 : 1;
        int distanciaFila = filaDestino - fila;
        int distanciaColumna = Math.abs(columnaDestino - columna);

        // Movimiento hacia adelante
        if (distanciaColumna == 0 && distanciaFila == direccion) {
            return true;
        }

        // Primer movimiento (dos casillas)
        if (distanciaColumna == 0 && distanciaFila == 2 * direccion && 
            ((color == Color.BLANCO && fila == 6) || (color == Color.NEGRO && fila == 1))) {
            return true;
        }

        // Captura diagonal
        return distanciaColumna == 1 && distanciaFila == direccion;
    }

    private boolean esMovimientoValidoTorre(int filaDestino, int columnaDestino) {
        return fila == filaDestino || columna == columnaDestino;
    }

    private boolean esMovimientoValidoCaballo(int filaDestino, int columnaDestino) {
        int distanciaFila = Math.abs(filaDestino - fila);
        int distanciaColumna = Math.abs(columnaDestino - columna);
        return (distanciaFila == 2 && distanciaColumna == 1) || 
               (distanciaFila == 1 && distanciaColumna == 2);
    }

    private boolean esMovimientoValidoAlfil(int filaDestino, int columnaDestino) {
        return Math.abs(filaDestino - fila) == Math.abs(columnaDestino - columna);
    }

    private boolean esMovimientoValidoReina(int filaDestino, int columnaDestino) {
        return esMovimientoValidoTorre(filaDestino, columnaDestino) || 
               esMovimientoValidoAlfil(filaDestino, columnaDestino);
    }

    private boolean esMovimientoValidoRey(int filaDestino, int columnaDestino) {
        int distanciaFila = Math.abs(filaDestino - fila);
        int distanciaColumna = Math.abs(columnaDestino - columna);
        return distanciaFila <= 1 && distanciaColumna <= 1;
    }

    // Getters y setters
    public Tipo getTipo() { return tipo; }
    public Color getColor() { return color; }
    public ImageView getImagen() { return imagen; }
    public int getFila() { return fila; }
    public void setFila(int fila) { this.fila = fila; }
    public int getColumna() { return columna; }
    public void setColumna(int columna) { this.columna = columna; }
} 