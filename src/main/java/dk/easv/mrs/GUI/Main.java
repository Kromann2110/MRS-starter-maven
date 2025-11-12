package dk.easv.mrs.GUI;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

// Starts the Movie app
public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{
        // Loads the GUI layout from FXML file
        Parent root = FXMLLoader.load(getClass().getResource("/views/MovieView.fxml"));
        primaryStage.setTitle("MRS");
        // Creates window with fixed size
        primaryStage.setScene(new Scene(root, 600, 475));
        primaryStage.show(); // Makes window visible
    }

    public static void main(String[] args) {
        launch(args); // Bootstraps JavaFX
    }
}