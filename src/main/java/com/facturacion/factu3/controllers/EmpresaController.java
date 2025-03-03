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

            // Mostrar la imagen si existe
            if (empresa.getLogo() != null && !empresa.getLogo().isEmpty()) {
                imgLogo.setImage(new Image(empresa.getLogo()));
            }
        }
    }


    @FXML
    private void actualizarEmpresa() {
        Empresa empresaActualizada = new Empresa(
                empresa.getId(),
                txtNombre.getText(),
                txtDireccion.getText(),
                txtTelefono.getText(),
                txtEmail.getText(),
                txtCIF.getText(),
                empresa.getLogo()  // O la nueva imagen si se seleccionó una
        );

        boolean resultado = EmpresaDAO.actualizarEmpresa(empresaActualizada);

        if (resultado) {
            mostrarAlerta("Éxito", "Los datos de la empresa se actualizaron correctamente.", Alert.AlertType.INFORMATION);
        } else {
            mostrarAlerta("Error", "Hubo un problema al actualizar los datos.", Alert.AlertType.ERROR);
        }
    }


    @FXML
    private void seleccionarLogo() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg"));
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            String rutaImagen = file.toURI().toString();  // Obtener la ruta en formato URI
            empresa.setLogo(rutaImagen);  // Actualizar en la empresa actual

            // Actualizar la imagen en la UI
            imgLogo.setImage(new Image(rutaImagen));

            // Guardar la nueva imagen en la base de datos
            boolean resultado = EmpresaDAO.actualizarEmpresa(empresa);

            if (resultado) {
                mostrarAlerta("Éxito", "El logo se ha actualizado correctamente.", Alert.AlertType.INFORMATION);
            } else {
                mostrarAlerta("Error", "No se pudo actualizar el logo.", Alert.AlertType.ERROR);
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
