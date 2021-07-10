package Pieces;

/**
 * class realizes a StraightPiece
 */
public class StraightPiece extends Piece {

    /**
     * constructor: sets the color and start coordinates
     */
    public StraightPiece() {
        this.color = 6;
        coordinates.add(new Coordinate(3, 0));
        coordinates.add(new Coordinate(4, 0));
        coordinates.add(new Coordinate(5, 0));
        coordinates.add(new Coordinate(6, 0));
    }
}
