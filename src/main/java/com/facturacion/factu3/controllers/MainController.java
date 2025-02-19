package com.facturacion.factu3.controllers;

import com.facturacion.factu3.MainApp;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.Pane;
import com.facturacion.factu3.database.DatabaseConnection;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {

    @FXML
    private MenuItem menuProveedores, menuClientes, menuTrabajadores, menuComerciales, menuDistribuidores;

    @FXML
    private StackPane mainContent;

    @FXML
    private Label statusLabel, dateTimeLabel;

    @FXML
    public void initialize() {
        // Verificar la conexión a la base de datos
        if (DatabaseConnection.obtenerConexion() != null) {
            statusLabel.setText("✅ CONEXIÓN EXITOSA");
            statusLabel.setStyle("-fx-text-fill: green;");
        } else {
            statusLabel.setText("❌ CONEXIÓN FALLIDA");
            statusLabel.setStyle("-fx-text-fill: red;");
        }

        // Configurar el timer para la fecha y hora
        DatabaseConnection.configurarTimer(dateTimeLabel);

        // Cargar la vista inicial
        loadView("/views/SplashScreen.fxml");
    }

    @FXML
    private void handleClientes() {
        loadView("/views/ClientesView.fxml");
    }

    @FXML
    private void handleProveedores() {
        System.out.println("📦 Cargando vista de Proveedores...");
        loadView("/views/ProveedoresView.fxml");
    }

     @FXML
    private void handleTrabajadores() {
        System.out.println("👷 Cargando vista de Trabajadores...");
        loadView("/views/TrabajadoresView.fxml");
    }
    @FXML
    private void handleEmpresa() {
        System.out.println("🏢 Cargando vista de Empresa...");
        loadView("/views/EmpresaView.fxml");
    }

    @FXML
    private void handleDistribuidores() {
        System.out.println("🏢 Cargando vista de Distribuidores...");
        loadView("/views/DistribuidoresView.fxml");
    }

    @FXML
    private void handleArticulo() {
        System.out.println("🏢 Cargando vista de Articulo...");
        loadView("/views/ArticuloView.fxml");
    }


    @FXML
    private void handleComerciales() {
        System.out.println("📊 Cargando vista de Comerciales...");
        loadView("/views/ComercialesView.fxml");
    }


    /*
    @FXML
    private void handleDistribuidores() {
        System.out.println("🚚 Cargando vista de Distribuidores...");
        loadView("/views/DistribuidoresView.fxml");
    }
    */

    @FXML
    private void handleHome() {
        loadView("/views/SplashScreen.fxml");
    }

    @FXML
    private void handleLogout() {
        Stage stage = (Stage) mainContent.getScene().getWindow(); // 🔹 Obtener la ventana actual
        stage.close(); // 🔹 Cerrar la ventana actual

        // 🔹 Volver a mostrar la ventana de login con el tamaño correcto
        new MainApp().mostrarLogin(new Stage());
    }

    private void loadView(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Pane view = loader.load();
            mainContent.getChildren().setAll(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
