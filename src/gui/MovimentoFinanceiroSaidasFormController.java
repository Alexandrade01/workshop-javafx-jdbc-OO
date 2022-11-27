package gui;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import application.Main;
import db.DbException;
import gui.listener.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import model.entities.Categoria;
import model.entities.MeioPagamento;
import model.entities.MovimentoFinanceiro;
import model.exceptions.ValidationException;
import model.service.CategoriaService;
import model.service.MeioPagamentoService;
import model.service.MovimentoFinanceiroService;

public class MovimentoFinanceiroSaidasFormController implements Initializable {

	private MovimentoFinanceiro entity;

	private MovimentoFinanceiroService service;

	private CategoriaService categoriaService;

	private MeioPagamentoService meioPagamentoService;

	private Integer usuarioId;

	@FXML
	private TextField txtDescricao;

	@FXML
	private DatePicker txtDataTransacao;

	@FXML
	private TextField txtValor;

	@FXML
	private ComboBox<Categoria> comboBoxCategoria;

	@FXML
	private ComboBox<MeioPagamento> comboBoxMeioPagamento;

	@FXML
	private Button btSave;

	@FXML
	private Button btCancel;

	@FXML
	private Label labelErrorDescricao;

	@FXML
	private Label labelErrorDataTransacao;

	@FXML
	private Label labelErrorValor;

	// lista de componentes interessados em alterações
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	// observadores de categorias e meio de pagamentos
	private ObservableList<Categoria> obsListCategoria;

	private ObservableList<MeioPagamento> obsListMeioPagamento;

	public void setServices(MovimentoFinanceiroService MovimentoFinanceiroService, CategoriaService categoriaService,
			MeioPagamentoService meioPagamentoService) {

		service = MovimentoFinanceiroService;
		this.categoriaService = categoriaService;
		this.meioPagamentoService = meioPagamentoService;

	}

	public void setMovimentoPagamento(MovimentoFinanceiro obj) {

		entity = obj;

	}

	// injecao da lista
	public void subscribeDataChangeListener(DataChangeListener listener) {

		dataChangeListeners.add(listener);
	}

	@FXML
	public void onBtSaveAction(ActionEvent event) {
		// controle de aviso para o programador injetar a dependencia
		if (entity == null) {
			throw new IllegalStateException();
		}
		if (service == null) {
			throw new IllegalStateException();
		}
		try {

			entity = getFormData();

			if (entity.getValor() > entity.getMeioPagamento().getSaldo()) {

				throw new DbException("Não é permitido saldo negativo ! Organize suas finanças !");
			}

			service.saveOrUpdate(entity);
			service.diminuiSaldo(entity.getValor(), entity.getMeioPagamento().getId());
			// possuindo algum item na lista de dataChangeListeners ele ira atualizar a
			// lista do formulario
			notifyDataChangeListener();
			Utils.currentStage(event).close();

		} catch (ValidationException e) {

			setErrorMessages(e.getErrors());
		}
		catch (RuntimeException e) {

			Alerts.showAlert("Erro !", null, "Erro ao salvar a movimentação !", AlertType.ERROR);
		}

	}

	private void notifyDataChangeListener() {

		for (DataChangeListener listener : dataChangeListeners) {

			listener.onDataChanged();

		}

	}

	private MovimentoFinanceiro getFormData() {

		MovimentoFinanceiro obj = new MovimentoFinanceiro();

		ValidationException validationException = new ValidationException("Validation error");

		// Validacao descricao
		if (txtDescricao.getText() == null || txtDescricao.getText().trim().equals("")) {
			validationException.addError("descricao", "O campo não pode ser vazio ! ");
		}
		obj.setDescricao(txtDescricao.getText());

		// Validacao data transicao
		if (txtDataTransacao.getValue() == null) {

			validationException.addError("dataTransacao", "O campo não pode ser vazio !");

		} else {
			Instant instant = Instant.from(txtDataTransacao.getValue().atStartOfDay(ZoneId.systemDefault()));
			obj.setDataTransacao(Date.from(instant));
		}

		// Validacao valor
		if (txtValor.getText() == null || txtValor.getText().trim().equals("")) {
			validationException.addError("valor", "O campo não pode ser vazio ! ");
		}
		obj.setValor(Utils.tryParseToDouble(txtValor.getText()));

		obj.setCategoria(comboBoxCategoria.getValue());

		obj.setMeioPagamento(comboBoxMeioPagamento.getValue());

		obj.setUsuario(usuarioId);

		if (validationException.getErrors().size() > 0) {

			throw validationException;
		}

		return obj;
	}

