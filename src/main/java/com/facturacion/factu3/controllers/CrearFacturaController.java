package com.facturacion.factu3.controllers;

import com.facturacion.factu3.database.DatabaseConnection;
import com.facturacion.factu3.models.Factura;
import com.facturacion.factu3.models.LineaFactura;
import com.facturacion.factu3.models.Articulo;
import com.facturacion.factu3.models.Cliente;
import com.facturacion.factu3.utils.GenerarFacturaPDF;
import com.facturacion.factu3.database.CrearFacturaDAO;
import com.facturacion.factu3.models.ClienteDAO;
import com.facturacion.factu3.models.ArticuloDAO;
import com.facturacion.factu3.utils.ImprimirFactura;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CrearFacturaController {
    private CrearFacturaDAO facturaDAO;
    private ObservableList<LineaFactura> lineasFactura;
    private ObservableList<Cliente> clientes;
    private ObservableList<Articulo> articulos;

    @FXML
    private TextField txtNumeroFactura;
    @FXML
    private Button btnImprimirFactura; // Declarar el botón

    @FXML private ComboBox<Cliente> cmbCliente;
    @FXML private ComboBox<String> cmbFormaPago;
    @FXML private ComboBox<Articulo> cmbArticulos;
    @FXML private TextField txtCantidad;
    @FXML private ComboBox<Double> cmbIVA;
    @FXML private TableView<LineaFactura> tablaLineasFactura;
    @FXML private TableColumn<LineaFactura, String> colDescripcion;
    @FXML private TableColumn<LineaFactura, Double> colPrecio, colTotal;
    @FXML private Label lblBaseImponible;
    @FXML private Label lblIVA;
    @FXML private Label lblTotal;
    @FXML private Button btnGuardarFactura;
    @FXML private Button btnGenerarPDF;
    @FXML private Button btnCancelar;
    @FXML private DatePicker dpFechaCobro;
    @FXML private TextArea txtObservaciones;
    @FXML private TableColumn<LineaFactura, Double> colCantidad;
    @FXML private TableColumn<LineaFactura, Double> colIVA;
    @FXML private TableColumn<LineaFactura, String> colFormaPago;
    @FXML private TableColumn<LineaFactura, String> colFechaCobro;
    @FXML private TableColumn<LineaFactura, String> colObservaciones;
    @FXML
    private Label lblDescuento;



    public CrearFacturaController() {
        this.lineasFactura = FXCollections.observableArrayList();
        this.clientes = FXCollections.observableArrayList();
        this.articulos = FXCollections.observableArrayList();
    }

    public void setDatabaseConnection(Connection connection) {
        this.facturaDAO = new CrearFacturaDAO(connection);
        cargarClientes();
        cargarArticulos();
        generarNumeroFactura();
    }

    @FXML
    public void initialize() {
        Platform.runLater(() -> {
            Connection connection = DatabaseConnection.obtenerConexion(); // Usa tu clase de conexión
            if (connection == null) {
                mostrarAlerta("Error", "No se pudo establecer la conexión con la base de datos.", Alert.AlertType.ERROR);
                return;
            }

            cmbCliente.setOnAction(event -> actualizarDescuento());


            // Inicializar DAO con la conexión obtenida
            facturaDAO = new CrearFacturaDAO(connection);

            // Generar y mostrar el número de factura
            int numeroFactura = facturaDAO.obtenerUltimoNumeroFactura() + 1;
            txtNumeroFactura.setText(String.valueOf(numeroFactura));
            txtNumeroFactura.setDisable(true);

            // Cargar datos en los ComboBox
            cargarClientes();
            cargarArticulos();

            // Inicializar la tabla de líneas de factura
            lineasFactura = FXCollections.observableArrayList();
            tablaLineasFactura.setItems(lineasFactura);

            // Configurar las columnas de la tabla
            colCantidad.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getCantidad()));
            colDescripcion.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getDescripcion()));
            colPrecio.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getPrecioUnitario()));
            colIVA.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getIva()));
            colTotal.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getTotal()));

            // Las siguientes columnas toman el valor directamente de los inputs del usuario
            colFormaPago.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cmbFormaPago.getSelectionModel().getSelectedItem()));
            colFechaCobro.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(dpFechaCobro.getValue() != null ? dpFechaCobro.getValue().toString() : "No cobrado"));
            colObservaciones.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(txtObservaciones.getText().trim().isEmpty() ? "Sin observaciones" : txtObservaciones.getText()));

            // Configurar valores predeterminados en ComboBox
            cmbIVA.setItems(FXCollections.observableArrayList(4.0, 10.0, 21.0));
            cmbFormaPago.setItems(FXCollections.observableArrayList("Efectivo", "Tarjeta", "Transferencia"));

            // Configurar conversores de texto en los ComboBox
            cmbCliente.setConverter(new StringConverter<Cliente>() {
                @Override
                public String toString(Cliente cliente) {
                    return cliente != null ? cliente.getNombreCliente() + " - " + cliente.getId() : "";
                }

                @Override
                public Cliente fromString(String string) {
                    return null;
                }
            });

            cmbArticulos.setConverter(new StringConverter<Articulo>() {
                @Override
                public String toString(Articulo articulo) {
                    return articulo != null ? articulo.getDescripcion() + " - " + articulo.getId() : "";
                }

                @Override
                public Articulo fromString(String string) {
                    return null;
                }
            });
        });
    }


    // Método para formatear valores en euros
    private String formatearEuros(double cantidad) {
        NumberFormat formatoEuro = NumberFormat.getCurrencyInstance(Locale.FRANCE); // Usa el formato europeo (€)
        return formatoEuro.format(cantidad);
    }



    private void cargarClientes() {
        List<Cliente> listaClientes = ClienteDAO.obtenerClientes();

        if (listaClientes == null || listaClientes.isEmpty()) {
            System.err.println("No se encontraron clientes en la base de datos.");
            return;
        }

        Platform.runLater(() -> {
            cmbCliente.setItems(FXCollections.observableArrayList(listaClientes));
            cmbCliente.setConverter(new StringConverter<Cliente>() {
                @Override
                public String toString(Cliente cliente) {
                    return cliente != null ? cliente.getNombreCliente() + " - " + cliente.getId() : "";
                }

                @Override
                public Cliente fromString(String string) {
                    return null;
                }
            });
            System.out.println("✅ Clientes cargados correctamente.");
        });
    }


    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }


    private void cargarArticulos() {
        List<Articulo> listaArticulos = ArticuloDAO.obtenerArticulos();

        if (listaArticulos == null || listaArticulos.isEmpty()) {
            System.err.println("No se encontraron artículos en la base de datos.");
            return;
        }

        Platform.runLater(() -> {
            cmbArticulos.setItems(FXCollections.observableArrayList(listaArticulos));
            cmbArticulos.setConverter(new StringConverter<Articulo>() {
                @Override
                public String toString(Articulo articulo) {
                    return articulo != null ? articulo.getDescripcion() + " - " + articulo.getId() : "";
                }

                @Override
                public Articulo fromString(String string) {
                    return null;
                }
            });
            System.out.println("✅ Artículos cargados correctamente.");
        });
    }


    private void generarNumeroFactura() {
        int numeroFactura = facturaDAO.obtenerUltimoNumeroFactura() + 1;
        txtNumeroFactura.setText(String.valueOf(numeroFactura));
        txtNumeroFactura.setEditable(false);
    }

    @FXML
    private void agregarLinea() {
        Articulo articuloSeleccionado = cmbArticulos.getSelectionModel().getSelectedItem();
        if (articuloSeleccionado == null || txtCantidad.getText().trim().isEmpty() || cmbIVA.getSelectionModel().isEmpty()) {
            mostrarAlerta("Error", "Seleccione un artículo, ingrese una cantidad válida y elija un IVA.", Alert.AlertType.ERROR);
            return;
        }

        try {
            double cantidad = Double.parseDouble(txtCantidad.getText().trim());
            double iva = cmbIVA.getSelectionModel().getSelectedItem();
            double precioUnitario = articuloSeleccionado.getPvp();
            double total = cantidad * precioUnitario * (1 + iva / 100);

            String formaPago = cmbFormaPago.getSelectionModel().getSelectedItem();
            String fechaCobro = dpFechaCobro.getValue() != null ? dpFechaCobro.getValue().toString() : "No cobrado";
            String observaciones = txtObservaciones.getText().trim().isEmpty() ? "Sin observaciones" : txtObservaciones.getText();

            LineaFactura nuevaLinea = new LineaFactura(0, articuloSeleccionado.getId(), articuloSeleccionado.getDescripcion(), cantidad, precioUnitario, iva, total);
            lineasFactura.add(nuevaLinea);

            // Actualizar tabla
            tablaLineasFactura.refresh();
            actualizarTotales();
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "La cantidad debe ser un número válido.", Alert.AlertType.ERROR);
        }
    }




    @FXML
    private void eliminarLinea() {
        LineaFactura seleccionada = tablaLineasFactura.getSelectionModel().getSelectedItem();
        if (seleccionada != null) {
            lineasFactura.remove(seleccionada);
            actualizarTotales();
        }
    }

    @FXML
    private void cancelar() {
        txtNumeroFactura.clear();
        cmbCliente.getSelectionModel().clearSelection();
        cmbFormaPago.getSelectionModel().clearSelection();
        cmbArticulos.getSelectionModel().clearSelection();
        txtCantidad.clear();
        cmbIVA.getSelectionModel().clearSelection();
        lineasFactura.clear();
        actualizarTotales();
        System.out.println("Formulario de creación de factura restablecido.");
    }

    private void actualizarTotales() {
        double baseImponible = calcularBaseImponible(lineasFactura);
        double descuentoCliente;

        // Obtener el descuento del cliente seleccionado
        Cliente clienteSeleccionado = cmbCliente.getSelectionModel().getSelectedItem();
        if (clienteSeleccionado != null) {
            descuentoCliente = clienteSeleccionado.getDescuentoCliente(); // Descuento en porcentaje
        } else {
            descuentoCliente = 0.0;
        }

        // Aplicar el descuento a la base imponible
        double montoDescuento = (baseImponible * descuentoCliente) / 100;
        double baseImponibleConDescuento = baseImponible - montoDescuento;

        // Calcular el IVA sobre la base imponible con el descuento aplicado
        double iva = lineasFactura.stream()
                .mapToDouble(l -> (l.getCantidad() * l.getPrecioUnitario() - (l.getCantidad() * l.getPrecioUnitario() * descuentoCliente / 100)) * (l.getIva() / 100))
                .sum();

        // Calcular el total
        double total = baseImponibleConDescuento + iva;

        // Mostrar los valores en la interfaz
        lblBaseImponible.setText(String.format("%.2f €", baseImponibleConDescuento));
        lblDescuento.setText(String.format("-%.2f €", montoDescuento));
        lblIVA.setText(String.format("%.2f €", iva));
        lblTotal.setText(String.format("%.2f €", total));
    }



    @FXML
    private void actualizarDescuento() {
        Cliente clienteSeleccionado = cmbCliente.getSelectionModel().getSelectedItem();
        if (clienteSeleccionado != null) {
            double descuento = clienteSeleccionado.getDescuentoCliente(); // Suponiendo que el modelo Cliente tiene getDescuento()
            lblDescuento.setText(String.format("%.2f €", descuento)); // Formatear a euros
        } else {
            lblDescuento.setText("0.00 €"); // Valor por defecto si no hay cliente seleccionado
        }
    }




    private double calcularBaseImponible(List<LineaFactura> lineasFactura) {
        Cliente clienteSeleccionado = cmbCliente.getSelectionModel().getSelectedItem();
        double descuento = clienteSeleccionado != null ? clienteSeleccionado.getDescuentoCliente() : 0.0;

        double baseImponible = lineasFactura.stream()
                .mapToDouble(l -> l.getCantidad() * l.getPrecioUnitario())
                .sum();

        // Aplicar el descuento antes de calcular el IVA
        return baseImponible - (baseImponible * (descuento / 100));
    }


    private double calcularIVA(List<LineaFactura> lineasFactura) {
        double baseImponibleConDescuento = calcularBaseImponible(lineasFactura);
        return lineasFactura.stream()
                .mapToDouble(l -> baseImponibleConDescuento * (l.getIva() / 100))
                .sum();
    }


    @FXML
    private void generarPDF() {
        try {
            int numeroFactura = Integer.parseInt(txtNumeroFactura.getText());
            Cliente clienteSeleccionado = cmbCliente.getSelectionModel().getSelectedItem();
            int idCliente = clienteSeleccionado != null ? clienteSeleccionado.getId() : 0;

            // Obtener datos adicionales
            String formaPago = cmbFormaPago.getSelectionModel().getSelectedItem();
            String fechaCobro = dpFechaCobro.getValue() != null ? dpFechaCobro.getValue().toString() : "No Cobrado";
            String observaciones = txtObservaciones.getText();

            // Crear factura con datos básicos
            Factura factura = new Factura(
                    numeroFactura, new Date(), idCliente,
                    calcularBaseImponible(lineasFactura),
                    calcularIVA(lineasFactura),
                    calcularBaseImponible(lineasFactura) + calcularIVA(lineasFactura),
                    false, 1, null
            );
            factura.setLineasFactura(lineasFactura);

            // Obtener la ruta de descargas
            String userHome = System.getProperty("user.home");
            String rutaPDF = userHome + "/Downloads/factura_" + numeroFactura + ".pdf";

            // Llamar a la función con todos los parámetros necesarios
            GenerarFacturaPDF.generarFacturaPDF(factura, clienteSeleccionado, formaPago, fechaCobro, observaciones);

            System.out.println("Factura generada en PDF correctamente: " + rutaPDF);
        } catch (NumberFormatException e) {
            System.err.println("Error al generar el PDF: " + e.getMessage());
        }
    }
    @FXML
    private void imprimirFactura() {
        try {
            Stage stage = (Stage) btnGuardarFactura.getScene().getWindow(); // Usa otro botón de referencia
            ImprimirFactura.imprimirPDF(stage);
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo imprimir la factura.", Alert.AlertType.ERROR);
            System.err.println("Error al imprimir la factura: " + e.getMessage());
        }
    }



    @FXML
    private void guardarFactura() {
        try {
            int numeroFactura = Integer.parseInt(txtNumeroFactura.getText());
            Cliente clienteSeleccionado = cmbCliente.getSelectionModel().getSelectedItem();
            int idCliente = clienteSeleccionado != null ? clienteSeleccionado.getId() : 0;
            int formaPago = cmbFormaPago.getSelectionModel().getSelectedIndex() + 1;

            double ivaSeleccionado = cmbIVA.getSelectionModel().getSelectedItem();

            // Obtener la fecha de cobro (si se seleccionó)
            java.sql.Date fechaCobro = dpFechaCobro.getValue() != null ?
                    java.sql.Date.valueOf(dpFechaCobro.getValue()) : null;

            // Obtener observaciones directamente desde el campo de texto
            String observaciones = txtObservaciones.getText().trim().isEmpty() ? null : txtObservaciones.getText();

            Factura factura = new Factura(numeroFactura, new Date(), idCliente,
                    calcularBaseImponible(lineasFactura), // Base imponible con descuento aplicado
                    ivaSeleccionado,
                    calcularBaseImponible(lineasFactura) + calcularIVA(lineasFactura), // Total con IVA
                    false, formaPago, fechaCobro);
            factura.setLineasFactura(lineasFactura);

            // Pasamos el valor de observaciones directamente al DAO
            facturaDAO.guardarFactura(factura, observaciones);
            System.out.println("Factura guardada con éxito.");
        } catch (SQLException | NumberFormatException e) {
            System.err.println("Error al guardar la factura: " + e.getMessage());
        }
    }


}
