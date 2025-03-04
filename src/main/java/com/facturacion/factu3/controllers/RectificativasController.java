package com.facturacion.factu3.controllers;

import com.facturacion.factu3.database.DatabaseConnection;
import com.facturacion.factu3.models.Factura;
import com.facturacion.factu3.models.FacturaDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class RectificativasController {

    @FXML private ComboBox<Integer> comboFacturas;
    @FXML private TableView<Factura> tablaFacturas;

    @FXML private TableColumn<Factura, Integer> colNumeroFactura;
    @FXML private TableColumn<Factura, String> colFecha;
    @FXML private TableColumn<Factura, String> colCliente;
    @FXML private TableColumn<Factura, Double> colBaseImponible;
    @FXML private TableColumn<Factura, Double> colIVA;
    @FXML private TableColumn<Factura, Double> colTotal;
    @FXML private TableColumn<Factura, String> colFormaPago;
    @FXML private TableColumn<Factura, String> colFechaCobro;

    @FXML private Button btnRectificar;

    private ObservableList<Factura> facturasList = FXCollections.observableArrayList();
    private List<Factura> todasLasFacturas;

    @FXML
    public void initialize() {
        cargarFacturasDisponibles();
        configurarColumnasTabla();
        comboFacturas.setOnAction(event -> mostrarFacturaSeleccionada());
        btnRectificar.setOnAction(event -> rectificarFactura());
    }

    private void cargarFacturasDisponibles() {
        todasLasFacturas = FacturaDAO.obtenerTodasLasFacturas();
        ObservableList<Integer> idsFacturas = FXCollections.observableArrayList();

        for (Factura factura : todasLasFacturas) {
            idsFacturas.add(factura.getNumeroFactura());
        }

        comboFacturas.setItems(idsFacturas);
    }

    private void configurarColumnasTabla() {
        colNumeroFactura.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getNumeroFactura()).asObject());

        colFecha.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getFecha().toString()));

        colCliente.setCellValueFactory(cellData ->
                new SimpleStringProperty("ID: " + cellData.getValue().getIdCliente() + " - " + cellData.getValue().getNombreCliente()));

        colBaseImponible.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleDoubleProperty(cellData.getValue().getBaseImponible()).asObject());

        colIVA.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleDoubleProperty(cellData.getValue().getIva()).asObject());

        colTotal.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleDoubleProperty(cellData.getValue().getTotal()).asObject());

        colFormaPago.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getFormaPago())));

        colFechaCobro.setCellValueFactory(cellData ->
                new SimpleStringProperty(
                        cellData.getValue().getFechaCobro() != null ? cellData.getValue().getFechaCobro().toString() : "No cobrado"
                ));
    }

    private void mostrarFacturaSeleccionada() {
        Integer numeroFacturaSeleccionado = comboFacturas.getValue();
        if (numeroFacturaSeleccionado != null) {
            for (Factura factura : todasLasFacturas) {
                if (factura.getNumeroFactura() == numeroFacturaSeleccionado) {
                    facturasList.setAll(factura);
                    tablaFacturas.setItems(facturasList);
                    return;
                }
            }
        }
    }

    private void rectificarFactura() {
        Integer numeroFacturaSeleccionado = comboFacturas.getValue();
        if (numeroFacturaSeleccionado != null) {
            Factura facturaAEliminar = null;

            for (Factura factura : todasLasFacturas) {
                if (factura.getNumeroFactura() == numeroFacturaSeleccionado) {
                    facturaAEliminar = factura;
                    break;
                }
            }

            if (facturaAEliminar != null) {
                Connection conexion = DatabaseConnection.obtenerConexion();
                if (conexion != null) {
                    try {
                        // Insertar la factura en rectificativasClientes
                        PreparedStatement insertStmt = conexion.prepareStatement(
                                "INSERT INTO rectificativasClientes (numeroRectificativaCliente, fechaRectificativaCliente, " +
                                        "idClienteRectificativaCliente, baseImponibleRectificativaCliente, ivaRectificativaCliente, " +
                                        "totalRectificativaCliente, observacionesRectificativaCliente) " +
                                        "VALUES (?, ?, ?, ?, ?, ?, ?)"
                        );

                        insertStmt.setInt(1, facturaAEliminar.getNumeroFactura());
                        insertStmt.setDate(2, new java.sql.Date(facturaAEliminar.getFecha().getTime()));
                        insertStmt.setInt(3, facturaAEliminar.getIdCliente());
                        insertStmt.setDouble(4, facturaAEliminar.getBaseImponible());
                        insertStmt.setDouble(5, facturaAEliminar.getIva());
                        insertStmt.setDouble(6, facturaAEliminar.getTotal());
                        insertStmt.setString(7, "Factura rectificada");

                        insertStmt.executeUpdate();

                        // Eliminar la factura de facturasClientes
                        PreparedStatement deleteStmt = conexion.prepareStatement("DELETE FROM facturasClientes WHERE numeroFacturaCliente = ?");
                        deleteStmt.setInt(1, numeroFacturaSeleccionado);
                        deleteStmt.executeUpdate();

                        conexion.close();

                        // Refrescar la vista
                        cargarFacturasDisponibles();
                        tablaFacturas.getItems().clear();
                        comboFacturas.setValue(null);

                        mostrarAlerta("Éxito", "Factura " + numeroFacturaSeleccionado + " rectificada correctamente.");
                    } catch (SQLException e) {
                        mostrarAlerta("Error", "No se pudo rectificar la factura.");
                        e.printStackTrace();
                    }
                } else {
                    mostrarAlerta("Error", "No hay conexión a la base de datos.");
                }
            }
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, mensaje, ButtonType.OK);
        alert.setTitle(titulo);
        alert.show();
    }
}
