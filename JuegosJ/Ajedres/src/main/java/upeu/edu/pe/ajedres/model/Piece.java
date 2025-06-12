package upeu.edu.pe.ajedres.model;

public class Piece {
    private boolean isKing;
    private String color; // "white" or "black"

    public Piece(String color) {
        this.color = color;
        this.isKing = false;
    }

    public boolean isKing() {
        return isKing;
    }

    public void makeKing() {
        this.isKing = true;
    }

    public String getColor() {
        return color;
    }
}