	@FXML
	public void onBtCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		initializeNodes();
	}

	private void initializeNodes() {

		usuarioId = Main.getUsuarioID();

		Constraints.setTextFieldMaxLength(txtDescricao, 70);
		Utils.formatDatePicker(txtDataTransacao, "dd/MM/yyyy");
		Constraints.setTextFieldDouble(txtValor);
		initializeComboBoxSaidas();

	}

	// popular formulario com o objeto
	public void updateFormData() {

		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}

		txtDescricao.setText(String.valueOf(entity.getDescricao()));
		if (entity.getDataTransacao() != null) {
			// serve para pegar o instante ou tempo da maquina local, via
			// zoneId.systemDefault
			txtDataTransacao
					.setValue(LocalDate.ofInstant(entity.getDataTransacao().toInstant(), ZoneId.systemDefault()));
		}
		Locale.setDefault(Locale.US);
		txtValor.setText(String.format("%.2f", entity.getValor()));

		if (entity.getCategoria() == null) {

			comboBoxCategoria.getSelectionModel().selectFirst();
		}

		comboBoxCategoria.setValue(entity.getCategoria());

		if (entity.getMeioPagamento() == null) {

			comboBoxMeioPagamento.getSelectionModel().selectFirst();
		}

		comboBoxMeioPagamento.setValue(entity.getMeioPagamento());

	}

	private void setErrorMessages(Map<String, String> errors) {

		Set<String> fields = errors.keySet();

		labelErrorDescricao.setText(fields.contains("descricao") ? errors.get("descricao") : "");
		labelErrorDataTransacao.setText(fields.contains("dataTransacao") ? errors.get("dataTransacao") : "");
		labelErrorValor.setText(fields.contains("valor") ? errors.get("valor") : "");

	}

	private void initializeComboBoxSaidas() {
		Callback<ListView<Categoria>, ListCell<Categoria>> factoryCategoria = lv -> new ListCell<Categoria>() {
			@Override
			protected void updateItem(Categoria item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getDescricao());
			}
		};
		comboBoxCategoria.setCellFactory(factoryCategoria);
		comboBoxCategoria.setButtonCell(factoryCategoria.call(null));

		Callback<ListView<MeioPagamento>, ListCell<MeioPagamento>> factoryMeioPagamento = lv -> new ListCell<MeioPagamento>() {
			@Override
			protected void updateItem(MeioPagamento item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getDescricao());
			}
		};
		comboBoxMeioPagamento.setCellFactory(factoryMeioPagamento);
		comboBoxMeioPagamento.setButtonCell(factoryMeioPagamento.call(null));

	}

	public void loadAssociatedObjects() {

		if (categoriaService == null) {

			throw new IllegalStateException("Categoria service é nulo ");
		}
		List<Categoria> listCategoria = categoriaService.findAllOutsByUserId(usuarioId);
		obsListCategoria = FXCollections.observableArrayList(listCategoria);
		comboBoxCategoria.setItems(obsListCategoria);

		if (meioPagamentoService == null) {

			throw new IllegalStateException("Meio pagamento service é nulo");
		}
		List<MeioPagamento> list = meioPagamentoService.findAllByUserId(usuarioId);
		obsListMeioPagamento = FXCollections.observableArrayList(list);
		comboBoxMeioPagamento.setItems(obsListMeioPagamento);

	}

}
