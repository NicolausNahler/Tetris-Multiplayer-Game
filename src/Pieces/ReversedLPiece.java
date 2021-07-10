package Pieces;

/**
 * class realizes a ReversedLPiece
 */
public class ReversedLPiece extends Piece {

    /**
     * constructor: sets the color and start coordinates
     */
    public ReversedLPiece() {
        this.color = 4;
        coordinates.add(new Coordinate(3, 0));
        coordinates.add(new Coordinate(3, 1));
        coordinates.add(new Coordinate(4, 1));
        coordinates.add(new Coordinate(5, 1));
    }
}
