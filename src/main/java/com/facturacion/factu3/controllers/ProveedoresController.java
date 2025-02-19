package com.facturacion.factu3.controllers;

import com.facturacion.factu3.models.Proveedor;
import com.facturacion.factu3.models.ProveedorDAO;
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

public class ProveedoresController {

    @FXML
    private TextField txtNombre, txtDireccion, txtCIF, txtTelefono, txtEmail;

    @FXML
    private TextArea txtObservaciones;

    @FXML
    private TableView<Proveedor> tblProveedores;

    @FXML
    private TableColumn<Proveedor, Integer> colId;

    @FXML
    private TableColumn<Proveedor, String> colNombre, colDireccion, colCif, colTelefono, colEmail, colObservaciones;

    private ObservableList<Proveedor> listaProveedores;

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\d{9}$");

    @FXML
    public void initialize() {
        System.out.println("➡ Inicializando ProveedoresController...");

        listaProveedores = FXCollections.observableArrayList(ProveedorDAO.obtenerProveedores());
        tblProveedores.setItems(listaProveedores);

        colId.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        colNombre.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre()));
        colDireccion.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDireccion()));
        colTelefono.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTelefono()));
        colEmail.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));
        colCif.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCif()));
        colObservaciones.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getObservaciones()));

        System.out.println("✅ Tabla de proveedores cargada con éxito.");

        tblProveedores.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Proveedor proveedorSeleccionado = tblProveedores.getSelectionModel().getSelectedItem();
                if (proveedorSeleccionado != null) {
                    abrirVentanaEdicion(proveedorSeleccionado);
                }
            }
        });
    }

    public void actualizarTablaProveedores() {
        listaProveedores.setAll(ProveedorDAO.obtenerProveedores());
    }

    @FXML
    private void guardarProveedor() {
        if (!validarCampos()) {
            return;
        }

        Proveedor nuevoProveedor = new Proveedor(
                ProveedorDAO.obtenerSiguienteId(),
                txtNombre.getText(),
                txtDireccion.getText(),
                txtTelefono.getText(),
                txtEmail.getText(),
                txtCIF.getText(),
                txtObservaciones.getText()
        );

        if (ProveedorDAO.agregarProveedor(nuevoProveedor)) {
            actualizarTablaProveedores();
            limpiarCampos();
            mostrarAlerta("Guardado", "Proveedor guardado correctamente.", Alert.AlertType.INFORMATION);
        } else {
            mostrarAlerta("Error", "No se pudo guardar el proveedor.", Alert.AlertType.ERROR);
        }
    }

    private boolean validarCampos() {
        if (txtNombre.getText().trim().isEmpty() || txtCIF.getText().trim().isEmpty()) {
            mostrarAlerta("Campos obligatorios", "Los campos Nombre y CIF son obligatorios.", Alert.AlertType.WARNING);
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

        return true;
    }

    private void limpiarCampos() {
        txtNombre.clear();
        txtDireccion.clear();
        txtCIF.clear();
        txtTelefono.clear();
        txtEmail.clear();
        txtObservaciones.clear();
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    private void abrirVentanaEdicion(Proveedor proveedor) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/EditarProveedorView.fxml"));
            Parent root = loader.load();
            EditarProveedorController controller = loader.getController();

            Stage stage = new Stage();
            controller.setProveedor(proveedor, stage, this);

            stage.setScene(new Scene(root));
            stage.setTitle("Editar Proveedor");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
