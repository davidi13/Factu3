package com.facturacion.factu3.database;

import com.facturacion.factu3.models.Factura;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CrearFacturaDAO {
    private static Connection connection;

    public CrearFacturaDAO(Connection connection) {
        this.connection = connection;
    }

    public void guardarFactura(Factura factura, String observaciones) throws SQLException {
        String sql = "INSERT INTO facturasClientes (numeroFacturaCliente, fechaFacturaCliente, idClienteFactura, baseImponibleFacturaCliente, ivaFacturaCliente, totalFacturaCliente, cobradaFactura, formaCobroFactura, fechaCobroFactura, hashFacturaCiente, qrFacturaCliente, observacionesFacturaClientes) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, factura.getNumeroFactura());
        statement.setDate(2, new java.sql.Date(factura.getFecha().getTime()));
        statement.setInt(3, factura.getIdCliente());
        statement.setDouble(4, factura.getBaseImponible());
        statement.setDouble(5, factura.getIva()); // Guardamos el porcentaje de IVA
        statement.setDouble(6, factura.getTotal());
        statement.setBoolean(7, factura.isCobrada());
        statement.setInt(8, factura.getFormaPago());
        statement.setDate(9, factura.getFechaCobro());

        // Generar hash aleatorio como identificador único de factura
        String hashFactura = "HASH_" + factura.getNumeroFactura();
        statement.setString(10, hashFactura);

        // Generar QR simulado como dato
        String qrFactura = "QR_" + factura.getNumeroFactura();
        statement.setString(11, qrFactura);

        // Guardar observaciones (puede ser NULL)
        statement.setString(12, observaciones);

        statement.executeUpdate();
        statement.close();
    }




    public int obtenerUltimoNumeroFactura() {
        int numeroFactura = 1000; // Valor inicial por defecto
        String sql = "SELECT MAX(numeroFacturaCliente) FROM facturasClientes";

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                numeroFactura = resultSet.getInt(1) + 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error obteniendo el último número de factura.");
        }
        return numeroFactura;
    }
}
