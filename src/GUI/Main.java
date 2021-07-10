package GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 * class which starts the drawThread
 */
public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * stage to be changed to
     */
    private static Stage stg;

    @Override
    public void start(Stage primaryStage) throws IOException {
        System.out.println();
        stg = primaryStage;
        primaryStage.setResizable(false);
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/FXML/sample.fxml")));
        primaryStage.getIcons().add(new Image("images/image5.png"));
        primaryStage.setTitle("Tetris");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }

    /**
     * changes the scene
     *
     * @param fxml fxml
     * @throws IOException IOException
     */
    public static void changeScene(String fxml) throws IOException {
        Parent pane = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource(fxml)));
        stg.getScene().setRoot(pane);
    }
}
