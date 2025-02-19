package com.facturacion.factu3.controllers;

import com.facturacion.factu3.models.Comercial;
import com.facturacion.factu3.models.ComercialDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class EditarComercialController {

    @FXML private TextField txtNombre, txtDireccion, txtTelefono, txtEmail, txtComision;
    @FXML private TextArea txtObservaciones;
    @FXML private Button btnGuardar, btnEliminar;

    private Comercial comercialSeleccionado;
    private Stage ventana;
    private ComercialesController comercialesController;

    public void setComercial(Comercial comercial, Stage stage, ComercialesController controller) {
        this.comercialSeleccionado = comercial;
        this.ventana = stage;
        this.comercialesController = controller;

        txtNombre.setText(comercial.getNombre());
        txtDireccion.setText(comercial.getDireccion());
        txtTelefono.setText(comercial.getTelefono());
        txtEmail.setText(comercial.getEmail());
        txtComision.setText(String.valueOf(comercial.getComision()));
        txtObservaciones.setText(comercial.getObservaciones());
    }

    @FXML
    private void guardarCambios() {
        comercialSeleccionado.setNombre(txtNombre.getText());
        comercialSeleccionado.setDireccion(txtDireccion.getText());
        comercialSeleccionado.setTelefono(txtTelefono.getText());
        comercialSeleccionado.setEmail(txtEmail.getText());
        comercialSeleccionado.setComision(Double.parseDouble(txtComision.getText()));
        comercialSeleccionado.setObservaciones(txtObservaciones.getText());

        if (ComercialDAO.actualizarComercial(comercialSeleccionado)) {
            mostrarAlerta("Éxito", "Comercial actualizado correctamente.", Alert.AlertType.INFORMATION);
            comercialesController.actualizarTablaComerciales();
            ventana.close();
        } else {
            mostrarAlerta("Error", "No se pudo actualizar el comercial.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void eliminarComercial() {
        Alert confirmacion1 = new Alert(Alert.AlertType.CONFIRMATION,
                "¿Seguro que quieres eliminar este comercial?", ButtonType.YES, ButtonType.NO);
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
                if (ComercialDAO.eliminarComercial(comercialSeleccionado.getId())) {
                    mostrarAlerta("Eliminado", "Comercial eliminado correctamente.", Alert.AlertType.INFORMATION);
                    comercialesController.actualizarTablaComerciales();
                    ventana.close();
                } else {
                    mostrarAlerta("Error", "No se pudo eliminar el comercial.", Alert.AlertType.ERROR);
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
