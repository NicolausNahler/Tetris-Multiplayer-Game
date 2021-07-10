package GUI;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.Serializable;

/**
 * class realizes a Cell in the Board
 */
public class Cell implements Serializable {

    /**
     * the Cell's color
     */
    private final int color;

    /**
     * sets the Cell's color
     */
    public Cell() {
        this.color = 0;
    }

    /**
     * constructor
     *
     * @param color color
     */
    public Cell(int color) {
        this.color = color;
    }

    /**
     * checks if the Cell is free
     *
     * @return boolean
     */
    public boolean isFree() {
        return color == 0;
    }

    /**
     * gets a Rectangle which represents a Cell
     *
     * @return rect
     */
    public Rectangle getCell() {
        Rectangle rect = new Rectangle(30, 30);
        switch (getColor()) {
            case 0:
                rect.setFill(Color.BLACK);
                break;
            case 1:
                rect.setFill(Color.ORANGE);
                break;
            case 2:
                rect.setFill(Color.YELLOW);
                break;
            case 3:
                rect.setFill(Color.LIGHTBLUE);
                break;
            case 4:
                rect.setFill(Color.GREEN);
                break;
            case 5:
                rect.setFill(Color.VIOLET);
                break;
            case 6:
                rect.setFill(Color.RED);
                break;
            case 7:
                rect.setFill(Color.BLUE);
                break;
            case 8:
                rect.setFill(Color.GRAY);
                break;
        }
        rect.setStroke(Color.LIGHTGREY);
        return rect;
    }

    /**
     * gets the cell's color
     *
     * @return int
     */
    public int getColor() {
        return color;
    }
}
