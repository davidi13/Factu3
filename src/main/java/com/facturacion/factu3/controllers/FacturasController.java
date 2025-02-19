package com.facturacion.factu3.controllers;

import com.facturacion.factu3.models.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.util.Date;

public class FacturasController {

    @FXML private ComboBox<Empresa> cmbEmpresa;
    @FXML private ComboBox<Cliente> cmbCliente;
    @FXML private ComboBox<Proveedor> cmbProveedor;
    @FXML private DatePicker dpFecha;
    @FXML private TextField txtNumeroFactura, txtTotal, txtIva;
    @FXML private Button btnGuardar;
    @FXML private TableView<Factura> tblFacturas;

    private ObservableList<Factura> listaFacturas;

    @FXML
    public void initialize() {
        cmbEmpresa.setItems(FXCollections.observableArrayList(EmpresaDAO.obtenerEmpresas()));
        cmbCliente.setItems(FXCollections.observableArrayList(ClienteDAO.obtenerClientes()));
        cmbProveedor.setItems(FXCollections.observableArrayList(ProveedorDAO.obtenerProveedores()));

        listaFacturas = FXCollections.observableArrayList(FacturaDAO.obtenerFacturas());
        tblFacturas.setItems(listaFacturas);
    }

    @FXML
    private void guardarFactura() {
        Factura nuevaFactura = new Factura(
                0,
                txtNumeroFactura.getText(),
                java.sql.Date.valueOf(dpFecha.getValue()),
                cmbEmpresa.getValue(),
                cmbCliente.getValue(),
                cmbProveedor.getValue(),
                null,
                Double.parseDouble(txtTotal.getText()),
                Double.parseDouble(txtIva.getText())
        );

        if (FacturaDAO.agregarFactura(nuevaFactura)) {
            listaFacturas.setAll(FacturaDAO.obtenerFacturas());
            limpiarCampos();
        }
    }

    private void limpiarCampos() {
        txtNumeroFactura.clear();
        dpFecha.setValue(null);
        cmbEmpresa.getSelectionModel().clearSelection();
        cmbCliente.getSelectionModel().clearSelection();
        cmbProveedor.getSelectionModel().clearSelection();
        txtTotal.clear();
        txtIva.clear();
    }
}
