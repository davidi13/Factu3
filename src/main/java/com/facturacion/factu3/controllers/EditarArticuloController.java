package com.facturacion.factu3.controllers;

import com.facturacion.factu3.models.Articulo;
import com.facturacion.factu3.models.ArticuloDAO;
import com.facturacion.factu3.models.Proveedor;
import com.facturacion.factu3.models.ProveedorDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.List;

public class EditarArticuloController {

    @FXML private TextField txtCodigoArticulo, txtCodigoBarras, txtDescripcion, txtFamilia, txtCoste, txtMargen, txtPvp, txtStock;
    @FXML private TextArea txtObservaciones;
    @FXML private ComboBox<Proveedor> cmbProveedor; // 🔄 Cambio a ComboBox
    @FXML private Button btnGuardar, btnEliminar;

    private Articulo articuloSeleccionado;
    private Stage ventana;
    private ArticulosController articulosController;
    private ObservableList<Proveedor> listaProveedores;

    public void setArticulo(Articulo articulo, Stage stage, ArticulosController controller) {
        this.articuloSeleccionado = articulo;
        this.ventana = stage;
        this.articulosController = controller;

        txtCodigoArticulo.setText(articulo.getCodigoArticulo());
        txtCodigoBarras.setText(articulo.getCodigoBarras());
        txtDescripcion.setText(articulo.getDescripcion());
        txtFamilia.setText(String.valueOf(articulo.getFamilia()));
        txtCoste.setText(String.valueOf(articulo.getCoste()));
        txtMargen.setText(String.valueOf(articulo.getMargenComercial()));
        txtPvp.setText(String.valueOf(articulo.getPvp()));
        txtStock.setText(String.valueOf(articulo.getStock()));
        txtObservaciones.setText(articulo.getObservaciones());

        cargarProveedores(); // ✅ Cargar proveedores en el ComboBox

        // Seleccionar el proveedor actual del artículo
        for (Proveedor p : listaProveedores) {
            if (p.getId() == articulo.getProveedor()) {
                cmbProveedor.setValue(p);
                break;
            }
        }
    }

    /**
     * ✅ Método para cargar proveedores en el ComboBox
     */
    private void cargarProveedores() {
        List<Proveedor> proveedores = ProveedorDAO.obtenerProveedores();
        listaProveedores = FXCollections.observableArrayList(proveedores);
        cmbProveedor.setItems(listaProveedores);
    }

    @FXML
    private void guardarCambios() {
        Proveedor proveedorSeleccionado = cmbProveedor.getValue();
        if (proveedorSeleccionado == null) {
            mostrarAlerta("Error", "Debe seleccionar un proveedor.", Alert.AlertType.ERROR);
            return;
        }

        articuloSeleccionado.setCodigoArticulo(txtCodigoArticulo.getText());
        articuloSeleccionado.setCodigoBarras(txtCodigoBarras.getText());
        articuloSeleccionado.setDescripcion(txtDescripcion.getText());
        articuloSeleccionado.setFamilia(Integer.parseInt(txtFamilia.getText()));
        articuloSeleccionado.setCoste(Double.parseDouble(txtCoste.getText()));
        articuloSeleccionado.setMargenComercial(Double.parseDouble(txtMargen.getText()));
        articuloSeleccionado.setPvp(Double.parseDouble(txtPvp.getText()));
        articuloSeleccionado.setProveedor(proveedorSeleccionado.getId()); // ✅ Guardar el ID del proveedor seleccionado
        articuloSeleccionado.setStock(Double.parseDouble(txtStock.getText()));
        articuloSeleccionado.setObservaciones(txtObservaciones.getText());

        if (ArticuloDAO.actualizarArticulo(articuloSeleccionado)) {
            mostrarAlerta("Éxito", "Artículo actualizado correctamente.", Alert.AlertType.INFORMATION);
            articulosController.actualizarTablaArticulos(); // 🔄 Recargar lista
            ventana.close();
        } else {
            mostrarAlerta("Error", "No se pudo actualizar el artículo.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void eliminarArticulo() {
        Alert confirmacion1 = new Alert(Alert.AlertType.CONFIRMATION,
                "¿Seguro que quieres eliminar este artículo?", ButtonType.YES, ButtonType.NO);
        confirmacion1.setTitle("Confirmar eliminación");
        confirmacion1.setHeaderText(null);
        confirmacion1.showAndWait();

        if (confirmacion1.getResult() == ButtonType.YES) {
            Alert confirmacion2 = new Alert(Alert.AlertType.CONFIRMATION,
                    "⚠ Esta acción es irreversible. ¿Deseas continuar?", ButtonType.YES, ButtonType.NO);
            confirmacion2.setTitle("Última advertencia");
            confirmacion2.setHeaderText(null);
            confirmacion2.showAndWait();

            if (confirmacion2.getResult() == ButtonType.YES) {
                if (ArticuloDAO.eliminarArticulo(articuloSeleccionado.getId())) {
                    mostrarAlerta("Eliminado", "Artículo eliminado correctamente.", Alert.AlertType.INFORMATION);
                    articulosController.actualizarTablaArticulos(); // 🔄 Recargar lista
                    ventana.close();
                } else {
                    mostrarAlerta("Error", "No se pudo eliminar el artículo.", Alert.AlertType.ERROR);
                }
            }
        }
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
