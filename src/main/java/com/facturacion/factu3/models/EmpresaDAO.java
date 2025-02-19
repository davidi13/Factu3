package com.facturacion.factu3.models;

import com.facturacion.factu3.database.DatabaseConnection;
import java.sql.*;

public class EmpresaDAO {

    public static Empresa obtenerEmpresa() {
        String sql = "SELECT * FROM empresa LIMIT 1";
        try (Connection conn = DatabaseConnection.obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return new Empresa(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("direccion"),
                        rs.getString("telefono"),
                        rs.getString("email"),
                        rs.getString("cif"),
                        rs.getString("logo")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Si no hay empresa registrada
    }

    public static boolean actualizarEmpresa(Empresa empresa) {
        String sql = "UPDATE empresa SET nombre=?, direccion=?, telefono=?, email=?, cif=?, logo=? WHERE id=?";

        try (Connection conn = DatabaseConnection.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, empresa.getNombre());
            stmt.setString(2, empresa.getDireccion());
            stmt.setString(3, empresa.getTelefono());
            stmt.setString(4, empresa.getEmail());
            stmt.setString(5, empresa.getCif());
            stmt.setString(6, empresa.getLogo());
            stmt.setInt(7, empresa.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
