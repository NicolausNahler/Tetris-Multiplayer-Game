package Pieces;

/**
 * class realizes a ReversedZPiece
 */
public class ReversedZPiece extends Piece {

    /**
     * constructor: sets the color and start coordinates
     */
    public ReversedZPiece() {
        this.color = 5;
        coordinates.add(new Coordinate(3, 1));
        coordinates.add(new Coordinate(4, 1));
        coordinates.add(new Coordinate(4, 0));
        coordinates.add(new Coordinate(5, 0));
    }
}
