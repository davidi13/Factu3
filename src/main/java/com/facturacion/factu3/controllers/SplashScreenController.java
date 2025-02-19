package com.facturacion.factu3.controllers;

import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class SplashScreenController {

    @FXML
    private ImageView imgLogo;

    @FXML
    private Text lblTexto;

    @FXML
    public void initialize() {
        // Cargar la imagen del logo
        imgLogo.setImage(new Image(getClass().getResourceAsStream("/images/logo.png")));

        // Aplicar la animación al logo y al texto
        iniciarAnimacion(imgLogo);
        iniciarAnimacion(lblTexto);
    }

    private void iniciarAnimacion(javafx.scene.Node node) {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(1.5), node);
        scaleTransition.setFromX(1);
        scaleTransition.setFromY(1);
        scaleTransition.setToX(1.2);
        scaleTransition.setToY(1.2);
        scaleTransition.setAutoReverse(true);
        scaleTransition.setCycleCount(ScaleTransition.INDEFINITE);
        scaleTransition.play();
    }
}
