package com.facturacion.factu3.controllers;

import com.facturacion.factu3.models.Distribuidor;
import com.facturacion.factu3.models.DistribuidorDAO;
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

import java.util.List;
import java.util.regex.Pattern;

public class DistribuidoresController {

    @FXML private TextField txtNombre, txtDireccion, txtTelefono, txtEmail, txtZona;
    @FXML private TextArea txtObservaciones;
    @FXML private TableView<Distribuidor> tblDistribuidores;
    @FXML private TableColumn<Distribuidor, Integer> colId;
    @FXML private TableColumn<Distribuidor, String> colNombre, colDireccion, colTelefono, colEmail, colZona, colObservaciones;

    private ObservableList<Distribuidor> listaDistribuidores;

    // ✅ Expresiones regulares para validar el correo y teléfono
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\d{9}$");

    @FXML
    public void initialize() {
        System.out.println("➡ Inicializando DistribuidoresController...");

        listaDistribuidores = FXCollections.observableArrayList(DistribuidorDAO.obtenerDistribuidores());
        tblDistribuidores.setItems(listaDistribuidores);

        colId.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        colNombre.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre()));
        colDireccion.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDireccion()));
        colTelefono.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTelefono()));
        colEmail.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));
        colZona.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getZona()));
        colObservaciones.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getObservaciones()));

        System.out.println("✅ Tabla de distribuidores cargada con éxito.");

        tblDistribuidores.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Distribuidor distribuidorSeleccionado = tblDistribuidores.getSelectionModel().getSelectedItem();
                if (distribuidorSeleccionado != null) {
                    abrirVentanaEdicion(distribuidorSeleccionado);
                }
            }
        });
    }

    public void actualizarTablaDistribuidores() {
        listaDistribuidores.setAll(DistribuidorDAO.obtenerDistribuidores());
    }

    @FXML
    private void guardarDistribuidor() {
        if (!validarCampos()) {
            return;
        }

        Distribuidor nuevoDistribuidor = new Distribuidor(
                0, // Se generará automáticamente en la BD
                txtNombre.getText().trim(),
                txtDireccion.getText().trim(),
                txtTelefono.getText().trim(),
                txtEmail.getText().trim(),
                txtZona.getText().trim(),
                txtObservaciones.getText().trim()
        );

        if (DistribuidorDAO.agregarDistribuidor(nuevoDistribuidor)) {
            actualizarTablaDistribuidores();
            limpiarCampos();
            mostrarAlerta("Guardado", "Distribuidor guardado correctamente.", Alert.AlertType.INFORMATION);
        } else {
            mostrarAlerta("Error", "No se pudo guardar el distribuidor.", Alert.AlertType.ERROR);
        }
    }

    /**
     * ✅ Validaciones de campos antes de guardar
     */
    private boolean validarCampos() {
        String nombre = txtNombre.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String email = txtEmail.getText().trim();
        String zona = txtZona.getText().trim();

        if (nombre.isEmpty() || telefono.isEmpty() || email.isEmpty() || zona.isEmpty()) {
            mostrarAlerta("Campos obligatorios", "Todos los campos marcados son obligatorios.", Alert.AlertType.WARNING);
            return false;
        }

        if (!PHONE_PATTERN.matcher(telefono).matches()) {
            mostrarAlerta("Teléfono inválido", "El número de teléfono debe tener 9 dígitos.", Alert.AlertType.WARNING);
            return false;
        }

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            mostrarAlerta("Correo inválido", "El correo debe tener el formato correcto (ejemplo@dominio.com).", Alert.AlertType.WARNING);
            return false;
        }

        return true;
    }

    private void limpiarCampos() {
        txtNombre.clear();
        txtDireccion.clear();
        txtTelefono.clear();
        txtEmail.clear();
        txtZona.clear();
        txtObservaciones.clear();
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    /**
     * ✅ Método para abrir la ventana de edición de un distribuidor
     */
    private void abrirVentanaEdicion(Distribuidor distribuidor) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/EditarDistribuidorView.fxml"));
            Parent root = loader.load();
            EditarDistribuidorController controller = loader.getController();

            Stage stage = new Stage();
            controller.setDistribuidor(distribuidor, stage, this);

            stage.setScene(new Scene(root));
            stage.setTitle("Editar Distribuidor");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
