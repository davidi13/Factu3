package com.facturacion.factu3.models;

import com.facturacion.factu3.database.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmpresaDAO {

    private static Empresa construirEmpresa(ResultSet rs) throws SQLException {
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

    public static Empresa obtenerEmpresa() {
        String sql = "SELECT * FROM empresa LIMIT 1";
        try (Connection conn = DatabaseConnection.obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            return rs.next() ? construirEmpresa(rs) : null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Empresa> obtenerEmpresas() {
        List<Empresa> empresas = new ArrayList<>();
        String sql = "SELECT * FROM empresa";

        try (Connection conn = DatabaseConnection.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                empresas.add(construirEmpresa(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return empresas;
    }

    public static Empresa obtenerEmpresaPorId(int id) {
        String sql = "SELECT * FROM empresa WHERE id = ?"; // Corregido el error en la tabla
        try (Connection conn = DatabaseConnection.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? construirEmpresa(rs) : null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
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
            stmt.setString(6, empresa.getLogo() != null ? empresa.getLogo() : ""); // Evitar null
            stmt.setInt(7, empresa.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
