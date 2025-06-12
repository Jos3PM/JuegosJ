package upeu.edu.pe.ajedres.model;

public class Board {
    private Piece[][] board = new Piece[8][8];

    public Board() {
        initializeBoard();
    }

    private void initializeBoard() {
        // Place black pieces
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 8; col++) {
                if ((row + col) % 2 != 0) {
                    board[row][col] = new Piece("black");
                }
            }
        }

        // Place white pieces
        for (int row = 5; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if ((row + col) % 2 != 0) {
                    board[row][col] = new Piece("white");
                }
            }
        }
    }

    public Piece getPiece(int row, int col) {
        return board[row][col];
    }

    public void movePiece(int fromRow, int fromCol, int toRow, int toCol) {
        board[toRow][toCol] = board[fromRow][fromCol];
        board[fromRow][fromCol] = null;

        // Check if the piece should become a king
        if (toRow == 0 && board[toRow][toCol].getColor().equals("white")) {
            board[toRow][toCol].makeKing();
        } else if (toRow == 7 && board[toRow][toCol].getColor().equals("black")) {
            board[toRow][toCol].makeKing();
        }
    }

    public void printBoard() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (board[row][col] == null) {
                    System.out.print(". ");
                } else {
                    System.out.print(board[row][col].getColor().charAt(0) + " ");
                }
            }
            System.out.println();
        }
    }
}