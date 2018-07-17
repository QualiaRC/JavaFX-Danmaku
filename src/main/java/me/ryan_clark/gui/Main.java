package me.ryan_clark.gui;

import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import me.ryan_clark.app.GameController;
import me.ryan_clark.app.Prefs;
import me.ryan_clark.gui.GuiController;

public class Main extends Application {

	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		URL location = getClass().getClassLoader().getResource("res/layout/GameLayout.fxml");
		FXMLLoader fxmlLoader = new FXMLLoader(location);
		Parent root = fxmlLoader.load();
		GuiController c = fxmlLoader.getController();

		Scene scene = new Scene(root, Prefs.RES_X, Prefs.RES_Y);

		primaryStage.setTitle("Danmaku");
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.sizeToScene();
		primaryStage.show();


		new GameController(c);
	}

}
