package Pieces;

/**
 * class realizes a TPiece
 */
public class TPiece extends Piece {

    /**
     * constructor: sets the color and start coordinates
     */
    public TPiece() {
        this.color = 7;
        coordinates.add(new Coordinate(3, 1));
        coordinates.add(new Coordinate(4, 0));
        coordinates.add(new Coordinate(4, 1));
        coordinates.add(new Coordinate(5, 1));
    }
}
