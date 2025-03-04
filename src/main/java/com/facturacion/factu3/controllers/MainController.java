package com.facturacion.factu3.controllers;

import com.facturacion.factu3.MainApp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.Pane;
import com.facturacion.factu3.database.DatabaseConnection;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

public class MainController {

    @FXML
    private MenuItem menuProveedores, menuClientes, menuTrabajadores, menuComerciales, menuDistribuidores, menuFacturas;

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

    // MÉTODOS PARA CARGAR LAS VISTAS
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
        System.out.println("🏢 Cargando vista de Artículo...");
        loadView("/views/ArticuloView.fxml");
    }

    @FXML
    private void handleComerciales() {
        System.out.println("📊 Cargando vista de Comerciales...");
        loadView("/views/ComercialesView.fxml");
    }

    @FXML
    private void handleHome() {
        loadView("/views/SplashScreen.fxml");
    }

    // CARGAR FACTURAS
    @FXML
    private void handleCrearFactura() {
        System.out.println("🧾 Cargando vista de Crear Factura...");
        loadView("/views/CrearFacturaView.fxml");
    }

    // MÉTODO PARA CARGAR VISTAS
    private void loadView(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Pane view = loader.load();
            mainContent.getChildren().setAll(view);
        } catch (IOException e) {
            System.err.println("⚠ ERROR: No se pudo cargar la vista: " + fxmlPath);
            e.printStackTrace();
        }
    }

    // CERRAR SESIÓN Y VOLVER AL LOGIN
    @FXML
    private void handleLogout() {
        try {
            Stage stage = (Stage) mainContent.getScene().getWindow();
            stage.close(); // Cierra la ventana actual
            new MainApp().mostrarLogin(new Stage()); // Muestra la pantalla de inicio de sesión
        } catch (Exception e) {
            System.err.println("⚠ ERROR: No se pudo cerrar la sesión.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleListados() {
        System.out.println("📊 Cargando vista de Listados...");
        loadView("/views/ListadosView.fxml");
    }

    @FXML
    private void handleVerFactura() {
        System.out.println("📊 Cargando vista de Facturas...");
        loadView("/views/VerFacturaView.fxml");
    }

    public void handleCrearRectificativa(ActionEvent actionEvent) {
        System.out.println("📊 Cargando vista de Crear Rectificativas...");
        loadView("/views/RectificativasView.fxml");
    }

    public void handleVerRectificativa(ActionEvent actionEvent) {
        System.out.println("📊 Cargando vista de Ver Rectificativas...");
        loadView("/views/VerRectificativasView.fxml");
    }

    @FXML
    private void abrirManualUsuario() {
        try {
            // Obtener la ruta del archivo dentro de resources
            URL pdfURL = getClass().getResource("/manual/Manual_Usuario_Factu3.pdf");

            if (pdfURL != null) {
                File pdfFile = new File(pdfURL.toURI());

                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(pdfFile);
                } else {
                    System.err.println("❌ La apertura de archivos no es compatible en este sistema.");
                }
            } else {
                System.err.println("❌ No se encontró el archivo PDF en resources.");
            }
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            System.err.println("❌ Error al intentar abrir el manual de usuario.");
        }
    }
}
