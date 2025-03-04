package com.facturacion.factu3.controllers;

import com.facturacion.factu3.models.Rectificativa;
import com.facturacion.factu3.models.RectificativaDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.List;

public class VerRectificativasController {

    @FXML private TableView<Rectificativa> tablaRectificativas;

    @FXML private TableColumn<Rectificativa, Integer> colNumeroFactura;
    @FXML private TableColumn<Rectificativa, String> colFecha;
    @FXML private TableColumn<Rectificativa, Integer> colIdCliente;
    @FXML private TableColumn<Rectificativa, Double> colBaseImponible;
    @FXML private TableColumn<Rectificativa, Double> colIVA;
    @FXML private TableColumn<Rectificativa, Double> colTotal;
    @FXML private TableColumn<Rectificativa, String> colObservaciones;

    private ObservableList<Rectificativa> rectificativasList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        configurarColumnasTabla();
        cargarRectificativas();
    }

    private void configurarColumnasTabla() {
        colNumeroFactura.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getNumeroFactura()).asObject());

        colFecha.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getFecha().toString()));

        colIdCliente.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getIdCliente()).asObject());

        colBaseImponible.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleDoubleProperty(cellData.getValue().getBaseImponible()).asObject());

        colIVA.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleDoubleProperty(cellData.getValue().getIva()).asObject());

        colTotal.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleDoubleProperty(cellData.getValue().getTotal()).asObject());

        colObservaciones.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getObservaciones()));
    }

    private void cargarRectificativas() {
        List<Rectificativa> rectificativas = RectificativaDAO.obtenerTodasLasRectificativas();
        rectificativasList.setAll(rectificativas);
        tablaRectificativas.setItems(rectificativasList);
    }
}
