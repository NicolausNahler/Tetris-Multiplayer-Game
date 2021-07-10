package GUI;

import Pieces.Coordinate;
import Pieces.Piece;

import javafx.scene.Group;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;
import java.util.stream.IntStream;

/**
 * class which realizes a tetris game environment
 */
public class Board implements Serializable {

    /**
     * the currently dropping piece
     */
    private Piece currentPiece;

    /**
     * 2D-Array: consists of Cells which realize a single Cell in a tetris game environment
     */
    private final Cell[][] grid;

    /**
     * counts the removed lines
     */
    private int rmLineCount;

    /**
     * removed lines for GUI
     */
    private int actRmLineCount;

    /**
     * represents the following piece
     */
    private Piece nextPiece;

    /**
     * boolean for declaring whether or not a swap is allowed
     */
    private boolean swapAllowed = true;

    /**
     * represents the holding piece
     */
    private Piece holdPiece;

    /**
     * represents the player's username
     */
    private String username;

    /**
     * represents the player's points
     */
    private int points = 0;

    /**
     * represents the goalPoints
     */
    private int goalPoints = 0;

    /**
     * boolean for checking whether or not finished
     */
    private boolean finished = false;

    /**
     * random number for generating the same random pieces
     */
    private Random random;

    /**
     * constructor
     */
    public Board() {
        grid = new Cell[10][24];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 24; j++) {
                grid[i][j] = new Cell();
            }
        }
        this.rmLineCount = 0;
        this.actRmLineCount = 0;
    }

    /**
     * gets the grid
     *
     * @return grid
     */
    public Cell[][] getGrid() {
        return grid;
    }

    /**
     * getter for goalPoints
     *
     * @return int
     */
    public int getGoalPoints() {
        return goalPoints;
    }

    /**
     * setter for goalPoints
     *
     * @param goalPoints goalPoints
     */
    public void setGoalPoints(int goalPoints) {
        this.goalPoints = goalPoints;
    }

    /**
     * getter for points
     *
     * @return int
     */
    public int getPoints() {
        return points;
    }

    /**
     * getter for username
     *
     * @return String
     */
    public String getUsername() {
        return username;
    }

    /**
     * sets finished to true
     */
    public void finish() {
        this.finished = true;
    }

    /**
     * getter for finished
     *
     * @return boolean
     */
    public boolean isFinished() {
        return finished;
    }

    /**
     * setter for username
     *
     * @param username username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * sets the current piece
     *
     * @param piece piece
     */
    public void setPiece(Piece piece) {
        this.currentPiece = piece;
    }

    /**
     * sets the Cell of a Coordinate
     *
     * @param coordinate coordinate
     * @param color      color
     */
    public void setCell(Coordinate coordinate, int color) {
        grid[coordinate.getX()][coordinate.getY()] = new Cell(color);
    }

    /**
     * gets the removed line counter
     *
     * @return rmLineCount
     */
    public int getRmLineCount() {
        return rmLineCount;
    }

    /**
     * getter for currentPiece
     *
     * @return Piece
     */
    public Piece getCurrentPiece() {
        return currentPiece;
    }

    /**
     * setter for random
     *
     * @param random random
     */
    public void setRandom(Random random) {
        this.random = random;
        currentPiece = Piece.generateNewPiece(random.nextInt(7) + 1);
        nextPiece = Piece.generateNewPiece(random.nextInt(7) + 1);
    }

    /**
     * setter for rmLineCount
     *
     * @param rmLineCount rmLineCount
     */
    public void setRmLineCount(int rmLineCount) {
        this.rmLineCount = rmLineCount;
    }

    /**
     * gets a group which realizes the board
     *
     * @return group
     */
    public Group getBoard() {
        Group group = new Group();
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 24; j++) {
                Rectangle rect = grid[i][j].getCell();
                rect.setX(i * 30 + 160);
                rect.setY(j * 30);
                group.getChildren().addAll(rect);
            }
        }
        if (currentPiece != null) {
            currentPiece.getCoordinates().forEach(n -> {
                Rectangle rect = new Cell(this.currentPiece.getColor()).getCell();
                rect.setX(n.getX() * 30 + 160);
                rect.setY(n.getY() * 30);
                group.getChildren().add(rect);
            });
        }
        group.getChildren().add(new Rectangle(461, 0, 150, 120));
        group.getChildren().add(new Rectangle(9, 0, 150, 120));
        if (nextPiece != null) {
            ArrayList<Coordinate> nextPiece = new ArrayList<>(this.nextPiece.getCoordinates());
            nextPiece.forEach(n -> {
                Rectangle rect = new Cell(this.nextPiece.getColor()).getCell();
                switch (this.nextPiece.getColor()) {
                    case 3:
                        rect.setX(n.getX() * 30 + 387);
                        rect.setY(n.getY() * 30 + 30);
                        break;
                    case 6:
                        rect.setX(n.getX() * 30 + 387);
                        rect.setY(n.getY() * 30 + 45);
                        break;
                    default:
                        rect.setX(n.getX() * 30 + 402);
                        rect.setY(n.getY() * 30 + 30);
                        break;
                }
                group.getChildren().add(rect);
            });
        }
        if (holdPiece != null) {
            ArrayList<Coordinate> holdPiece = new ArrayList<>(this.holdPiece.getCoordinates());
            holdPiece.forEach(n -> {
                Rectangle rect = new Cell(this.holdPiece.getColor()).getCell();
                switch (this.holdPiece.getColor()) {
                    case 3:
                        rect.setX(n.getX() * 30 - 65);
                        rect.setY(n.getY() * 30 + 30);
                        break;
                    case 6:
                        rect.setX(n.getX() * 30 - 65);
                        rect.setY(n.getY() * 30 + 45);
                        break;
                    default:
                        rect.setX(n.getX() * 30 - 50);
                        rect.setY(n.getY() * 30 + 30);
                        break;
                }
                group.getChildren().add(rect);
            });
        }
        int xCoordForText = 480;
        int fontSizeForText = 20;
        Text username = new Text(this.username);
        username.setX(20);
        username.setY(300);
        username.setWrappingWidth(130);
        username.setFont(new Font(fontSizeForText));
        Text goalPointsText = null;
        if (goalPoints != 0) {
            goalPointsText = new Text("Goal points: \n  " + goalPoints);
            goalPointsText.setX(xCoordForText);
            goalPointsText.setY(200);
            goalPointsText.setFont(new Font(fontSizeForText));
        }
        Text pointsText = new Text("Points: " + points);
        pointsText.setX(xCoordForText);
        pointsText.setY(300);
        pointsText.setFont(new Font(fontSizeForText));
        Text levelText = new Text("Level:  " + rmLineCount / 10);
        levelText.setX(xCoordForText);
        levelText.setY(400);
        levelText.setFont(new Font(fontSizeForText));
        Text rmLineCountText = new Text("Lines:  " + actRmLineCount);
        rmLineCountText.setX(xCoordForText);
        rmLineCountText.setY(500);
        rmLineCountText.setFont(new Font(fontSizeForText));
        if (goalPoints != 0) group.getChildren().addAll(goalPointsText);
        group.getChildren().addAll(pointsText, levelText, rmLineCountText, username);
        return group;
    }

    /**
     * checks whether the Cell is free
     *
     * @param x x value
     * @param y y value
     * @return boolean
     */
    public boolean isFreeCell(int x, int y) {
        try {
            return grid[x][y].isFree();
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
    }

    /**
     * check if a row is filled
     * if the row is filled, the row is removed
     */
    public void checkForClear() {
        int oldRmLineCount = rmLineCount;
        boolean isFilled;
        for (int col = 0; col < 24; col++) {
            isFilled = true;
            for (int row = 0; row < 10; row++) {
                if (this.getGrid()[row][col].isFree()) {
                    isFilled = false;
                }
            }
            if (isFilled) {
                for (int x = 0; x < 10; x++) {
                    this.setCell(new Coordinate(x, col), 0);
                }
                rmLineCount++;
                actRmLineCount++;
                moveDown(col);
            }
        }
        int removedLines = rmLineCount - oldRmLineCount;
        if (removedLines == 1) points += 40 * (rmLineCount / 10 + 1);
        else if (removedLines == 2) points += 100 * (rmLineCount / 10 + 1);
        else if (removedLines == 3) points += 300 * (rmLineCount / 10 + 1);
        else if (removedLines >= 4) points += 1200 * (rmLineCount / 10 + 1);
    }

    /**
     * moves all the rows above the cleared row 1 row down
     *
     * @param clearedRow the cleared row
     */
    public void moveDown(int clearedRow) {
        for (int y = clearedRow; y > 0; y--) {
            for (int x = 0; x < 10; x++) {
                grid[x][y] = grid[x][y - 1];
            }
        }
        IntStream.range(0, 10).forEach(i -> grid[i][0] = new Cell());
    }

    /**
     * adds random lines at the bottom of the board, used in multiplayer
     *
     * @param lines amount of lines removed from the other players board
     */
    public void generateRandomLine(int lines) {
        if (lines < 2) {
            return;
        } else if (lines < 4) {
            lines -= 1;
        }
        for (int y = 0; y < 24 - lines; y++) {
            for (int x = 0; x < 10; x++) {
                grid[x][y] = grid[x][y + lines];
            }
        }
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < lines; y++) {
                if (Math.random() < 0.5) {
                    grid[x][23 - y] = new Cell();
                } else {
                    grid[x][23 - y] = new Cell(8);
                }
            }
        }
    }

    /**
     * updates the current and next Piece
     *
     * @return Piece
     */
    public Piece updatePiece() {
        this.currentPiece = nextPiece;
        nextPiece = Piece.generateNewPiece(random.nextInt(7) + 1);
        swapAllowed = true;
        return currentPiece;
    }

    /**
     * swaps the hold piece
     */
    public void swapHoldPiece() {
        if (!swapAllowed) {
            return;
        }
        if (holdPiece == null) {
            this.holdPiece = Piece.generateNewPiece(currentPiece.getColor());
            updatePiece();
            swapAllowed = false;
            return;
        }
        Piece hold = holdPiece;
        this.holdPiece = Piece.generateNewPiece(currentPiece.getColor());
        currentPiece = hold;
        swapAllowed = false;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (Cell[] cells : grid) {
            for (Cell cell : cells) {
                result.append(cell.getColor());
            }
        }
        return result.toString();
    }

    /**
     * Sets a cell at a specific location
     *
     * @param x    x
     * @param y    y
     * @param cell cell to set
     */
    public void setCell(int x, int y, Cell cell) {
        grid[x][y] = cell;
    }

    /**
     * Gets a cell at a specific location
     *
     * @param x x
     * @param y y
     * @return cell at x,y
     */
    public Cell getCell(int x, int y) {
        return grid[x][y];
    }
}

