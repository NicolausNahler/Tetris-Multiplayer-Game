package Pieces;

import org.junit.Test;

import static org.junit.Assert.*;

public class CoordinateTest {

    @Test
    public void translateRight() {
        Coordinate c1 = new Coordinate(5, 4);
        Coordinate c2 = new Coordinate(4, 5);
        Coordinate center = new Coordinate(4, 4);
        c1 = c1.translateRight(center);
        assertEquals(c1.getX(), c2.getX());
        assertEquals(c1.getY(), c2.getY());
    }

    @Test
    public void translateLeft() {
        Coordinate c1 = new Coordinate(5, 4);
        Coordinate c2 = new Coordinate(4, 3);
        Coordinate center = new Coordinate(4, 4);
        c1 = c1.translateLeft(center);
        assertEquals(c1.getX(), c2.getX());
        assertEquals(c1.getY(), c2.getY());
    }
}