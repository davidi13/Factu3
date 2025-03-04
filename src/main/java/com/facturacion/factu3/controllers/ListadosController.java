package com.facturacion.factu3.controllers;

import com.facturacion.factu3.models.ArticuloDAO;
import com.facturacion.factu3.models.ClienteDAO;
import com.facturacion.factu3.models.DistribuidorDAO;
import com.facturacion.factu3.models.ProveedorDAO;
import com.facturacion.factu3.models.TrabajadorDAO;
import com.facturacion.factu3.models.ComercialDAO;


import com.facturacion.factu3.utils.ExportarPDF;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.List;

public class ListadosController {

    @FXML private ComboBox<String> cmbCategoria;
    @FXML private TableView<Object> tablaDatos;

    @FXML
    public void initialize() {
        cmbCategoria.setItems(FXCollections.observableArrayList(
                "Clientes", "Distribuidores", "Proveedores", "Trabajadores", "Artículos", "Comerciales"
        ));
    }

    @FXML
    private void cargarDatos() {
        String categoriaSeleccionada = cmbCategoria.getSelectionModel().getSelectedItem();
        if (categoriaSeleccionada == null) {
            mostrarAlerta("Error", "Selecciona una categoría antes de cargar los datos.", Alert.AlertType.ERROR);
            return;
        }

        List<?> datos = obtenerDatos(categoriaSeleccionada);
        if (datos == null || datos.isEmpty()) {
            mostrarAlerta("Información", "No hay datos disponibles para esta categoría.", Alert.AlertType.INFORMATION);
            return;
        }

        actualizarTabla(datos);
    }

    private List<?> obtenerDatos(String categoria) {
        switch (categoria) {
            case "Clientes":
                return ClienteDAO.obtenerClientes();  // ✅ Usa el método correcto
            case "Distribuidores":
                return DistribuidorDAO.obtenerDistribuidores(); // ✅ Método correcto
            case "Proveedores":
                return ProveedorDAO.obtenerProveedores();  // ✅ Método correcto
            case "Trabajadores":
                return TrabajadorDAO.obtenerTrabajadores();  // ✅ Método correcto
            case "Artículos":
                return ArticuloDAO.obtenerArticulos();  // ✅ Método correcto
            case "Comerciales":
                return ComercialDAO.obtenerComerciales();  // ✅ Método correcto
            default:
                return null;
        }
    }


    private void actualizarTabla(List<?> datos) {
        tablaDatos.getColumns().clear();
        tablaDatos.getItems().clear();

        if (datos.isEmpty()) return;

        Object primeraFila = datos.get(0);
        for (var field : primeraFila.getClass().getDeclaredFields()) {
            field.setAccessible(true); // Permitir acceso a atributos privados
            TableColumn<Object, String> columna = new TableColumn<>(formatearNombreColumna(field.getName()));

            columna.setCellValueFactory(cellData -> {
                try {
                    Object valor = field.get(cellData.getValue());
                    return new SimpleStringProperty(valor != null ? valor.toString() : "N/A");
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    return new SimpleStringProperty("Error");
                }
            });

            tablaDatos.getColumns().add(columna);
        }

        tablaDatos.getItems().addAll(datos);
    }

    private String formatearNombreColumna(String nombreCampo) {
        return nombreCampo.replaceAll("([a-z])([A-Z]+)", "$1 $2").toUpperCase();
    }

    @FXML
    private void exportarPDF() {
        if (tablaDatos.getItems().isEmpty()) {
            mostrarAlerta("Error", "No hay datos para exportar.", Alert.AlertType.ERROR);
            return;
        }

        ExportarPDF.generarListadoPDF(tablaDatos, cmbCategoria);
    }


    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
