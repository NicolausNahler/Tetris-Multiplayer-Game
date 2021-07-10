package Connection;

import GUI.*;
import Input.KeyListener;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

/**
 * class handles connection to Server
 */
public class ServerHandler extends Thread {

    /**
     * boolean for checking whether or not the game (threads) started
     */
    private boolean notStarted = true;

    /**
     * stage
     */
    private final Stage primaryStage;

    /**
     * board
     */
    private final Board board;

    /**
     * boolean for canceling the game (threads)
     */
    private boolean cancel = false;

    /**
     * represents the IP Address of the server
     */
    private String IPAddress;

    /**
     * socket for the connection
     */
    private Socket connection;

    /**
     * constructor
     *
     * @param primaryStage stage
     * @param board        board
     */
    public ServerHandler(Stage primaryStage, Board board) {
        this.primaryStage = primaryStage;
        this.board = board;
    }

    @Override
    public void run() {
        DrawThread drawThread = new DrawThread(primaryStage, board, true);
        KeyListener input = new KeyListener(drawThread.getScene(), board);
        DropThread dropThread = new DropThread(input, board, true);
        Board multiBoard = new Board();
        input.setDropThread(dropThread);
        Socket connection = new Socket();
        this.connection = connection;
        ObjectOutputStream out;
        ObjectInputStream in;
        try {
            connection.connect(new InetSocketAddress(IPAddress, Server.PORT));
            out = new ObjectOutputStream(connection.getOutputStream());
            in = new ObjectInputStream(connection.getInputStream());
        } catch (IOException e) {
            System.out.println("Connection failed");
            return;
        }
        while (true) {
            if (cancel) {
                input.cancel();
                drawThread.cancel();
                dropThread.cancel();
                try {
                    connection.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
            try {
                try {
                    Board board = drawThread.getBoard();
                    out.writeObject(board);
                    out.flush();
                    out.reset();
                    NetworkPackage networkPackage = (NetworkPackage) in.readObject();
                    if (notStarted) {
                        Main.changeScene("/FXML/modeSelection.fxml");
                        board.setRandom(networkPackage.getRandom());
                        dropThread.start();
                        drawThread.setMultiplayerBoard(networkPackage.getBoard());
                        input.start();
                        drawThread.start();
                        notStarted = false;
                    }
                    board.generateRandomLine(networkPackage.getLinesToAdd());
                    multiBoard = networkPackage.getBoard();
                    drawThread.setMultiplayerBoard(multiBoard);
                    dropThread.setMultiplayerBoard(multiBoard);
                    Thread.sleep(10);
                } catch (SocketException | EOFException e) {
                    if(!notStarted && board.isFinished() && !multiBoard.isFinished()) {
                        Platform.runLater(() -> {
                            Controller controller = new Controller();
                            try {
                                controller.showConnectionLossWin();
                            } catch (IOException ioException) {
                                System.out.println("Can't update Application Thread");
                            }
                        });
                        this.cancel();
                    }
                }
            } catch (IOException | InterruptedException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * sets cancel to true
     */
    public void cancel() {
        this.cancel = true;
    }

    /**
     * setter for IP Address
     *
     * @param IPAddress IP Address
     */
    public void setIPAddress(String IPAddress) {
        this.IPAddress = IPAddress;
    }

    /**
     * closes the connection
     *
     * @throws IOException IOException
     */
    public void close() throws IOException {
        connection.close();
    }
}
