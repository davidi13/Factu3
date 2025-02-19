package com.facturacion.factu3.controllers;

import com.facturacion.factu3.models.Cliente;
import com.facturacion.factu3.models.ClienteDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class EditarClienteController {

    @FXML private TextField txtNombre, txtDireccion, txtCIF, txtTelefono, txtEmail, txtProvincia, txtPais, txtIban, txtRiesgo, txtDescuento;
    @FXML private TextArea txtObservaciones;
    @FXML private Button btnGuardar, btnEliminar;

    private Cliente clienteSeleccionado;
    private Stage ventana;
    private ClientesController clientesController; // 🔄 Para refrescar la tabla en la vista principal

    public void setCliente(Cliente cliente, Stage stage, ClientesController controller) {
        this.clienteSeleccionado = cliente;
        this.ventana = stage;
        this.clientesController = controller;

        txtNombre.setText(cliente.getNombreCliente());
        txtDireccion.setText(cliente.getDireccionCliente());
        txtCIF.setText(cliente.getCifCliente());
        txtTelefono.setText(cliente.getTelCliente());
        txtEmail.setText(cliente.getEmailCliente());
        txtProvincia.setText(cliente.getProvinciaCliente());
        txtPais.setText(cliente.getPaisCliente());
        txtIban.setText(cliente.getIbanCliente());
        txtRiesgo.setText(String.valueOf(cliente.getRiesgoCliente()));
        txtDescuento.setText(String.valueOf(cliente.getDescuentoCliente()));
        txtObservaciones.setText(cliente.getObservacionesCliente());
    }

    @FXML
    private void guardarCambios() {
        clienteSeleccionado.setNombreCliente(txtNombre.getText());
        clienteSeleccionado.setDireccionCliente(txtDireccion.getText());
        clienteSeleccionado.setCifCliente(txtCIF.getText());
        clienteSeleccionado.setTelCliente(txtTelefono.getText());
        clienteSeleccionado.setEmailCliente(txtEmail.getText());
        clienteSeleccionado.setProvinciaCliente(txtProvincia.getText());
        clienteSeleccionado.setPaisCliente(txtPais.getText());
        clienteSeleccionado.setIbanCliente(txtIban.getText());
        clienteSeleccionado.setRiesgoCliente(Double.parseDouble(txtRiesgo.getText()));
        clienteSeleccionado.setDescuentoCliente(Double.parseDouble(txtDescuento.getText()));
        clienteSeleccionado.setObservacionesCliente(txtObservaciones.getText());

        if (ClienteDAO.actualizarCliente(clienteSeleccionado)) {
            clientesController.actualizarTablaClientes();
            mostrarAlerta("Éxito", "Cliente actualizado correctamente.", Alert.AlertType.INFORMATION);
            ventana.close();
        } else {
            mostrarAlerta("Error", "No se pudo actualizar el cliente.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void eliminarCliente() {
        Alert confirmacion1 = new Alert(Alert.AlertType.CONFIRMATION,
                "¿Seguro que quieres eliminar este cliente?", ButtonType.YES, ButtonType.NO);
        confirmacion1.setTitle("Confirmar eliminación");
        confirmacion1.setHeaderText(null);
        confirmacion1.showAndWait();

        if (confirmacion1.getResult() == ButtonType.YES) {
            // Segunda confirmación
            Alert confirmacion2 = new Alert(Alert.AlertType.CONFIRMATION,
                    "⚠ Esta acción es irreversible. ¿Deseas continuar?", ButtonType.YES, ButtonType.NO);
            confirmacion2.setTitle("Última advertencia");
            confirmacion2.setHeaderText(null);
            confirmacion2.showAndWait();

            if (confirmacion2.getResult() == ButtonType.YES) {
                if (ClienteDAO.eliminarCliente(clienteSeleccionado.getId())) {
                    clientesController.actualizarTablaClientes();
                    mostrarAlerta("Eliminado", "Cliente eliminado correctamente.", Alert.AlertType.INFORMATION);
                    ventana.close();
                } else {
                    mostrarAlerta("Error", "No se pudo eliminar el cliente.", Alert.AlertType.ERROR);
                }
            }
        }
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
