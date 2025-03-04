package com.facturacion.factu3.models;

import com.facturacion.factu3.database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class FacturaDAO {
    private static Connection connection = DatabaseConnection.obtenerConexion(); // Obtener conexión directamente

    public static List<Factura> obtenerTodasLasFacturas() {
        List<Factura> facturas = new ArrayList<>();
        String sql = "SELECT f.numeroFacturaCliente, f.fechaFacturaCliente, f.idClienteFactura, " +
                "c.id AS clienteID, c.nombreCliente, " +
                "f.baseImponibleFacturaCliente, f.ivaFacturaCliente, f.totalFacturaCliente, f.formaCobroFactura, f.fechaCobroFactura " +
                "FROM facturasClientes f " +
                "JOIN clientes c ON f.idClienteFactura = c.id";  // Usamos 'id' en lugar de 'idCliente'


        if (connection == null) {
            System.err.println("❌ Error: La conexión a la base de datos es nula.");
            return facturas;
        }

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Factura factura = new Factura();
                factura.setNumeroFactura(resultSet.getInt("numeroFacturaCliente"));
                factura.setFecha(resultSet.getDate("fechaFacturaCliente"));
                factura.setIdCliente(resultSet.getInt("idClienteFactura"));
                factura.setNombreCliente(resultSet.getString("nombreCliente"));  // Nuevo campo
                factura.setBaseImponible(resultSet.getDouble("baseImponibleFacturaCliente"));
                factura.setIva(resultSet.getDouble("ivaFacturaCliente"));
                factura.setTotal(resultSet.getDouble("totalFacturaCliente"));
                factura.setFormaPago(resultSet.getInt("formaCobroFactura"));
                factura.setFechaCobro(resultSet.getDate("fechaCobroFactura"));

                facturas.add(factura);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("❌ Error obteniendo las facturas.");
        }
        return facturas;
    }

}
