package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import gui.listener.DataChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.MeioPagamento;
import model.service.CategoriaService;
import model.service.MeioPagamentoService;

public class MeioPagamentoFormController implements Initializable {

	private MeioPagamento entity;

	private MeioPagamentoService service;

	private ObservableList<MeioPagamento> obsList;

	// lista de componentes interessados em alterações
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	@FXML
	private TextField txtId;

	@FXML
	private TextField txtDescricao;

	@FXML
	private TextField txtSaldo;

	@FXML
	private TextField txtUsuarioId;

	@FXML
	private Label labelErrorDescricao;

	@FXML
	private Button btSave;

	@FXML
	private Button btCancel;

	public void setMeioPagamento(MeioPagamento entity) {

		this.entity = entity;

	}

	public void setServices(MeioPagamentoService service) {

		this.service = service;

	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub

	}

	public void setCategoria(MeioPagamento obj) {
		// TODO Auto-generated method stub

	}

	public void loadAssociatedObjects() {
		// TODO Auto-generated method stub

	}

	public void subscribeDataChangeListener(MeioPagamentoListController meioPagamentoListController) {
		// TODO Auto-generated method stub

	}

	public void updateFormData() {
		// TODO Auto-generated method stub

	}

	public void setServices(CategoriaService categoriaService) {
		// TODO Auto-generated method stub
		
	}

}
