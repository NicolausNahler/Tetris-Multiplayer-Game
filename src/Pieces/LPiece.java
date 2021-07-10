package Pieces;

/**
 * class realizes a LPiece
 */
public class LPiece extends Piece {

    /**
     * constructor: sets the color and start coordinates
     */
    public LPiece() {
        this.color = 2;
        coordinates.add(new Coordinate(3, 1));
        coordinates.add(new Coordinate(5, 1));
        coordinates.add(new Coordinate(4, 1));
        coordinates.add(new Coordinate(5, 0));
    }
}
