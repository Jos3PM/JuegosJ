package upeu.edu.pe.ajedres.service;

import upeu.edu.pe.ajedres.model.Board;

public class Game {
    private Board board;

    public Game() {
        board = new Board();
    }

    public void startGame() {
        System.out.println("Starting Checkers Game!");
        board.printBoard();
    }

    public void makeMove(int fromRow, int fromCol, int toRow, int toCol) {
        board.movePiece(fromRow, fromCol, toRow, toCol);
        board.printBoard();
    }
}