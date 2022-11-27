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
import model.entities.Categoria;
import model.entities.MeioPagamento;
import model.entities.MovimentoFinanceiro;
import model.service.CategoriaService;
import model.service.MeioPagamentoService;
import model.service.MovimentoFinanceiroService;

public class MovimentoFinanceiroListController implements Initializable, DataChangeListener {

	private MovimentoFinanceiroService service;

	private Integer usuarioID;

	@FXML
	private TableView<MovimentoFinanceiro> tableViewMovimentoFinanceiro;

	@FXML
	private TableColumn<MovimentoFinanceiro, Integer> tableColumnId;

	@FXML
	private TableColumn<MovimentoFinanceiro, String> tableColumnDescricao;

	@FXML
	private TableColumn<MovimentoFinanceiro, Date> tableColumnData;

	@FXML
	private TableColumn<MovimentoFinanceiro, Double> tableColumnValor;

	@FXML
	private TableColumn<MovimentoFinanceiro, Categoria> tableColumnCategoria;

	@FXML
	private TableColumn<MovimentoFinanceiro, MeioPagamento> tableColumnMeioPagamento;

	@FXML
	private Button buttonNewDeposit;

	@FXML
	private TableColumn<MovimentoFinanceiro, MovimentoFinanceiro> tableColumnRemove;

	private ObservableList<MovimentoFinanceiro> obsList;

	@FXML
	public void onBtNewActionEntradas(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		MovimentoFinanceiro obj = new MovimentoFinanceiro();
		createDialogForm(obj, "/gui/MovimentoFinanceiroEntradasFormView.fxml", parentStage);
	}

	public void setMovimentoFinanceiroService(MovimentoFinanceiroService movimentoFinanceiroService) {

		service = movimentoFinanceiroService;
	}

	@Override
	public void initialize(URL uri, ResourceBundle rb) {
		usuarioID = Main.getUsuarioID();
		initializeNodes();

	}

	// setando as colunas de acordo com o nome dos atributos das classes
	private void initializeNodes() {

		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));

		tableColumnData.setCellValueFactory(new PropertyValueFactory<>("dataTransacao"));
		Utils.formatTableColumnDate(tableColumnData, "dd/MM/yyyy");

		tableColumnValor.setCellValueFactory(new PropertyValueFactory<>("valor"));
		Utils.formatTableColumnCash(tableColumnValor);

		tableColumnCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
		
		tableColumnMeioPagamento.setCellValueFactory(new PropertyValueFactory<>("meioPagamento"));

		// serve para que a lista acompanhe ate o final da tela
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewMovimentoFinanceiro.prefHeightProperty().bind(stage.heightProperty());

	}

	public void updateTableView() {

		if (service == null) {

			throw new IllegalStateException("Service was null");
		}
		List<MovimentoFinanceiro> list = service.findByUserId(usuarioID);
		obsList = FXCollections.observableArrayList(list);
		tableViewMovimentoFinanceiro.setItems(obsList);
		initRemoveButtons();

	}

	private void createDialogForm(MovimentoFinanceiro obj, String absoluteName, Stage parentStage) {

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));

			// carrega a view
			Pane pane = loader.load();

			// populando o form com os meios de pagamento
			MovimentoFinanceiroEntradasFormController controller = loader.getController();
			controller.setMovimentoPagamento(obj);
			controller.setServices(new MovimentoFinanceiroService(), new CategoriaService(), new MeioPagamentoService());
			controller.loadAssociatedObjects();

			// adiciona um item na lista de listeners portanto sera chamado para atualizar a
			// lista
			controller.subscribeDataChangeListener(this);
			controller.updateFormData();

			// quando quero abrir uma janela nova preciso instanciar um novo stage
			Stage dialogStage = new Stage();

			// setando o titulo do stage
			dialogStage.setTitle("Cadastre suas suas movimentações financeiras !");
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
		// verificador de mudanças, caso haja alguma sera feito um update de tabela da
		// view
		updateTableView();

	}

	private void initRemoveButtons() {

		tableColumnRemove.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnRemove.setCellFactory(param -> new TableCell<MovimentoFinanceiro, MovimentoFinanceiro>() {
			private final Button button = new Button("Remover");

			@Override
			protected void updateItem(MovimentoFinanceiro obj, boolean empty) {
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

	private void removeEntity(MovimentoFinanceiro obj) {
		Optional<ButtonType> result = Alerts.showConfirmation("Confirmation", "Tem certeza que você quer deletar ?");

		if (result.get() == ButtonType.OK) {
			if (service == null) {
				throw new IllegalStateException("Service é nulo !");
			}

			try {
				if(obj.getValor() > obj.getMeioPagamento().getSaldo()) {
					
					throw new DbIntegrityException("Não é permitido saldo negativo ! Organize suas fincanças !");
					
				}
				service.remove(obj);
				service.diminuiSaldo(obj.getValor(),obj.getMeioPagamento().getId());
				updateTableView();
			} catch (DbIntegrityException e) {

				Alerts.showAlert("Error para mover objeto", null, e.getMessage(), AlertType.ERROR);
			}
		}
	}

}
