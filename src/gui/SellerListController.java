package gui;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import db.DbIntegrityException;
import gui.listener.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Seller;
import model.service.DepartmentService;
import model.service.SellerService;

public class SellerListController implements Initializable, DataChangeListener {

	private SellerService service;

	@FXML
	private TableView<Seller> tableViewSeller;

	@FXML
	private TableColumn<Seller, Integer> tableColumnId;

	@FXML
	private TableColumn<Seller, String> tableColumnName;

	@FXML
	private TableColumn<Seller, String> tableColumnEmail;

	@FXML
	private TableColumn<Seller, Date> tableColumnBirthDate;

	@FXML
	private TableColumn<Seller, Double> tableColumnBaseSalary;

	@FXML
	private TableColumn<Seller, Seller> tableColumnEdit;

	@FXML
	private Button buttonNew;

	@FXML
	private TableColumn<Seller, Seller> tableColumnRemove;

	private ObservableList<Seller> obsList;

	@FXML
	public void onBtNewAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Seller obj = new Seller();
		createDialogForm(obj, "/gui/SellerForm.fxml", parentStage);
		System.out.println("onBtNewAction");
	}

	public void setSellerService(SellerService service) {

		this.service = service;

	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {

		initializeNodes();
	}

	// setando as colunas de acordo com o nome dos atributos das classes
	private void initializeNodes() {

		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
		tableColumnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

		tableColumnBirthDate.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
		Utils.formatTableColumnDate(tableColumnBirthDate, "dd/MM/yyyy");

		tableColumnBaseSalary.setCellValueFactory(new PropertyValueFactory<>("baseSalary"));
		Utils.formatTableColumnDouble(tableColumnBaseSalary, 2);

		// serve para que a lista acompanhe ate o final da tela
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewSeller.prefHeightProperty().bind(stage.heightProperty());

	}

	public void updateTableView() {

		if (service == null) {

			throw new IllegalStateException("Service was null");
		}
		List<Seller> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewSeller.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
	}

	private void createDialogForm(Seller obj, String absoluteName, Stage parentStage) {

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));

			// carrega a view
			Pane pane = loader.load();

			// populando o form department com o obj Seller
			SellerFormController controller = loader.getController();
			controller.setSeller(obj);
			controller.setServices(new SellerService(), new DepartmentService());
			controller.loadAssociatedObjects();

			// adiciona um item na lista de listeners portanto sera chamado para atualizar a
			// lista
			controller.subscribeDataChangeListener(this);
			controller.updateFormData();

			// quando quero abrir uma janela nova preciso instanciar um novo stage
			Stage dialogStage = new Stage();

			// setando o titulo do stage
			dialogStage.setTitle("Enter Seller data");
			//
			dialogStage.setScene(new Scene(pane));
			// propriedade quem diz se a janela PODE OU NAO ser redimensionada
			dialogStage.setResizable(false);
			// indica quem é o stage pai dessa janela
			dialogStage.initOwner(parentStage);
			// window modal trava o app e so podemos usar o stage atual
			dialogStage.initModality(Modality.WINDOW_MODAL);
			// abre e espera a conclusao
			dialogStage.showAndWait();
			
		} catch (IOException e) {

			e.printStackTrace();

			Alerts.showAlert("Io Exception", "Error loading view", e.getMessage(), AlertType.ERROR);

		}

	}

	@Override
	public void onDataChanged() {

		updateTableView();

	}

	private void initEditButtons() {
		tableColumnEdit.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEdit.setCellFactory(param -> new TableCell<Seller, Seller>() {
			private final Button button = new Button("Edit");

			@Override
			protected void updateItem(Seller obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setPrefWidth(80);
				button.setOnAction(event -> createDialogForm(obj, "/gui/SellerForm.fxml", Utils.currentStage(event)));

			}
		});
	}

	private void initRemoveButtons() {
		tableColumnRemove.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnRemove.setCellFactory(param -> new TableCell<Seller, Seller>() {
			private final Button button = new Button("remove");

			@Override
			protected void updateItem(Seller obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setPrefWidth(80);
				button.setOnAction(event -> removeEntity(obj));
			}
		});

	}

	private void removeEntity(Seller obj) {
		Optional<ButtonType> result = Alerts.showConfirmation("Confirmation", "Tem certeza que você quer deletar ?");

		if (result.get() == ButtonType.OK) {
			if (service == null) {
				throw new IllegalStateException("Service é nulo !");
			}

			try {
				service.remove(obj);
				updateTableView();
			} catch (DbIntegrityException e) {

				Alerts.showAlert("Error para mover objeto", null, e.getMessage(), AlertType.ERROR);
			}
		}
	}

}
