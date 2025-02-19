package com.facturacion.factu3.controllers;

import com.facturacion.factu3.models.Trabajador;
import com.facturacion.factu3.models.TrabajadorDAO;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.regex.Pattern;

public class TrabajadoresController {

    @FXML
    private TextField txtNombre, txtDireccion, txtTelefono, txtEmail, txtPuesto, txtSalario;

    @FXML
    private TextArea txtObservaciones;

    @FXML
    private TableView<Trabajador> tblTrabajadores;

    @FXML
    private TableColumn<Trabajador, Integer> colId;

    @FXML
    private TableColumn<Trabajador, String> colNombre, colDireccion, colTelefono, colEmail, colPuesto, colObservaciones;

    @FXML
    private TableColumn<Trabajador, Double> colSalario;

    private ObservableList<Trabajador> listaTrabajadores;

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\d{9}$");
    private static final Pattern SALARY_PATTERN = Pattern.compile("^\\d+(\\.\\d{1,2})?$");

    @FXML
    public void initialize() {
        System.out.println("👷 Inicializando TrabajadoresController...");

        listaTrabajadores = FXCollections.observableArrayList(TrabajadorDAO.obtenerTrabajadores());
        tblTrabajadores.setItems(listaTrabajadores);

        colId.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        colNombre.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre()));
        colDireccion.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDireccion()));
        colTelefono.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTelefono()));
        colEmail.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));
        colPuesto.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPuesto()));
        colSalario.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getSalario()).asObject());
        colObservaciones.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getObservaciones()));

        System.out.println("✅ Tabla de trabajadores cargada con éxito.");

        tblTrabajadores.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Trabajador trabajadorSeleccionado = tblTrabajadores.getSelectionModel().getSelectedItem();
                if (trabajadorSeleccionado != null) {
                    abrirVentanaEdicion(trabajadorSeleccionado);
                }
            }
        });
    }

    public void actualizarTablaTrabajadores() {
        listaTrabajadores.setAll(TrabajadorDAO.obtenerTrabajadores());
    }

    @FXML
    private void guardarTrabajador() {
        if (!validarCampos()) {
            return;
        }

        Trabajador nuevoTrabajador = new Trabajador(
                TrabajadorDAO.obtenerSiguienteId(),
                txtNombre.getText(),
                txtDireccion.getText(),
                txtTelefono.getText(),
                txtEmail.getText(),
                txtPuesto.getText(),
                Double.parseDouble(txtSalario.getText()),
                txtObservaciones.getText()
        );

        if (TrabajadorDAO.agregarTrabajador(nuevoTrabajador)) {
            actualizarTablaTrabajadores();
            limpiarCampos();
            mostrarAlerta("Guardado", "Trabajador guardado correctamente.", Alert.AlertType.INFORMATION);
        } else {
            mostrarAlerta("Error", "No se pudo guardar el trabajador.", Alert.AlertType.ERROR);
        }
    }

    private boolean validarCampos() {
        if (txtNombre.getText().trim().isEmpty() || txtPuesto.getText().trim().isEmpty()) {
            mostrarAlerta("Campos obligatorios", "Los campos Nombre y Puesto son obligatorios.", Alert.AlertType.WARNING);
            return false;
        }

        if (!PHONE_PATTERN.matcher(txtTelefono.getText().trim()).matches()) {
            mostrarAlerta("Teléfono inválido", "El número de teléfono debe tener 9 dígitos.", Alert.AlertType.WARNING);
            return false;
        }

        if (!EMAIL_PATTERN.matcher(txtEmail.getText().trim()).matches()) {
            mostrarAlerta("Correo inválido", "El correo debe tener el formato correcto (ejemplo@dominio.com).", Alert.AlertType.WARNING);
            return false;
        }

        if (!SALARY_PATTERN.matcher(txtSalario.getText().trim()).matches()) {
            mostrarAlerta("Salario inválido", "El salario debe ser un número válido.", Alert.AlertType.WARNING);
            return false;
        }

        return true;
    }

    private void limpiarCampos() {
        txtNombre.clear();
        txtDireccion.clear();
        txtTelefono.clear();
        txtEmail.clear();
        txtPuesto.clear();
        txtSalario.clear();
        txtObservaciones.clear();
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    private void abrirVentanaEdicion(Trabajador trabajador) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/EditarTrabajadorView.fxml"));
            Parent root = loader.load();
            EditarTrabajadorController controller = loader.getController();

            Stage stage = new Stage();
            controller.setTrabajador(trabajador, stage, this);

            stage.setScene(new Scene(root));
            stage.setTitle("Editar Trabajador");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
