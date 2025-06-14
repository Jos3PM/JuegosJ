package upeu.edu.pe.ajedres;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class AjedresApplication extends Application {
	@Override
	public void start(Stage stage) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(AjedresApplication.class.getResource("/fxml/tablero.fxml"));
		Scene scene = new Scene(fxmlLoader.load(), 800, 800);
		stage.setTitle("Ajedrez");
		stage.setScene(scene);
		stage.show();
	}

	public static void main(String[] args) {
		launch();
	}
}
