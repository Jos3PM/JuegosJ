package upeu.edu.pe.ajedres.controller;

import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import upeu.edu.pe.ajedres.model.Board;
import upeu.edu.pe.ajedres.model.Piece;

public class CheckersController {
    @FXML
    private GridPane boardGrid;

    private Board board;

    public void initialize() {
        board = new Board();
        drawBoard();
    }

    private void drawBoard() {
        boardGrid.getChildren().clear();
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Rectangle cell = new Rectangle(60, 60);
                cell.setFill((row + col) % 2 == 0 ? Color.BEIGE : Color.BROWN);
                boardGrid.add(cell, col, row);

                Piece piece = board.getPiece(row, col);
                if (piece != null) {
                    Rectangle pieceShape = new Rectangle(50, 50);
                    pieceShape.setFill(piece.getColor().equals("white") ? Color.WHITE : Color.BLACK);
                    boardGrid.add(pieceShape, col, row);
                }
            }
        }
    }
}