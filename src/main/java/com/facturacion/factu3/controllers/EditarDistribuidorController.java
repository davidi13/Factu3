package com.facturacion.factu3.controllers;

import com.facturacion.factu3.models.Distribuidor;
import com.facturacion.factu3.models.DistribuidorDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class EditarDistribuidorController {

    @FXML private TextField txtNombre, txtDireccion, txtTelefono, txtEmail, txtZona;
    @FXML private TextArea txtObservaciones;
    @FXML private Button btnGuardar, btnEliminar;

    private Distribuidor distribuidorSeleccionado;
    private Stage ventana;
    private DistribuidoresController distribuidoresController;

    public void setDistribuidor(Distribuidor distribuidor, Stage stage, DistribuidoresController controller) {
        this.distribuidorSeleccionado = distribuidor;
        this.ventana = stage;
        this.distribuidoresController = controller;

        // Rellenar los campos con los datos del distribuidor
        txtNombre.setText(distribuidor.getNombre());
        txtDireccion.setText(distribuidor.getDireccion());
        txtTelefono.setText(distribuidor.getTelefono());
        txtEmail.setText(distribuidor.getEmail());
        txtZona.setText(distribuidor.getZona());
        txtObservaciones.setText(distribuidor.getObservaciones());
    }

    @FXML
    private void guardarCambios() {
        distribuidorSeleccionado.setNombre(txtNombre.getText());
        distribuidorSeleccionado.setDireccion(txtDireccion.getText());
        distribuidorSeleccionado.setTelefono(txtTelefono.getText());
        distribuidorSeleccionado.setEmail(txtEmail.getText());
        distribuidorSeleccionado.setZona(txtZona.getText());
        distribuidorSeleccionado.setObservaciones(txtObservaciones.getText());

        if (DistribuidorDAO.actualizarDistribuidor(distribuidorSeleccionado)) {
            mostrarAlerta("Éxito", "Distribuidor actualizado correctamente.", Alert.AlertType.INFORMATION);
            distribuidoresController.actualizarTablaDistribuidores(); // 🔄 Recargar lista
            ventana.close();
        } else {
            mostrarAlerta("Error", "No se pudo actualizar el distribuidor.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void eliminarDistribuidor() {
        Alert confirmacion1 = new Alert(Alert.AlertType.CONFIRMATION,
                "¿Seguro que quieres eliminar este distribuidor?", ButtonType.YES, ButtonType.NO);
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
                if (DistribuidorDAO.eliminarDistribuidor(distribuidorSeleccionado.getId())) {
                    mostrarAlerta("Eliminado", "Distribuidor eliminado correctamente.", Alert.AlertType.INFORMATION);
                    distribuidoresController.actualizarTablaDistribuidores(); // 🔄 Recargar lista
                    ventana.close();
                } else {
                    mostrarAlerta("Error", "No se pudo eliminar el distribuidor.", Alert.AlertType.ERROR);
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
