package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import model.service.DepartmentService;
import model.service.SellerService;

public class MainViewController implements Initializable {

	@FXML
	private MenuItem menuItemSeller;
	@FXML
	private MenuItem menuItemDepartment;
	@FXML
	private MenuItem menuItemAbout;

	@FXML
	public void onMenuItemSellerAction() {
		System.out.println("onMenuItemSellerAction");
		loadView("/gui/SellerList.fxml", (SellerListController controller) -> {

			controller.setSellerService(new SellerService());
			controller.updateTableView();
		});
	}

	@FXML
	public void onMenuItemDepartmentAction() {
		loadView("/gui/DepartmentList.fxml", (DepartmentListController controller) -> {

			controller.setDepartmentService(new DepartmentService());
			controller.updateTableView();
		});
	}

	@FXML
	public void onMenuItemAboutAction() {
		System.out.println("onMenuItemAboutAction");
		loadView("/gui/About.fxml", x -> {
		});
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// TODO Auto-generated method stub

	}

	// synchronized garante que todos os processos da tela nao vao ser interrompidos
	// na troca de telas

	// colocamos um segundo parametro no padrao lambda e generics para poder criar
	// diferentes aberturas na mesma view loadView
	private synchronized <T> void loadView(String absoluteName, Consumer<T> initializingAction) {

		try {

			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox newVBox = loader.load();

			// instancia a tela principal em um objeto
			Scene mainScene = Main.getMainScene();
			// guarda o Vbox da tela principal
			VBox mainVBox = (VBox) (((ScrollPane) mainScene.getRoot()).getContent());
			// guarda os filhos da Vbox principal no caso o primeiro que é a barra superior
			Node mainMenu = mainVBox.getChildren().get(0);
			// limpa todos os childrens da tela
			mainVBox.getChildren().clear();
			// remonta os childrens com os da tela principal e em seguida da tela about.fxml
			mainVBox.getChildren().add(mainMenu);
			mainVBox.getChildren().addAll(newVBox.getChildren());

			T controller = loader.getController();
			initializingAction.accept(controller);

		} catch (IOException e) {

			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}

//	private synchronized void loadView2(String absoluteName) {
//
//		try {
//
//			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
//			VBox newVBox = loader.load();
//
//			// instancia a tela principal em um objeto
//			Scene mainScene = Main.getMainScene();
//			// guarda o Vbox da tela principal
//			VBox mainVBox = (VBox) (((ScrollPane) mainScene.getRoot()).getContent());
//			// guarda os filhos da Vbox principal no caso o primeiro que é a barra superior
//			Node mainMenu = mainVBox.getChildren().get(0);
//			// limpa todos os childrens da tela
//			mainVBox.getChildren().clear();
//			// remonta os childrens com os da tela principal e em seguida da tela about.fxml
//			mainVBox.getChildren().add(mainMenu);
//			mainVBox.getChildren().addAll(newVBox.getChildren());
//			
//			DepartmentListController controller = loader.getController();
//			controller.setDepartmentService(new DepartmentService());
//			controller.updateTableView();
//			
//		} catch (IOException e) {
//
//			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
//		}
//	}

}
