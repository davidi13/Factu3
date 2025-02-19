package com.facturacion.factu3.controllers;

import com.facturacion.factu3.models.Usuario;
import com.facturacion.factu3.models.UsuarioDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class LoginController {

    @FXML private ImageView imgLogo;
    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtPassword;
    @FXML private Label lblError;

    @FXML
    public void initialize() {
        imgLogo.setImage(new Image(getClass().getResourceAsStream("/images/logo.png")));
    }

    @FXML
    private void handleLogin() {
        String usuarioIngresado = txtUsuario.getText().trim();
        String passwordIngresada = txtPassword.getText().trim();

        if (usuarioIngresado.isEmpty() || passwordIngresada.isEmpty()) {
            lblError.setText("⚠ Los campos no pueden estar vacíos.");
            return;
        }

        // ✅ Verificar credenciales en la base de datos
        Usuario usuario = UsuarioDAO.verificarCredenciales(usuarioIngresado, passwordIngresada);

        if (usuario != null) {
            abrirMainView(); // ✅ Abrir la vista principal si las credenciales son correctas
        } else {
            lblError.setText("❌ Usuario o contraseña incorrectos.");
        }
    }

    private void abrirMainView() {
        try {
            Stage stageActual = (Stage) txtUsuario.getScene().getWindow();
            stageActual.close(); // 🔹 Cerrar ventana de login

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/MainView.fxml"));
            Scene scene = new Scene(loader.load(), 1280, 720); // ✅ Tamaño inicial

            Stage stage = new Stage();
            stage.setTitle("Factu3 - Sistema de Facturación");
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/logo.png")));
            stage.setScene(scene);

            // ✅ Permitir redimensionar y maximizar
            stage.setResizable(true);
            stage.setMaximized(true); // Abre en pantalla completa

            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
