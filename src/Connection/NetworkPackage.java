package Connection;

import GUI.Board;

import java.io.Serializable;
import java.util.Random;

/**
 * class represents a network package which is send over a TCP-IP connection
 */
public class NetworkPackage implements Serializable {

    /**
     * board
     */
    private final Board board;

    /**
     * represents the lines to be added to the the player
     */
    private final int linesToAdd;

    /**
     * random number for generating the same random pieces
     */
    private Random random;

    /**
     * constructor
     *
     * @param board      board
     * @param linesToAdd linesToAdd
     */
    public NetworkPackage(Board board, int linesToAdd) {
        this.board = board;
        this.linesToAdd = linesToAdd;
    }

    /**
     * getter for board
     *
     * @return Board
     */
    public Board getBoard() {
        return board;
    }

    /**
     * getter for the linesToAdd
     *
     * @return int
     */
    public int getLinesToAdd() {
        return linesToAdd;
    }

    /**
     * setter for random number for generating the same random pieces
     *
     * @param random random
     */
    public void setRandom(Random random) {
        this.random = random;
    }

    /**
     * getter for random number for generating the same random pieces
     *
     * @return Random
     */
    public Random getRandom() {
        return this.random;
    }
}
