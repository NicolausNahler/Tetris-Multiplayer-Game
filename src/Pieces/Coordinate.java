package Pieces;

import java.io.Serializable;
import java.util.Objects;

/**
 * class realizes a Coordinates
 */
public class Coordinate implements Serializable {

    /**
     * value y of the Coordinate
     */
    private int x;

    /**
     * value y of the Coordinate
     */
    private int y;

    /**
     * constructor
     *
     * @param x x
     * @param y y
     */
    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * gets x
     *
     * @return x
     */
    public int getX() {
        return x;
    }

    /**
     * gets y
     *
     * @return y
     */
    public int getY() {
        return y;
    }

    /**
     * sets x
     *
     * @param x x value
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * sets y
     *
     * @param y y value
     */
    public void setY(int y) {
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinate that = (Coordinate) o;
        return x == that.x && y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    /**
     * translates a Coordinate to a new Coordinate which will be rotated in a clock-wise manner
     *
     * @param center Coordinate
     * @return new Coordinate
     */
    public Coordinate translateLeft(Coordinate center) {
        int newY = center.getX() + center.getY() - x;
        int newX = y + center.getX() - center.getY();
        return new Coordinate(newX, newY);
    }

    /**
     * translates a Coordinate to a new Coordinate which will be rotated in a counter clock-wise manner
     *
     * @param center Coordinate
     * @return new Coordinate
     */
    public Coordinate translateRight(Coordinate center) {
        int newY = x + center.getY() - center.getX();
        int newX = center.getX() + center.getY() - y;
        return new Coordinate(newX, newY);
    }
}
