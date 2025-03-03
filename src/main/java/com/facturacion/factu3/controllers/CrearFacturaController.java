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
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
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

            // Configurar las columnas de la tabla con formateo en €
            colDescripcion.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescripcion()));

            colTotal.setCellValueFactory(cellData ->
                    new SimpleDoubleProperty(cellData.getValue().getTotal()).asObject());
            colTotal.setCellFactory(column -> new TextFieldTableCell<>(new StringConverter<Double>() {
                @Override
                public String toString(Double value) {
                    return value != null ? formatearEuros(value) : "";
                }

                @Override
                public Double fromString(String string) {
                    try {
                        return NumberFormat.getCurrencyInstance(Locale.FRANCE).parse(string).doubleValue();
                    } catch (Exception e) {
                        return 0.0;
                    }
                }
            }));

            colPrecio.setCellValueFactory(cellData ->
                    new SimpleDoubleProperty(cellData.getValue().getPrecioUnitario()).asObject());
            colPrecio.setCellFactory(column -> new TextFieldTableCell<>(new StringConverter<Double>() {
                @Override
                public String toString(Double value) {
                    return value != null ? formatearEuros(value) : "";
                }

                @Override
                public Double fromString(String string) {
                    try {
                        return NumberFormat.getCurrencyInstance(Locale.FRANCE).parse(string).doubleValue();
                    } catch (Exception e) {
                        return 0.0;
                    }
                }
            }));

            // Agregar valores predeterminados a los ComboBox
            cmbIVA.setItems(FXCollections.observableArrayList(4.0, 10.0, 21.0));
            cmbFormaPago.setItems(FXCollections.observableArrayList("Efectivo", "Tarjeta", "Transferencia"));

            // Configurar correctamente la conversión de objetos a String en los ComboBox
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

            LineaFactura nuevaLinea = new LineaFactura(0, articuloSeleccionado.getId(), articuloSeleccionado.getDescripcion(), cantidad, precioUnitario, iva, total);
            lineasFactura.add(nuevaLinea);
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
        double iva = calcularIVA(lineasFactura);
        double total = baseImponible + iva;

        lblBaseImponible.setText(String.format("%.2f", baseImponible));
        lblIVA.setText(String.format("%.2f", iva));
        lblTotal.setText(String.format("%.2f", total));
    }

    private double calcularBaseImponible(List<LineaFactura> lineasFactura) {
        return lineasFactura.stream().mapToDouble(l -> l.getCantidad() * l.getPrecioUnitario()).sum();
    }

    private double calcularIVA(List<LineaFactura> lineasFactura) {
        return lineasFactura.stream().mapToDouble(l -> l.getCantidad() * l.getPrecioUnitario() * (l.getIva() / 100)).sum();
    }

    @FXML
    private void generarPDF() {
        try {
            int numeroFactura = Integer.parseInt(txtNumeroFactura.getText());
            Cliente clienteSeleccionado = cmbCliente.getSelectionModel().getSelectedItem();

            if (clienteSeleccionado == null) {
                mostrarAlerta("Error", "Debe seleccionar un cliente antes de generar el PDF.", Alert.AlertType.ERROR);
                return;
            }

            Factura factura = new Factura(
                    numeroFactura,
                    new Date(),
                    clienteSeleccionado.getId(),
                    calcularBaseImponible(lineasFactura),
                    calcularIVA(lineasFactura),
                    calcularBaseImponible(lineasFactura) + calcularIVA(lineasFactura),
                    false, 1, null
            );
            factura.setLineasFactura(lineasFactura);

            // Ruta donde se guardará el PDF (en la carpeta Descargas del usuario)
            String userHome = System.getProperty("user.home");
            String rutaPDF = userHome + File.separator + "Downloads" + File.separator + "factura_" + numeroFactura + ".pdf";

            // Generar el PDF pasando la factura y el cliente
            GenerarFacturaPDF.generarFacturaPDF(factura, clienteSeleccionado);

            System.out.println("Factura generada en PDF correctamente: " + rutaPDF);
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "Número de factura no válido.", Alert.AlertType.ERROR);
            System.err.println("Error al generar el PDF: " + e.getMessage());
        }
    }


    @FXML
    private void guardarFactura() {
        try {
            int numeroFactura = Integer.parseInt(txtNumeroFactura.getText());
            Cliente clienteSeleccionado = cmbCliente.getSelectionModel().getSelectedItem();
            int idCliente = clienteSeleccionado != null ? clienteSeleccionado.getId() : 0;
            int formaPago = cmbFormaPago.getSelectionModel().getSelectedIndex() + 1;

            // 🟢 🔄 Obtener el IVA desde el ComboBox
            double ivaSeleccionado = cmbIVA.getSelectionModel().getSelectedItem();

            Factura factura = new Factura(numeroFactura, new Date(), idCliente,
                    calcularBaseImponible(lineasFactura),
                    ivaSeleccionado, // 🟢 Se pasa el porcentaje, no el monto calculado
                    calcularBaseImponible(lineasFactura) * (1 + ivaSeleccionado / 100),
                    false, formaPago, null);
            factura.setLineasFactura(lineasFactura);

            facturaDAO.guardarFactura(factura);
            System.out.println("Factura guardada con éxito.");
        } catch (SQLException | NumberFormatException e) {
            System.err.println("Error al guardar la factura: " + e.getMessage());
        }
    }

}
