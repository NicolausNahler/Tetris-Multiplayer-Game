package Pieces;

import GUI.Board;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * abstract class from which all the Pieces extend, realizes a general Piece
 */
public abstract class Piece implements Serializable {

    /**
     * the Piece's color
     */
    protected int color;

    /**
     * a list of all the Piece's coordinates
     */
    protected CopyOnWriteArrayList<Coordinate> coordinates = new CopyOnWriteArrayList<>();

    /**
     * gets all the Piece's coordinates
     *
     * @return coordinates
     */
    public List<Coordinate> getCoordinates() {
        return coordinates;
    }

    /**
     * gets the Piece's color
     *
     * @return color
     */
    public int getColor() {
        return this.color;
    }

    /**
     * updates all the Piece's coordinatee
     *
     * @param x x value
     * @param y y value
     */
    public void updateCoordinates(int x, int y) {
        synchronized (coordinates) {
            this.coordinates.forEach(n -> {
                n.setX(n.getX() + x);
                n.setY(n.getY() + y);
            });
        }
    }

    /**
     * generates a new Piece
     *
     * @return Piece
     */
    public static Piece generateNewPiece() {
        switch ((int) (Math.random() * 7)) {
            case 0:
                return new SquarePiece();
            case 1:
                return new StraightPiece();
            case 2:
                return new LPiece();
            case 3:
                return new ReversedLPiece();
            case 4:
                return new TPiece();
            case 5:
                return new ZPiece();
            case 6:
                return new ReversedZPiece();
            default:
                return null;
        }
    }

    /**
     * generate new Piece with specific piece
     *
     * @param piece piece
     * @return Piece
     */
    public static Piece generateNewPiece(int piece) {
        switch (piece) {
            case 1:
                return new ZPiece();
            case 2:
                return new LPiece();
            case 3:
                return new SquarePiece();
            case 4:
                return new ReversedLPiece();
            case 5:
                return new ReversedZPiece();
            case 6:
                return new StraightPiece();
            case 7:
                return new TPiece();
            default:
                return null;
        }
    }

    /**
     * rotates the Piece's coordinates in a counter clock-wise manner
     *
     * @param board board
     */
    public  void rotateLeft(Board board) {
        synchronized (this.coordinates) {
            CopyOnWriteArrayList<Coordinate> newCoords = new CopyOnWriteArrayList<>(this.coordinates);
            newCoords.replaceAll(n -> n = n.translateLeft(newCoords.get(2)));

            Coordinate checkForCollision = newCoords.stream()
                    .filter(c -> !board.isFreeCell(c.getX(), c.getY()))
                    .findFirst()
                    .orElse(new Coordinate(-1, -1));

            if (checkForCollision.equals(new Coordinate(-1, -1))) {
                this.coordinates = newCoords;
            }
        }
    }

    /**
     * rotates the Piece's coordinates in a clock-wise manner
     *
     * @param board board
     */
    public  void rotateRight(Board board) {
        synchronized (this.coordinates) {
            CopyOnWriteArrayList<Coordinate> newCoords = new CopyOnWriteArrayList<>(this.coordinates);
            newCoords.replaceAll(n -> n = n.translateRight(newCoords.get(2)));

            Coordinate checkForCollision = newCoords.stream()
                    .filter(c -> !board.isFreeCell(c.getX(), c.getY()))
                    .findFirst()
                    .orElse(new Coordinate(-1, -1));

            if (checkForCollision.equals(new Coordinate(-1, -1))) {
                this.coordinates = newCoords;
            }
        }
    }
}
