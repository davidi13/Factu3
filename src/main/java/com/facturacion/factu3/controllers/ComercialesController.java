package com.facturacion.factu3.controllers;

import com.facturacion.factu3.models.Comercial;
import com.facturacion.factu3.models.ComercialDAO;
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

public class ComercialesController {

    @FXML private TextField txtNombre, txtDireccion, txtTelefono, txtEmail, txtComision;
    @FXML private TextArea txtObservaciones;
    @FXML private TableView<Comercial> tblComerciales;
    @FXML private TableColumn<Comercial, Integer> colId;
    @FXML private TableColumn<Comercial, String> colNombre, colDireccion, colTelefono, colEmail, colComision, colObservaciones;

    private ObservableList<Comercial> listaComerciales;

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\d{9}$");

    @FXML
    public void initialize() {
        listaComerciales = FXCollections.observableArrayList(ComercialDAO.obtenerComerciales());
        tblComerciales.setItems(listaComerciales);

        colId.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        colNombre.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre()));
        colDireccion.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDireccion()));
        colTelefono.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTelefono()));
        colEmail.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));
        colComision.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getComision())));
        colObservaciones.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getObservaciones()));

        tblComerciales.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Comercial comercialSeleccionado = tblComerciales.getSelectionModel().getSelectedItem();
                if (comercialSeleccionado != null) {
                    abrirVentanaEdicion(comercialSeleccionado);
                }
            }
        });
    }

    public void actualizarTablaComerciales() {
        listaComerciales.setAll(ComercialDAO.obtenerComerciales());
    }

    @FXML
    private void guardarComercial() {
        if (!validarCampos()) return;

        Comercial nuevoComercial = new Comercial(
                ComercialDAO.obtenerSiguienteId(),
                txtNombre.getText(),
                txtDireccion.getText(),
                txtTelefono.getText(),
                txtEmail.getText(),
                Double.parseDouble(txtComision.getText()),
                txtObservaciones.getText()
        );

        if (ComercialDAO.agregarComercial(nuevoComercial)) {
            actualizarTablaComerciales();
            limpiarCampos();
            mostrarAlerta("Guardado", "Comercial guardado correctamente.", Alert.AlertType.INFORMATION);
        } else {
            mostrarAlerta("Error", "No se pudo guardar el comercial.", Alert.AlertType.ERROR);
        }
    }

    private boolean validarCampos() {
        if (txtNombre.getText().trim().isEmpty()) {
            mostrarAlerta("Campos obligatorios", "El campo Nombre es obligatorio.", Alert.AlertType.WARNING);
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

        try {
            Double.parseDouble(txtComision.getText());
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "La comisión debe ser un número.", Alert.AlertType.ERROR);
            return false;
        }

        return true;
    }

    private void limpiarCampos() {
        txtNombre.clear();
        txtDireccion.clear();
        txtTelefono.clear();
        txtEmail.clear();
        txtComision.clear();
        txtObservaciones.clear();
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    private void abrirVentanaEdicion(Comercial comercial) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/EditarComercialView.fxml"));
            Parent root = loader.load();
            EditarComercialController controller = loader.getController();

            Stage stage = new Stage();
            controller.setComercial(comercial, stage, this);

            stage.setScene(new Scene(root));
            stage.setTitle("Editar Comercial");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
