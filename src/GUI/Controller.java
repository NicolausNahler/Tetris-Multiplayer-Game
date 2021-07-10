package GUI;

import Connection.Server;
import Connection.ServerHandler;
import Input.KeyListener;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static GUI.Main.changeScene;

/**
 * class which controls fxml
 */
public class Controller {

    /**
     * constructor
     */
    public Controller() {
    }

    @FXML
    private TextField usernameField;
    @FXML
    private TextField addressField;
    @FXML
    private Slider levelSlider;
    @FXML
    private Slider pointSlider;

    private static String username;

    private static ServerHandler serverHandler;

    private static Server server;

    /**
     * handles login, when login button is clicked
     *
     * @throws IOException IOException
     */
    public void loginButtonOnAction() throws IOException {
        if (!usernameField.getText().trim().isEmpty()) {
            username = usernameField.getText();
            changeScene("/FXML/modeSelection.fxml");
        } else {
            usernameField.setStyle("-fx-border-color: red");
        }
    }

    /**
     * handles log out, when log out button is clicked
     *
     * @throws IOException IOException
     */
    public void logOutButtonOnAction() throws IOException {
        changeScene("/FXML/sample.fxml");
    }

    /**
     * handles go back, when go back button is clicked
     *
     * @throws IOException IOException
     */
    public void backButtonOnAction() throws IOException {
        changeScene("/FXML/modeSelection.fxml");
        if (serverHandler != null) {
            serverHandler.close();
        }
        if (server != null) {
            server.close();
        }
    }

    /**
     * handles solo mode, when solo mode is clicked
     *
     * @throws IOException IOException
     */
    public void soloButtonOnAction() throws IOException {
        changeScene("/FXML/soloGameSelection.fxml");
    }

    /**
     * starts the solo game till death
     */
    public void startTillDeathSoloGameButtonOnAction() {
        Stage stage = new Stage();
        Board board = new Board();
        board.setRandom(new Random(new Random().nextLong()));
        board.setRmLineCount((int) levelSlider.getValue() * 10);
        startSoloGame(stage, board);
    }

    /**
     * starts the solo game till points are achieved
     */
    public void startTillPointsSoloGameButtonOnAction() {
        Stage stage = new Stage();
        Board board = new Board();
        board.setRandom(new Random(new Random().nextLong()));
        board.setGoalPoints((int) pointSlider.getValue());
        startSoloGame(stage, board);
    }

    /**
     * starts the solo mode game
     *
     * @param stage stage
     * @param board board
     */
    private void startSoloGame(Stage stage, Board board) {
        board.setUsername(username);
        DrawThread drawThread = new DrawThread(stage, board, false);
        KeyListener input = new KeyListener(drawThread.getScene(), board);
        DropThread dropThread = new DropThread(input, board, false);
        input.setDropThread(dropThread);
        drawThread.start();
        dropThread.start();
        input.start();
        stage.setOnCloseRequest(event -> {
            input.cancel();
            drawThread.cancel();
            dropThread.cancel();
        });
    }

    /**
     * handles the host function for the multiplayer mode
     */
    public void hostButtonOnAction() throws IOException {
        Board board = new Board();
        board.setUsername(username);
        Stage stage = new Stage();
        Server server = new Server();
        server.start();
        ServerHandler serverHandler = new ServerHandler(stage, board);
        serverHandler.setIPAddress("localhost");
        serverHandler.start();
        Controller.serverHandler = serverHandler;
        Controller.server = server;
        stage.setOnCloseRequest(event -> serverHandler.cancel());
        changeScene("/FXML/waitingConnection.fxml");
    }

