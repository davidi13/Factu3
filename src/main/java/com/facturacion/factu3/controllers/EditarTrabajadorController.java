package com.facturacion.factu3.controllers;

import com.facturacion.factu3.models.Trabajador;
import com.facturacion.factu3.models.TrabajadorDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class EditarTrabajadorController {

    @FXML private TextField txtNombre, txtDireccion, txtTelefono, txtEmail, txtPuesto, txtSalario;
    @FXML private TextArea txtObservaciones;
    @FXML private Button btnGuardar, btnEliminar;

    private Trabajador trabajadorSeleccionado;
    private Stage ventana;
    private TrabajadoresController trabajadoresController;

    public void setTrabajador(Trabajador trabajador, Stage stage, TrabajadoresController controller) {
        this.trabajadorSeleccionado = trabajador;
        this.ventana = stage;
        this.trabajadoresController = controller;

        txtNombre.setText(trabajador.getNombre());
        txtDireccion.setText(trabajador.getDireccion());
        txtTelefono.setText(trabajador.getTelefono());
        txtEmail.setText(trabajador.getEmail());
        txtPuesto.setText(trabajador.getPuesto());
        txtSalario.setText(String.valueOf(trabajador.getSalario()));
        txtObservaciones.setText(trabajador.getObservaciones());
    }

    @FXML
    private void guardarCambios() {
        trabajadorSeleccionado.setNombre(txtNombre.getText());
        trabajadorSeleccionado.setDireccion(txtDireccion.getText());
        trabajadorSeleccionado.setTelefono(txtTelefono.getText());
        trabajadorSeleccionado.setEmail(txtEmail.getText());
        trabajadorSeleccionado.setPuesto(txtPuesto.getText());

        try {
            trabajadorSeleccionado.setSalario(Double.parseDouble(txtSalario.getText()));
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "El salario debe ser un número válido.", Alert.AlertType.ERROR);
            return;
        }

        trabajadorSeleccionado.setObservaciones(txtObservaciones.getText());

        if (TrabajadorDAO.actualizarTrabajador(trabajadorSeleccionado)) {
            mostrarAlerta("Éxito", "Trabajador actualizado correctamente.", Alert.AlertType.INFORMATION);
            trabajadoresController.actualizarTablaTrabajadores(); // 🔄 Recargar lista
            ventana.close();
        } else {
            mostrarAlerta("Error", "No se pudo actualizar el trabajador.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void eliminarTrabajador() {
        Alert confirmacion1 = new Alert(Alert.AlertType.CONFIRMATION,
                "¿Seguro que quieres eliminar este trabajador?", ButtonType.YES, ButtonType.NO);
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
                if (TrabajadorDAO.eliminarTrabajador(trabajadorSeleccionado.getId())) {
                    mostrarAlerta("Eliminado", "Trabajador eliminado correctamente.", Alert.AlertType.INFORMATION);
                    trabajadoresController.actualizarTablaTrabajadores(); // 🔄 Recargar lista
                    ventana.close();
                } else {
                    mostrarAlerta("Error", "No se pudo eliminar el trabajador.", Alert.AlertType.ERROR);
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
