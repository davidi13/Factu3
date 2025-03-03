package com.facturacion.factu3.utils;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

public class ImprimirFactura {
    public static void imprimirPDF(Stage stage) {
        // Configurar el FileChooser para seleccionar un archivo PDF
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Factura para Imprimir");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home") + "/Downloads"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos PDF", "*.pdf"));

        // Mostrar el diálogo para seleccionar el archivo
        File archivoSeleccionado = fileChooser.showOpenDialog(stage);
        if (archivoSeleccionado == null) {
            System.out.println("Impresión cancelada: No se seleccionó ningún archivo.");
            return;
        }

        try {
            // Usar Desktop para imprimir el PDF con la aplicación predeterminada
            Desktop desktop = Desktop.getDesktop();
            if (desktop.isSupported(Desktop.Action.PRINT)) {
                desktop.print(archivoSeleccionado);
                System.out.println("Factura enviada a la impresora: " + archivoSeleccionado.getName());
            } else {
                System.err.println("La impresión no está soportada en este sistema.");
            }
        } catch (IOException e) {
            System.err.println("Error al imprimir la factura: " + e.getMessage());
        }
    }
}
