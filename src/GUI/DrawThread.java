package GUI;

import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * class responsible for drawing the game
 */
public class DrawThread extends Thread {

    /**
     * the stage which holds the groups and scenes
     */
    private final Stage primaryStage;

    /**
     * board which represents a tetris game environment
     */
    private final Board board;

    /**
     * board which represents the tetris game environment of the opponent
     */
    private Board multiplayerBoard;

    /**
     * boolean which enables multiplayer mode
     */
    private final boolean mulitplayer;

    /**
     * scene
     */
    private final Scene scene;

    /**
     * group
     */
    private final Group group = new Group();

    /**
     * boolean for canceling the thread
     */
    private boolean cancel = false;

    /**
     * constructor
     *
     * @param primaryStage primaryStage
     * @param board        board
     */
    public DrawThread(Stage primaryStage, Board board, boolean multiplayer) {
        this.board = board;
        this.primaryStage = primaryStage;
        this.mulitplayer = multiplayer;
        if (mulitplayer) {
            scene = new Scene(group, 1300, 720);
        } else {
            scene = new Scene(group, 620, 720);
        }
    }

    @Override
    public void run() {
        primaryStage.getIcons().add(new Image("images/image5.png"));
        primaryStage.setTitle("Tetris");
        Platform.runLater(() -> {
            group.getChildren().setAll(board.getBoard());
            primaryStage.setScene(scene);
            primaryStage.show();
        });
        while (true) {
            if (cancel) {
                break;
            }
            try {
                Thread.sleep(16);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Platform.runLater(() -> {
                if (mulitplayer) {
                    Group translate = multiplayerBoard.getBoard();
                    translate.setTranslateX(680);
                    group.getChildren().setAll(board.getBoard(), translate);
                } else {
                    group.getChildren().setAll(board.getBoard());
                }
                primaryStage.setScene(scene);
            });
        }
    }

    /**
     * gets the board
     *
     * @return board
     */
    public Board getBoard() {
        return this.board;
    }

    /**
     * sets the board of the opponent
     *
     * @param multiplayerBoard board
     */
    public void setMultiplayerBoard(Board multiplayerBoard) {
        this.multiplayerBoard = multiplayerBoard;
    }

    /**
     * getter for scene
     *
     * @return Scene
     */
    public Scene getScene() {
        return this.scene;
    }

    /**
     * sets cancel to true
     */
    public void cancel() {
        this.cancel = true;
    }
}
