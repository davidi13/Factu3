package com.facturacion.factu3.controllers;

import com.facturacion.factu3.models.FacturaDAO;
import com.facturacion.factu3.models.Factura;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.List;

public class VerFacturaController {

    @FXML private TableView<Factura> tablaFacturas;
    @FXML private TableColumn<Factura, Integer> colNumeroFactura;
    @FXML private TableColumn<Factura, String> colFecha;
    @FXML private TableColumn<Factura, String> colCliente;
    @FXML private TableColumn<Factura, Double> colBaseImponible;
    @FXML private TableColumn<Factura, Double> colIVA;
    @FXML private TableColumn<Factura, Double> colTotal;
    @FXML private TableColumn<Factura, String> colFormaPago;
    @FXML private TableColumn<Factura, String> colFechaCobro;

    private ObservableList<Factura> facturasList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Configurar columnas correctamente
        colNumeroFactura.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getNumeroFactura()).asObject());

        colFecha.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getFecha().toString()));

        colCliente.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty("ID Cliente: " + cellData.getValue().getIdCliente()));

        colCliente.setCellValueFactory(cellData ->
                new SimpleStringProperty("ID: " + cellData.getValue().getIdCliente() + " - " + cellData.getValue().getNombreCliente()));

        colBaseImponible.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleDoubleProperty(cellData.getValue().getBaseImponible()).asObject());

        colIVA.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleDoubleProperty(cellData.getValue().getIva()).asObject());

        colTotal.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleDoubleProperty(cellData.getValue().getTotal()).asObject());

        colFormaPago.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(String.valueOf(cellData.getValue().getFormaPago())));

        colFechaCobro.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getFechaCobro() != null ? cellData.getValue().getFechaCobro().toString() : "No cobrado"
                ));

        // Cargar datos en la tabla
        cargarFacturas();
    }


    private void cargarFacturas() {
        List<Factura> facturas = FacturaDAO.obtenerTodasLasFacturas();
        facturasList.setAll(facturas);
        tablaFacturas.setItems(facturasList);
    }

    @FXML
    private void cerrarVentana() {
        Stage stage = (Stage) tablaFacturas.getScene().getWindow();
        stage.close();
    }
}
