package com.facturacion.factu3.controllers;

import com.facturacion.factu3.models.Articulo;
import com.facturacion.factu3.models.ArticuloDAO;
import com.facturacion.factu3.models.Proveedor;
import com.facturacion.factu3.models.ProveedorDAO;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.List;

public class ArticulosController {

    @FXML private TextField txtCodigoArticulo, txtCodigoBarras, txtDescripcion, txtFamilia, txtCoste, txtMargen, txtPvp, txtStock;
    @FXML private TextArea txtObservaciones;
    @FXML private ComboBox<Proveedor> cmbProveedor; // ✅ ComboBox correctamente implementado
    @FXML private TableView<Articulo> tblArticulos;
    @FXML private TableColumn<Articulo, Integer> colId, colFamilia;
    @FXML private TableColumn<Articulo, String> colCodigoArticulo, colCodigoBarras, colDescripcion, colProveedor;
    @FXML private TableColumn<Articulo, Double> colCoste, colMargen, colPvp, colStock;
    @FXML private TableColumn<Articulo, String> colObservaciones;

    private ObservableList<Articulo> listaArticulos;
    private ObservableList<Proveedor> listaProveedores; // Lista de proveedores

    @FXML
    public void initialize() {
        System.out.println("➡ Inicializando ArticulosController...");

        // Generar el siguiente código de artículo automáticamente
        txtCodigoArticulo.setText(ArticuloDAO.obtenerSiguienteCodigoArticulo());
        txtCodigoArticulo.setDisable(true); // Evitar que el usuario lo modifique

        listaArticulos = FXCollections.observableArrayList(ArticuloDAO.obtenerArticulos());
        tblArticulos.setItems(listaArticulos);

        colId.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        colCodigoArticulo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCodigoArticulo()));
        colCodigoBarras.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCodigoBarras()));
        colDescripcion.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescripcion()));
        colFamilia.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getFamilia()).asObject());
        colCoste.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getCoste()).asObject());
        colMargen.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getMargenComercial()).asObject());
        colPvp.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPvp()).asObject());
        colStock.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getStock()).asObject());
        colObservaciones.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getObservaciones()));

        // ✅ Ahora la columna de proveedores mostrará el nombre en vez del ID
        colProveedor.setCellValueFactory(cellData -> {
            Proveedor proveedor = ProveedorDAO.obtenerProveedorPorId(cellData.getValue().getProveedor());
            return new SimpleStringProperty(proveedor != null ? proveedor.getNombre() : "Desconocido");
        });

        cargarProveedores(); // ✅ Cargar la lista de proveedores en el ComboBox

        tblArticulos.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Articulo articuloSeleccionado = tblArticulos.getSelectionModel().getSelectedItem();
                if (articuloSeleccionado != null) {
                    abrirVentanaEdicion(articuloSeleccionado);
                }
            }
        });
    }

    /**
     * ✅ Método para cargar proveedores en el ComboBox
     */
    private void cargarProveedores() {
        listaProveedores = FXCollections.observableArrayList(ProveedorDAO.obtenerProveedores());
        cmbProveedor.setItems(listaProveedores);
    }

    public void actualizarTablaArticulos() {
        listaArticulos.setAll(ArticuloDAO.obtenerArticulos());
    }

    @FXML
    private void guardarArticulo() {
        Proveedor proveedorSeleccionado = cmbProveedor.getValue();
        if (proveedorSeleccionado == null) {
            mostrarAlerta("Error", "Debe seleccionar un proveedor.", Alert.AlertType.ERROR);
            return;
        }

        try {
            Articulo nuevoArticulo = new Articulo(
                    0, // Se generará automáticamente en la BD
                    txtCodigoArticulo.getText(),
                    txtCodigoBarras.getText(),
                    txtDescripcion.getText(),
                    Integer.parseInt(txtFamilia.getText()),
                    Double.parseDouble(txtCoste.getText()),
                    Double.parseDouble(txtMargen.getText()),
                    Double.parseDouble(txtPvp.getText()),
                    proveedorSeleccionado.getId(), // ✅ Obtener ID del proveedor seleccionado
                    Double.parseDouble(txtStock.getText()),
                    txtObservaciones.getText()
            );

            if (ArticuloDAO.agregarArticulo(nuevoArticulo)) {
                actualizarTablaArticulos();
                limpiarCampos();
                mostrarAlerta("Guardado", "Artículo guardado correctamente.", Alert.AlertType.INFORMATION);
            } else {
                mostrarAlerta("Error", "No se pudo guardar el artículo.", Alert.AlertType.ERROR);
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "Revise los datos ingresados. Algunos valores no son válidos.", Alert.AlertType.ERROR);
        }
    }

    private void limpiarCampos() {
        txtCodigoArticulo.clear();
        txtCodigoBarras.clear();
        txtDescripcion.clear();
        txtFamilia.clear();
        txtCoste.clear();
        txtMargen.clear();
        txtPvp.clear();
        cmbProveedor.getSelectionModel().clearSelection(); // ✅ Limpiar selección
        txtStock.clear();
        txtObservaciones.clear();
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    private void abrirVentanaEdicion(Articulo articulo) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/EditarArticuloView.fxml"));
            Parent root = loader.load();
            EditarArticuloController controller = loader.getController();

            Stage stage = new Stage();
            controller.setArticulo(articulo, stage, this);

            stage.setScene(new Scene(root));
            stage.setTitle("Editar Artículo");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
