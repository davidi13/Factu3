package com.facturacion.factu3.controllers;

import com.facturacion.factu3.models.Cliente;
import com.facturacion.factu3.models.ClienteDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.regex.Pattern;

public class ClientesController {

    @FXML
    private TextField txtNombre, txtDireccion, txtCIF, txtTelefono, txtEmail, txtProvincia, txtPais, txtIban, txtRiesgo, txtDescuento;

    @FXML
    private TextArea txtObservaciones;

    @FXML
    private TableView<Cliente> tblClientes;

    @FXML
    private TableColumn<Cliente, Integer> colId;

    @FXML
    private TableColumn<Cliente, String> colNombre, colDireccion, colCif, colTelefono, colEmail, colProvincia, colPais, colIban, colObservaciones;

    @FXML
    private TableColumn<Cliente, Double> colRiesgo, colDescuento;

    private ObservableList<Cliente> listaClientes;

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\d{9}$");
    private static final Pattern NUMERIC_PATTERN = Pattern.compile("^\\d+(\\.\\d{1,2})?$");

    @FXML
    public void initialize() {

        tblClientes.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Doble clic en la tabla
                Cliente clienteSeleccionado = tblClientes.getSelectionModel().getSelectedItem();
                if (clienteSeleccionado != null) {
                    abrirVentanaEdicion(clienteSeleccionado);
                }
            }
        });

        System.out.println("➡ Inicializando ClientesController...");

        listaClientes = FXCollections.observableArrayList(ClienteDAO.obtenerClientes());
        tblClientes.setItems(listaClientes);

        colId.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        colNombre.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNombreCliente()));
        colDireccion.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDireccionCliente()));
        colCif.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getCifCliente()));
        colTelefono.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTelCliente()));
        colEmail.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getEmailCliente()));
        colProvincia.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getProvinciaCliente()));
        colPais.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getPaisCliente()));
        colIban.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getIbanCliente()));
        colRiesgo.setCellValueFactory(cellData -> new javafx.beans.property.SimpleDoubleProperty(cellData.getValue().getRiesgoCliente()).asObject());
        colDescuento.setCellValueFactory(cellData -> new javafx.beans.property.SimpleDoubleProperty(cellData.getValue().getDescuentoCliente()).asObject());
        colObservaciones.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getObservacionesCliente()));

        System.out.println("✅ Tabla de clientes cargada con éxito.");
    }

    // ✅ Nuevo método para abrir la ventana de edición
    private void abrirVentanaEdicion(Cliente cliente) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/EditarClienteView.fxml"));
            Parent root = loader.load();
            EditarClienteController controller = loader.getController();

            Stage stage = new Stage();
            controller.setCliente(cliente, stage, this); // 🔄 Pasamos `this` para actualizar la tabla al cerrar la ventana

            stage.setScene(new Scene(root));
            stage.setTitle("Editar Cliente");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ✅ Método para refrescar la tabla después de editar o eliminar
    public void actualizarTablaClientes() {
        listaClientes.setAll(ClienteDAO.obtenerClientes()); // 🔄 Refrescar la lista sin recargar toda la ventana
    }

    @FXML
    private void guardarCliente() {
        System.out.println("➡ Intentando guardar cliente...");

        if (!validarCampos()) {
            System.out.println("❌ Error: Falló la validación de los datos.");
            return;
        }

        int nuevoId = ClienteDAO.obtenerSiguienteId();
        System.out.println("🆕 Próximo ID disponible: " + nuevoId);

        Cliente nuevoCliente = new Cliente(
                nuevoId,
                txtNombre.getText(),
                txtDireccion.getText(),
                "00000",  // 🚨 Faltaba CP (puedes agregar un campo txtCp si es necesario)
                "Ciudad",  // 🚨 Faltaba Población (puedes agregar un campo txtPoblacion si es necesario)
                txtProvincia.getText(),
                txtPais.getText(),
                txtCIF.getText(),
                txtTelefono.getText(),
                txtEmail.getText(),
                txtIban.getText(),
                Double.parseDouble(txtRiesgo.getText().isEmpty() ? "0" : txtRiesgo.getText()),
                Double.parseDouble(txtDescuento.getText().isEmpty() ? "0" : txtDescuento.getText()),
                txtObservaciones.getText()
        );

        System.out.println("📌 Cliente a guardar: " + nuevoCliente);

        if (ClienteDAO.agregarCliente(nuevoCliente)) {
            listaClientes.add(nuevoCliente);
            limpiarCampos();
            System.out.println("✅ Cliente guardado correctamente.");
        } else {
            System.out.println("❌ Error: No se pudo guardar el cliente en la BD.");
            mostrarAlerta("Error", "No se pudo guardar el cliente.");
        }
    }


    private boolean validarCampos() {
        if (txtNombre.getText().trim().isEmpty() || txtCIF.getText().trim().isEmpty()) {
            mostrarAlerta("Campos obligatorios", "Los campos Nombre y CIF son obligatorios.");
            return false;
        }

        if (!PHONE_PATTERN.matcher(txtTelefono.getText().trim()).matches()) {
            mostrarAlerta("Teléfono inválido", "El número de teléfono debe tener 9 dígitos.");
            return false;
        }

        if (!EMAIL_PATTERN.matcher(txtEmail.getText().trim()).matches()) {
            mostrarAlerta("Correo inválido", "El correo debe tener el formato correcto (ejemplo@dominio.com).");
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
        txtProvincia.clear();
        txtPais.clear();
        txtIban.clear();
        txtRiesgo.clear();
        txtDescuento.clear();
        txtObservaciones.clear();
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
