package com.facturacion.factu3.controllers;

import com.facturacion.factu3.models.Proveedor;
import com.facturacion.factu3.models.ProveedorDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class EditarProveedorController {

    @FXML private TextField txtNombre, txtDireccion, txtCIF, txtTelefono, txtEmail;
    @FXML private TextArea txtObservaciones;
    @FXML private Button btnGuardar, btnEliminar;

    private Proveedor proveedorSeleccionado;
    private Stage ventana;
    private ProveedoresController proveedoresController;

    public void setProveedor(Proveedor proveedor, Stage stage, ProveedoresController controller) {
        this.proveedorSeleccionado = proveedor;
        this.ventana = stage;
        this.proveedoresController = controller;

        txtNombre.setText(proveedor.getNombre());
        txtDireccion.setText(proveedor.getDireccion());
        txtCIF.setText(proveedor.getCif());
        txtTelefono.setText(proveedor.getTelefono());
        txtEmail.setText(proveedor.getEmail());
        txtObservaciones.setText(proveedor.getObservaciones());
    }

    @FXML
    private void guardarCambios() {
        proveedorSeleccionado.setNombre(txtNombre.getText());
        proveedorSeleccionado.setDireccion(txtDireccion.getText());
        proveedorSeleccionado.setCif(txtCIF.getText());
        proveedorSeleccionado.setTelefono(txtTelefono.getText());
        proveedorSeleccionado.setEmail(txtEmail.getText());
        proveedorSeleccionado.setObservaciones(txtObservaciones.getText());

        if (ProveedorDAO.actualizarProveedor(proveedorSeleccionado)) {
            mostrarAlerta("Éxito", "Proveedor actualizado correctamente.", Alert.AlertType.INFORMATION);
            proveedoresController.actualizarTablaProveedores(); // 🔄 Recargar lista
            ventana.close();
        } else {
            mostrarAlerta("Error", "No se pudo actualizar el proveedor.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void eliminarProveedor() {
        Alert confirmacion1 = new Alert(Alert.AlertType.CONFIRMATION,
                "¿Seguro que quieres eliminar este proveedor?", ButtonType.YES, ButtonType.NO);
        confirmacion1.setTitle("Confirmar eliminación");
        confirmacion1.setHeaderText(null);
        confirmacion1.showAndWait();

        if (confirmacion1.getResult() == ButtonType.YES) {
            Alert confirmacion2 = new Alert(Alert.AlertType.CONFIRMATION,
                    "⚠ Esta acción es irreversible. ¿Deseas continuar?", ButtonType.YES, ButtonType.NO);
            confirmacion2.setTitle("Última advertencia");
            confirmacion2.setHeaderText(null);
            confirmacion2.showAndWait();

            if (confirmacion2.getResult() == ButtonType.YES) {
                if (ProveedorDAO.eliminarProveedor(proveedorSeleccionado.getId())) {
                    mostrarAlerta("Eliminado", "Proveedor eliminado correctamente.", Alert.AlertType.INFORMATION);
                    proveedoresController.actualizarTablaProveedores(); // 🔄 Recargar lista
                    ventana.close();
                } else {
                    mostrarAlerta("Error", "No se pudo eliminar el proveedor.", Alert.AlertType.ERROR);
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
