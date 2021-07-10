package GUI;

import org.junit.Test;

import static org.junit.Assert.*;

public class BoardTest {

    @Test
    public void checkForClear() {
        Board board = new Board();
        int points = 0;
        for (int i = 0; i < 10; i++) {
            board.setCell(i, 23, new Cell(1));
        }
        board.checkForClear();
        assertNotEquals(board.getPoints(), points);
        points = board.getPoints();

        for (int i = 0; i < 9; i++) {
            board.setCell(i, 23, new Cell(1));
        }
        board.checkForClear();
        assertEquals(board.getPoints(), points);
    }

    @Test
    public void moveDown() {
        Board board = new Board();
        board.setCell(0, 22, new Cell(1));
        board.moveDown(23);
        assertEquals(board.getCell(0, 23).getColor(), new Cell(1).getColor());
    }

    @Test
    public void generateRandomLine() {
        Board board = new Board();
        board.generateRandomLine(2);
        int count = 0;
        for (int i = 0; i < 10; i++) {
            if (board.getCell(i, 23).getColor() != (new Cell(0)).getColor()) {
                count++;
            }
        }
        assertNotEquals(count, 0);
    }
}