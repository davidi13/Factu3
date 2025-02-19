package com.facturacion.factu3.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javafx.scene.control.Label;
import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class DatabaseConnection {

    private static final String URL = "jdbc:mysql://localhost:3307/gestion?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USUARIO = "root";
    private static final String CONTRASEÑA = "admin";

    /**
     * Método para obtener la conexión a la base de datos.
     * @return Connection
     */
    public static Connection obtenerConexion() {
        try {
            return DriverManager.getConnection(URL, USUARIO, CONTRASEÑA);
        } catch (SQLException e) {
            System.err.println("❌ Error al conectar con la base de datos: " + e.getMessage());
            return null;
        }
    }

    /**
     * Método para verificar y actualizar el estado de conexión en un Label de JavaFX.
     * @param statusLabel Label donde se mostrará el estado de la conexión.
     */
    public static void verificarConexion(Label statusLabel) {
        try (Connection conexion = obtenerConexion()) {
            if (conexion != null) {
                statusLabel.setText("✅ CONEXIÓN EXITOSA |");
                statusLabel.setStyle("-fx-text-fill: green;");
            } else {
                statusLabel.setText("❌ CONEXIÓN FALLIDA |");
                statusLabel.setStyle("-fx-text-fill: red;");
            }
        } catch (Exception e) {
            statusLabel.setText("❌ Error desconocido");
            statusLabel.setStyle("-fx-text-fill: red;");
            e.printStackTrace();
        }
    }

    /**
     * Configura un temporizador para actualizar un Label con la fecha y hora actuales en JavaFX.
     * @param dateTimeLabel Label donde se mostrará la fecha y hora.
     */
    public static void configurarTimer(Label dateTimeLabel) {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            String dateTime = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
            dateTimeLabel.setText(dateTime);
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
}
