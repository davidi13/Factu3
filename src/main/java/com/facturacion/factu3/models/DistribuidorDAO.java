package com.facturacion.factu3.models;

import com.facturacion.factu3.database.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DistribuidorDAO {

    public static List<Distribuidor> obtenerDistribuidores() {
        List<Distribuidor> listaDistribuidores = new ArrayList<>();
        String sql = "SELECT id, nombre, direccion, telefono, email, zona, observaciones FROM distribuidores";

        try (Connection conn = DatabaseConnection.obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Distribuidor distribuidor = new Distribuidor(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("direccion"),
                        rs.getString("telefono"),
                        rs.getString("email"),
                        rs.getString("zona"),
                        rs.getString("observaciones")
                );
                listaDistribuidores.add(distribuidor);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaDistribuidores;
    }

    public static int obtenerSiguienteId() {
        String sql = "SELECT MAX(id) + 1 AS siguienteId FROM distribuidores";
        try (Connection conn = DatabaseConnection.obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt("siguienteId");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 1; // Si no hay registros, empieza desde 1
    }

    public static boolean agregarDistribuidor(Distribuidor distribuidor) {
        String sql = "INSERT INTO distribuidores (nombre, direccion, telefono, email, zona, observaciones) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, distribuidor.getNombre());
            stmt.setString(2, distribuidor.getDireccion());
            stmt.setString(3, distribuidor.getTelefono());
            stmt.setString(4, distribuidor.getEmail());
            stmt.setString(5, distribuidor.getZona());
            stmt.setString(6, distribuidor.getObservaciones());

            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean actualizarDistribuidor(Distribuidor distribuidor) {
        String sql = "UPDATE distribuidores SET nombre=?, direccion=?, telefono=?, email=?, zona=?, observaciones=? WHERE id=?";

        try (Connection conn = DatabaseConnection.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, distribuidor.getNombre());
            stmt.setString(2, distribuidor.getDireccion());
            stmt.setString(3, distribuidor.getTelefono());
            stmt.setString(4, distribuidor.getEmail());
            stmt.setString(5, distribuidor.getZona());
            stmt.setString(6, distribuidor.getObservaciones());
            stmt.setInt(7, distribuidor.getId());

            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean eliminarDistribuidor(int id) {
        String sql = "DELETE FROM distribuidores WHERE id=?";

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
