package minesPlus;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
	

	@Override
	public void start(Stage primaryStage) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Mines.fxml"));
		MineControl controller = new MineControl();
		controller.setStage(primaryStage);
		loader.setController(controller);
		loader.load();
		Parent p = loader.getRoot(); 
	    Scene scene = new Scene(p);
	    primaryStage.setResizable(false);
	    primaryStage.setScene(scene);
	    primaryStage.setTitle("Mines Sweeper Ultimate");
	    primaryStage.sizeToScene();
	    primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}


	
}
