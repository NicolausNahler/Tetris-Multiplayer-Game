package GUI;

import Pieces.Piece;
import Pieces.StraightPiece;
import org.junit.Test;

import static org.junit.Assert.*;

public class DropThreadTest {

    @Test
    public void checkForDrop() {
        Board board = new Board();
        Piece piece = new StraightPiece();
        piece.updateCoordinates(0, 22);
        DropThread dropThread = new DropThread(null, board, false);
        dropThread.setPiece(piece);

        for (int i = 0; i < 10; i++) {
            board.setCell(i, 23, new Cell(1));
        }
        assertTrue(dropThread.checkForDrop());
    }
}