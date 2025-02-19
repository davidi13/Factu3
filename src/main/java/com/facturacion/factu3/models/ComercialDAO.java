package com.facturacion.factu3.models;

import com.facturacion.factu3.database.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ComercialDAO {

    public static List<Comercial> obtenerComerciales() {
        List<Comercial> listaComerciales = new ArrayList<>();
        String sql = "SELECT * FROM comerciales";

        try (Connection conn = DatabaseConnection.obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Comercial comercial = new Comercial(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("direccion"),
                        rs.getString("telefono"),
                        rs.getString("email"),
                        rs.getDouble("comision"),
                        rs.getString("observaciones")
                );
                listaComerciales.add(comercial);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listaComerciales;
    }

    public static int obtenerSiguienteId() {
        String sql = "SELECT MAX(id) + 1 AS siguienteId FROM comerciales";
        try (Connection conn = DatabaseConnection.obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt("siguienteId");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 1;
    }

    public static boolean agregarComercial(Comercial comercial) {
        String sql = "INSERT INTO comerciales (nombre, direccion, telefono, email, comision, observaciones) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, comercial.getNombre());
            stmt.setString(2, comercial.getDireccion());
            stmt.setString(3, comercial.getTelefono());
            stmt.setString(4, comercial.getEmail());
            stmt.setDouble(5, comercial.getComision());
            stmt.setString(6, comercial.getObservaciones());

            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean actualizarComercial(Comercial comercial) {
        String sql = "UPDATE comerciales SET nombre=?, direccion=?, telefono=?, email=?, comision=?, observaciones=? WHERE id=?";

        try (Connection conn = DatabaseConnection.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, comercial.getNombre());
            stmt.setString(2, comercial.getDireccion());
            stmt.setString(3, comercial.getTelefono());
            stmt.setString(4, comercial.getEmail());
            stmt.setDouble(5, comercial.getComision());
            stmt.setString(6, comercial.getObservaciones());
            stmt.setInt(7, comercial.getId());

            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean eliminarComercial(int id) {
        String sql = "DELETE FROM comerciales WHERE id=?";

        try (Connection conn = DatabaseConnection.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
