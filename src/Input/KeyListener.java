package Input;

import java.util.HashMap;
import java.util.Map;

import GUI.Board;
import GUI.DropThread;
import Pieces.Coordinate;
import Pieces.Piece;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

/**
 * class manages all keyboard inputs
 */
public class KeyListener extends Thread {

    /**
     * map with all keyCodes and a boolean value so see if the key is pressed
     */
    private final Map<KeyCode, Boolean> inputs = new HashMap<>();

    /**
     * scene which shows the GUI
     */
    private final Scene scene;

    /**
     * board which represents a tetris game environment
     */
    private final Board board;

    /**
     * piece which represents a tetris game Piece
     */
    private Piece piece;

    /**
     * dropThread is a Thread responsible for lowering the Piece every time
     */
    private DropThread dropThread;

    /**
     * boolean for canceling the thread
     */
    private boolean cancel = false;

    /**
     * constructor
     *
     * @param scene scene
     * @param board board
     */
    public KeyListener(Scene scene, Board board) {
        this.scene = scene;
        this.board = board;
        inputs.put(KeyCode.UP, Boolean.FALSE);
        inputs.put(KeyCode.DOWN, Boolean.FALSE);
        inputs.put(KeyCode.LEFT, Boolean.FALSE);
        inputs.put(KeyCode.RIGHT, Boolean.FALSE);
        inputs.put(KeyCode.C, Boolean.FALSE);
        inputs.put(KeyCode.SPACE, Boolean.FALSE);
        inputs.put(KeyCode.SHIFT, Boolean.FALSE);
    }

    @Override
    public void run() {
        long timer = System.currentTimeMillis();
        long rotateTimer = System.currentTimeMillis();
        long dropTimer = System.currentTimeMillis();
        boolean interrupt = false;
        while (true) {
            if (cancel) {
                break;
            }
            scene.setOnKeyPressed(event -> {
                if (inputs.containsKey(event.getCode())) inputs.put(event.getCode(), Boolean.TRUE);
            });
            scene.setOnKeyReleased(event -> {
                if (inputs.containsKey(event.getCode())) inputs.put(event.getCode(), Boolean.FALSE);
            });
            if (inputs.get(KeyCode.RIGHT) && System.currentTimeMillis() - timer >= 100) {
                synchronized (piece) {
                    Coordinate maxXCoords = piece.getCoordinates().stream()
                            .filter(c -> !board.isFreeCell(c.getX() + 1, c.getY()))
                            .findFirst()
                            .orElse(new Coordinate(-1, -1));
                    if (maxXCoords.getX() < 9 && maxXCoords.equals(new Coordinate(-1, -1))) {
                        piece.updateCoordinates(1, 0);
                        timer = System.currentTimeMillis();
                    }
                }
            }
            if (inputs.get(KeyCode.LEFT) && System.currentTimeMillis() - timer >= 100) {
                synchronized (piece) {
                    Coordinate minXCoords = piece.getCoordinates().stream()
                            .filter(c -> !board.isFreeCell(c.getX() - 1, c.getY()))
                            .findFirst()
                            .orElse(new Coordinate(-1, -1));
                    if (minXCoords.getX() != 0 && minXCoords.equals(new Coordinate(-1, -1))) {
                        piece.updateCoordinates(-1, 0);
                        timer = System.currentTimeMillis();
                    }
                }
            }
            if (inputs.get(KeyCode.SPACE) && System.currentTimeMillis() - dropTimer >= 300) {
                Coordinate[] coord = piece.getCoordinates().toArray(new Coordinate[0]);
                while (board.isFreeCell(coord[0].getX(), coord[0].getY() + 1)
                        && board.isFreeCell(coord[1].getX(), coord[1].getY() + 1)
                        && board.isFreeCell(coord[2].getX(), coord[2].getY() + 1)
                        && board.isFreeCell(coord[3].getX(), coord[3].getY() + 1)) {
                    piece.updateCoordinates(0, 1);
                }
                dropThread.interrupt();
                board.checkForClear();
                dropTimer = System.currentTimeMillis();
            }
            if (inputs.get(KeyCode.DOWN) && System.currentTimeMillis() - rotateTimer >= 200 && piece.getColor() != 3) {
                piece.rotateLeft(board);
                rotateTimer = System.currentTimeMillis();
            }
            if (inputs.get(KeyCode.UP) && System.currentTimeMillis() - rotateTimer >= 200 && piece.getColor() != 3) {
                piece.rotateRight(board);
                rotateTimer = System.currentTimeMillis();
            }
            if (inputs.get(KeyCode.C) && System.currentTimeMillis() - rotateTimer >= 200) {
                synchronized (board) {
                    board.swapHoldPiece();
                    piece = board.getCurrentPiece();
                    dropThread.setPiece(piece);
                }
               rotateTimer = System.currentTimeMillis();
            }
            if (inputs.get(KeyCode.SHIFT) && interrupt) {
                dropThread.interrupt();
                interrupt = false;
            } else if (!inputs.get(KeyCode.SHIFT)) {
                interrupt = true;
            }
            dropThread.changeSoftDrop(inputs.get(KeyCode.SHIFT));
            try {
                Thread.sleep(16);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * updates a Piece
     *
     * @param piece piece
     */
    public void updatePiece(Piece piece) {
        this.piece = piece;
    }

    /**
     * sets the DropThread
     *
     * @param dropThread dropThread
     */
    public void setDropThread(DropThread dropThread) {
        this.dropThread = dropThread;
    }

    /**
     * sets cancel to true
     */
    public void cancel() {
        this.cancel = true;
    }
}
