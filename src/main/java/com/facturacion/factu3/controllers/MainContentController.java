package com.facturacion.factu3.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import java.io.IOException;

public class MainContentController {

    @FXML
    private StackPane contentPane;

    public void loadView(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Pane newView = loader.load();
            contentPane.getChildren().setAll(newView); // Reemplaza el contenido actual
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
