package application;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Main extends Application {

	public static void main(String[] args) {
		launch(args);
	}
	
	private static Scene mainScene;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			
			FXMLLoader loaderLogin = new FXMLLoader(getClass().getResource("/gui/UsuarioLoginView.fxml"));
			AnchorPane anchorPaneLogin = loaderLogin.load();
			Stage stage = new Stage();
			mainScene = new Scene(anchorPaneLogin);
			stage.setScene(mainScene);
			stage.showAndWait();
			
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/MainView.fxml"));
			ScrollPane scrollPane = loader.load();
			
			//configurando o scrollpane para ficar do tamanho da tela
			scrollPane.setFitToHeight(true);
			scrollPane.setFitToWidth(true);
			

			
			
			mainScene = new Scene(scrollPane);
			primaryStage.setScene(mainScene);
			primaryStage.setTitle("Fesa Wallet ®");
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Scene getMainScene() {
		
		return mainScene;
		
	}

}
