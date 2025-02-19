package com.facturacion.factu3.models;

import com.facturacion.factu3.database.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FacturaDAO {

    public static List<Factura> obtenerFacturas() {
        List<Factura> listaFacturas = new ArrayList<>();
        String sql = "SELECT * FROM facturas";

        try (Connection conn = DatabaseConnection.obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Factura factura = new Factura(
                        rs.getInt("id"),
                        rs.getString("numeroFactura"),
                        rs.getDate("fecha"),
                        EmpresaDAO.obtenerEmpresaPorId(rs.getInt("empresaId")),
                        ClienteDAO.obtenerClientePorId(rs.getInt("clienteId")),
                        ProveedorDAO.obtenerProveedorPorId(rs.getInt("proveedorId")),
                        LineaFacturaDAO.obtenerLineasPorFactura(rs.getInt("id")),
                        rs.getDouble("total"),
                        rs.getDouble("iva")
                );
                listaFacturas.add(factura);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listaFacturas;
    }

    public static boolean agregarFactura(Factura factura) {
        String sql = "INSERT INTO facturas (numeroFactura, fecha, empresaId, clienteId, proveedorId, total, iva) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, factura.getNumeroFactura());
            stmt.setDate(2, new java.sql.Date(factura.getFecha().getTime()));
            stmt.setInt(3, factura.getEmpresa().getId());
            stmt.setInt(4, factura.getCliente().getId());
            stmt.setInt(5, factura.getProveedor().getId());
            stmt.setDouble(6, factura.getTotal());
            stmt.setDouble(7, factura.getIva());

            stmt.executeUpdate();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
