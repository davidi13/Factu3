package com.facturacion.factu3;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class MainApp extends Application {

    private static final double LOGIN_WIDTH = 325;
    private static final double LOGIN_HEIGHT = 400;

    @Override
    public void start(Stage primaryStage) {
        mostrarLogin(primaryStage); // 🔹 Inicia la app con la ventana de login
    }

    /**
     * 📌 Método para mostrar la ventana de Login con el tamaño correcto.
     */
    public void mostrarLogin(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/LoginView.fxml"));
            Scene scene = new Scene(loader.load(), LOGIN_WIDTH, LOGIN_HEIGHT);

            stage.setTitle("Factu3 - Login");
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/logo.png")));

            stage.setScene(scene);
            stage.setMinWidth(LOGIN_WIDTH);
            stage.setMinHeight(LOGIN_HEIGHT);
            stage.setResizable(false); // 🔹 Evita que el usuario cambie el tamaño
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 📌 Método para mostrar la ventana principal después de un login exitoso.
     */
    public void mostrarMain(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/MainView.fxml"));
            Scene scene = new Scene(loader.load(), 1280, 720);

            stage.setTitle("Factu3 - Sistema de Facturación");
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/logo.png")));

            stage.setScene(scene);
            stage.setMinWidth(800);
            stage.setMinHeight(600);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
