package Pieces;

/**
 * class realizes a SquarePiece
 */
public class SquarePiece extends Piece {

    /**
     * constructor: sets the color and start coordinates
     */
    public SquarePiece() {
        this.color = 3;
        coordinates.add(new Coordinate(4, 0));
        coordinates.add(new Coordinate(5, 0));
        coordinates.add(new Coordinate(4, 1));
        coordinates.add(new Coordinate(5, 1));
    }
}
