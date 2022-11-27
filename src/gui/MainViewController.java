package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.Main;
import gui.util.Alerts;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import model.service.CategoriaService;
import model.service.MeioPagamentoService;
import model.service.MovimentoFinanceiroService;

public class MainViewController implements Initializable {

	@FXML
	private MenuItem menuItemSeller;
	@FXML
	private MenuItem menuItemDepartment;
	@FXML
	private MenuItem menuItemAbout;
	@FXML
	private MenuItem menuItemCategoria;
	@FXML
	private MenuItem menuItemMeioPagamento;
	@FXML
	private MenuItem menuItemMovimentoFinanceiro;
	@FXML
	private Label textoNome;
	@FXML
	private PieChart chartMinhasCarteiras;
	
	Double totalreceita = Double.valueOf(0);
	Double totaldespesa = Double.valueOf(0);

	@FXML
	public void onMenuItemAboutAction() {
		loadView("/gui/AboutView.fxml", x -> {
		});
	}
	
	@FXML
	public void onMenuItemCategoriaAction() {
		
		loadView("/gui/CategoriaListView.fxml", (CategoriaListController controller) -> {

			controller.setCategoriaService(new CategoriaService());
			controller.updateTableView();
		});
	}
	
	@FXML
	public void onMenuItemMeioPagamentoAction() {

		loadView("/gui/MeioPagamentoListView.fxml", (MeioPagamentoListController controller) -> {

			controller.setMeioPagamentoService(new MeioPagamentoService());
			controller.updateTableView();
		});
	}
	
	@FXML
	public void onMenuItemMovimentoFinanceiroAction() {

		loadView("/gui/MovimentoFinanceiroListView.fxml", (MovimentoFinanceiroListController controller) -> {

			controller.setMovimentoFinanceiroService(new MovimentoFinanceiroService());
			controller.updateTableView();
		});
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		
		textoNome.setText(Main.getUsuarioName().toUpperCase());
		
		MeioPagamentoService service = new MeioPagamentoService();
		totalreceita = service.totalReceitasById(Main.getUsuarioID());
		totaldespesa = service.totalDespesasById(Main.getUsuarioID());
		
		createPieChart();

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
	
	private void createPieChart() {
		
		ObservableList<PieChart.Data> pieChartData
			= FXCollections.observableArrayList(
					new PieChart.Data("RECEITAS R$" + String.format("%.2f", totalreceita),totalreceita),
					new PieChart.Data("DESPESAS R$" + String.format("%.2f", totaldespesa),totaldespesa)
					);
		chartMinhasCarteiras.setData(pieChartData);
		chartMinhasCarteiras.setTitle("Meu fluxo de Caixa");
		
	}

}
