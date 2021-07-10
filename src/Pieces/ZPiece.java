package Pieces;

/**
 * class realizes a ZPiece
 */
public class ZPiece extends Piece {

    /**
     * constructor: sets the color and start coordinates
     */
    public ZPiece() {
        this.color = 1;
        coordinates.add(new Coordinate(3, 0));
        coordinates.add(new Coordinate(4, 1));
        coordinates.add(new Coordinate(4, 0));
        coordinates.add(new Coordinate(5, 1));
    }
}
