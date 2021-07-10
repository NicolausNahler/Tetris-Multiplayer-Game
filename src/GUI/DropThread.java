package GUI;

import Input.KeyListener;
import Pieces.Coordinate;
import Pieces.Piece;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;

import static Pieces.Piece.generateNewPiece;

/**
 * class responsible for dropping the Piece
 * it also controls the speed with which the Pieces drop
 */
public class DropThread extends Thread {

    /**
     * the Piece's speed
     */
    private int speed;

    /**
     * the Piece
     */
    private Piece piece;

    /**
     * board which represents a tetris game environment
     */
    private final Board board;

    /**
     * keyListener input manages all keyboard inputs
     */
    private final KeyListener input;

    /**
     * stores all the levels with their dropping speed
     */
    private final Map<Integer, Integer> speedMap = new HashMap<Integer, Integer>() {{
        put(0, 768);
        put(1, 688);
        put(2, 608);
        put(3, 528);
        put(4, 448);
        put(5, 368);
        put(6, 288);
        put(7, 208);
        put(8, 128);
        put(9, 96);
        put(10, 80);
        put(11, 80);
        put(12, 80);
        put(13, 64);
        put(14, 64);
        put(15, 64);
        put(16, 48);
        put(17, 48);
        put(18, 48);
        put(19, 32);
        put(20, 32);
        put(21, 32);
        put(22, 32);
        put(23, 32);
        put(24, 32);
        put(25, 32);
        put(26, 32);
        put(27, 32);
        put(28, 32);
        put(29, 16);
    }};

    /**
     * boolean which enables soft drop
     */
    private boolean softDrop = false;

    /**
     * boolean for canceling the thread
     */
    private boolean cancel = false;

    /**
     * boolean for flagging the multiplayer mode
     */
    private final boolean multiplayer;

    /**
     * board of the other player
     */
    private Board multiplayerBoard;

    /**
     * constructor
     *
     * @param input input
     * @param board board
     */
    public DropThread(KeyListener input, Board board, boolean multiplayer) {
        this.piece = generateNewPiece();
        this.input = input;
        this.board = board;
        this.multiplayer = multiplayer;
        this.speed = Integer.min(768, speedMap.get(board.getRmLineCount() / 10));
    }

    @Override
    public void run() {
        this.piece = board.getCurrentPiece();
        input.updatePiece(piece);
        boolean newPiece;
        breakPoint:
        while (true) {
            if (cancel) {
                try {
                    if (!multiplayer) {
                        finishGame();
                    }
                } catch (IOException e) {
                    System.out.println("Can't save the highscore");
                }
                return;
            }
            newPiece = false;
            try {
                if (softDrop) {
                    Thread.sleep(64);
                } else {
                    Thread.sleep(speed);
                }
            } catch (InterruptedException ignored) {

            }
            synchronized (piece) {
                int maxY = piece.getCoordinates().stream().map(Coordinate::getY).max(Integer::compare).get();
                if (maxY == 23 || checkForDrop()) {
                    piece.getCoordinates().forEach(n -> board.setCell(n, piece.getColor()));
                    for (int i = 0; i < 10; i++) {
                        if (!board.isFreeCell(i, 0)) {
                            board.finish();
                            if (!multiplayer) {
                                try {
                                    finishGame();
                                    return;
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                if (multiplayerBoard.isFinished()) {
                                    try {
                                        Controller controller = new Controller();
                                        controller.awardCeremonyMulti(board.getUsername(), multiplayerBoard.getUsername(), board.getPoints(), multiplayerBoard.getPoints());
                                        return;
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    break breakPoint;
                                }
                            }
                        }
                    }
                    board.checkForClear();
                    speed = speedMap.get(board.getRmLineCount() / 10);
                    this.piece = board.updatePiece();
                    input.updatePiece(piece);
                    newPiece = true;
                }
            }
            if (!newPiece) {
                piece.updateCoordinates(0, 1);
            }
            if (!multiplayer && board.getGoalPoints() != 0 && board.getGoalPoints() <= board.getPoints()) {
                try {
                    finishGame();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return;
            }
        }
        while (!multiplayerBoard.isFinished()) {
            try {
                Thread.sleep(20);
            } catch (InterruptedException ignored) {

            }
        }
        try {
            Controller controller = new Controller();
            controller.awardCeremonyMulti(board.getUsername(), multiplayerBoard.getUsername(), board.getPoints(), multiplayerBoard.getPoints());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * writes the highscore in the highscore file
     *
     * @throws IOException IOException
     */
    private void finishGame() throws IOException {
        File highscore = new File("highscore.txt");
        highscore.createNewFile();
        Files.write(Paths.get(highscore.toString()), (System.lineSeparator() + board.getUsername() + " - " + board.getPoints()).getBytes(), StandardOpenOption.APPEND);
        Controller controller = new Controller();
        controller.awardCeremony(board.getPoints());
    }

    /**
     * check whether the piece can be dropped
     *
     * @return boolean
     */
    public boolean checkForDrop() {
        Coordinate coord = piece.getCoordinates().stream().filter(c -> !board.isFreeCell(c.getX(), c.getY() + 1)).findFirst().orElse(new Coordinate(-1, -1));
        return coord.getX() != -1;
    }

    /**
     * changes this.softDrop to true to speed up the game
     *
     * @param softDrop if SHIFT is pressed
     */
    public void changeSoftDrop(boolean softDrop) {
        this.softDrop = softDrop;
    }

    /**
     * sets cancel to true
     */
    public void cancel() {
        this.cancel = true;
    }

    /**
     * setter for piece
     *
     * @param piece piece
     */
    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    /**
     * sets the board of the opponent
     *
     * @param multiplayerBoard board
     */
    public void setMultiplayerBoard(Board multiplayerBoard) {
        this.multiplayerBoard = multiplayerBoard;
    }
}
