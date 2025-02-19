package com.facturacion.factu3.controllers;

import com.facturacion.factu3.models.Empresa;
import com.facturacion.factu3.models.EmpresaDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;

public class EmpresaController {

    @FXML private TextField txtNombre, txtDireccion, txtTelefono, txtEmail, txtCIF;
    @FXML private ImageView imgLogo;
    @FXML private Button btnActualizar, btnSeleccionarLogo;

    private Empresa empresa;

    @FXML
    public void initialize() {
        cargarDatosEmpresa();
    }

    private void cargarDatosEmpresa() {
        empresa = EmpresaDAO.obtenerEmpresa();
        if (empresa != null) {
            txtNombre.setText(empresa.getNombre());
            txtDireccion.setText(empresa.getDireccion());
            txtTelefono.setText(empresa.getTelefono());
            txtEmail.setText(empresa.getEmail());
            txtCIF.setText(empresa.getCif());

            if (empresa.getLogo() != null && !empresa.getLogo().isEmpty()) {
                imgLogo.setImage(new Image("file:" + empresa.getLogo()));
            }
        }
    }

    @FXML
    private void actualizarEmpresa() {
        empresa.setNombre(txtNombre.getText());
        empresa.setDireccion(txtDireccion.getText());
        empresa.setTelefono(txtTelefono.getText());
        empresa.setEmail(txtEmail.getText());
        empresa.setCif(txtCIF.getText());

        if (EmpresaDAO.actualizarEmpresa(empresa)) {
            mostrarAlerta("Éxito", "Datos actualizados correctamente.", Alert.AlertType.INFORMATION);
        } else {
            mostrarAlerta("Error", "No se pudieron actualizar los datos.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void seleccionarLogo() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg"));
        File file = fileChooser.showOpenDialog(new Stage());

        if (file != null) {
            imgLogo.setImage(new Image(file.toURI().toString()));
            empresa.setLogo(file.getAbsolutePath());
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
