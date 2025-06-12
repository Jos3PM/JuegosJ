package upeu.edu.pe.ajedres;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/upeu/edu/pe/ajedres/view/CheckersBoard.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.setTitle("Checkers Game");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}