    /**
     * opens the IP Address selection for the multiplayer mode
     *
     * @throws IOException IOException
     */
    public void openAddressSelectionOnAction() throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/FXML/addressSelection.fxml")));
        Stage stage = new Stage();
        stage.getIcons().add(new Image("images/image5.png"));
        stage.setTitle("Tetris");
        stage.setScene(new Scene(root, 350, 260));
        stage.show();
    }

    /**
     * handles the join function for the multiplayer mode
     */
    public void joinButtonOnAction() {
        String address = addressField.getText();
        if (isIP(address)) {
            Board board = new Board();
            board.setUsername(username);
            Stage stage = new Stage();
            ServerHandler serverHandler = new ServerHandler(stage, board);
            serverHandler.setIPAddress(address);
            System.out.println(addressField.getText());
            serverHandler.start();
            stage.setOnCloseRequest(event -> serverHandler.cancel());
        } else {
            addressField.setStyle("-fx-border-color: red");
        }
    }

    /**
     * checks if the String is a valid IP
     *
     * @param ip InputString
     * @return true or false
     */
    public static boolean isIP(String ip) {
        if (ip.trim().equals("localhost")) {
            return true;
        }
        Pattern p = Pattern.compile("(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})");
        Matcher m = p.matcher(ip);

        if (!m.matches()) {
            return false;
        }

        final int four = 4;
        final int twoHundredFiftyFive = 255;

        for (int i = 1; i <= four; i++) {
            int octet = Integer.parseInt(m.group(i));
            if (octet < 0 || octet > twoHundredFiftyFive) {
                return false;
            }
        }

        return true;
    }

    /**
     * changes scene to highscore scene
     *
     * @throws IOException IOException
     */
    public void highscoreButtonOnAction() throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/FXML/highscore.fxml")));
        Platform.runLater(() -> {
            StringBuilder stringBuilder;
            try {
                BufferedReader reader = new BufferedReader(new FileReader("highscore.txt"));
                stringBuilder = new StringBuilder();
                String line;
                ArrayList<String> strings = new ArrayList<>();
                while ((line = reader.readLine()) != null) {
                    if(!line.trim().equals("")) {
                        strings.add(line);
                    }
                }
                strings.stream()
                        .sorted(Comparator.comparingInt(n -> -Integer.parseInt(n.split("-")[1].trim())))
                        .limit(5)
                        .forEach(n -> stringBuilder.append("    ").append(n).append("\n"));
                reader.close();
                Stage awardCeremony = new Stage();
                Label label = new Label("The highscores are: \n" + stringBuilder);
                label.setLayoutX(70);
                label.setLayoutY(120);
                label.setFont(new Font(20));
                awardCeremony.getIcons().add(new Image("images/image5.png"));
                awardCeremony.setTitle("Tetris");
                Group group = new Group();
                group.getChildren().addAll(root, label);
                awardCeremony.setScene(new Scene(group, 350, 330));
                awardCeremony.show();
            } catch (IOException e) {
                System.out.println("Couldn't find highscores.");
            }
        });
    }

    /**
     * award ceremony in a single player game
     *
     * @param points reached points
     * @throws IOException IOException
     */
    public void awardCeremony(int points) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/FXML/awardCeremonySolo.fxml")));
        Platform.runLater(() -> {
            Stage awardCeremony = new Stage();
            Label label = new Label("Nice! You got " + points + " points.");
            label.setLayoutX(80);
            label.setLayoutY(130);
            label.setFont(new Font(20));
            awardCeremony.getIcons().add(new Image("images/image5.png"));
            awardCeremony.setTitle("Tetris");
            Group group = new Group();
            group.getChildren().addAll(root, label);
            awardCeremony.setScene(new Scene(group, 350, 260));
            awardCeremony.show();
        });
    }

    /**
     * award ceremony in a multiplayer game
     *
     * @param playerOne       player one
     * @param playerTwo       player two
     * @param playerOnePoints points of player one
     * @param playerTwoPoints points of player two
     * @throws IOException IOException
     */
    public void awardCeremonyMulti(String playerOne, String playerTwo, int playerOnePoints, int playerTwoPoints) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/FXML/awardCeremony.fxml")));
        Platform.runLater(() -> {
            Label labelPlayerOne;
            Label labelPlayerTwo;

            if (playerOnePoints >= playerTwoPoints) {
                labelPlayerOne = new Label("Nice! " + playerOne + " won and got " + playerOnePoints + " points.");
                labelPlayerTwo = new Label(playerTwo + " lost and got " + playerTwoPoints + " points.\n   Better luck next time.");
            } else {
                labelPlayerOne = new Label("Nice! " + playerTwo + " won and got " + playerTwoPoints + " points.");
                labelPlayerTwo = new Label(playerOne + " lost and got " + playerOnePoints + " points.\n   Better luck next time.");
            }

            Stage awardCeremony = new Stage();
            labelPlayerOne.setLayoutX(80);
            labelPlayerOne.setLayoutY(130);
            labelPlayerOne.setFont(new Font(20));
            labelPlayerTwo.setLayoutX(80);
            labelPlayerTwo.setLayoutY(180);
            labelPlayerTwo.setFont(new Font(20));
            awardCeremony.getIcons().add(new Image("images/image5.png"));
            awardCeremony.setTitle("Tetris");
            Group group = new Group();
            group.getChildren().addAll(root, labelPlayerOne, labelPlayerTwo);
            awardCeremony.setScene(new Scene(group, 415, 260));
            awardCeremony.show();
        });
    }

    /**
     * sets new stage for connection loss win
     *
     * @throws IOException IOException
     */
    public void showConnectionLossWin() throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/FXML/connectionLostWin.fxml")));
        Stage stage = new Stage();
        stage.getIcons().add(new Image("images/image5.png"));
        stage.setTitle("Tetris");
        stage.setScene(new Scene(root, 350, 260));
        stage.show();
    }
}
