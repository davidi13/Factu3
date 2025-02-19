package com.facturacion.factu3.models;

import com.facturacion.factu3.database.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TrabajadorDAO {

    public static List<Trabajador> obtenerTrabajadores() {
        List<Trabajador> listaTrabajadores = new ArrayList<>();
        String sql = "SELECT id, nombre, direccion, telefono, email, puesto, salario, observaciones FROM trabajadores";

        try (Connection conn = DatabaseConnection.obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Trabajador trabajador = new Trabajador(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("direccion"),
                        rs.getString("telefono"),
                        rs.getString("email"),
                        rs.getString("puesto"),
                        rs.getDouble("salario"),
                        rs.getString("observaciones")
                );
                listaTrabajadores.add(trabajador);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listaTrabajadores;
    }


    public static int obtenerSiguienteId() {
        String sql = "SELECT MAX(id) + 1 AS siguienteId FROM trabajadores";
        try (Connection conn = DatabaseConnection.obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt("siguienteId");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 1; // Si no hay registros, empezar desde 1
    }


    public static boolean agregarTrabajador(Trabajador trabajador) {
        String sql = "INSERT INTO trabajadores (nombre, direccion, telefono, email, puesto, salario, observaciones) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, trabajador.getNombre());
            stmt.setString(2, trabajador.getDireccion());
            stmt.setString(3, trabajador.getTelefono());
            stmt.setString(4, trabajador.getEmail());
            stmt.setString(5, trabajador.getPuesto());
            stmt.setDouble(6, trabajador.getSalario());
            stmt.setString(7, trabajador.getObservaciones());

            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public static boolean actualizarTrabajador(Trabajador trabajador) {
        String sql = "UPDATE trabajadores SET nombre=?, direccion=?, telefono=?, email=?, puesto=?, salario=?, observaciones=? WHERE id=?";

        try (Connection conn = DatabaseConnection.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, trabajador.getNombre());
            stmt.setString(2, trabajador.getDireccion());
            stmt.setString(3, trabajador.getTelefono());
            stmt.setString(4, trabajador.getEmail());
            stmt.setString(5, trabajador.getPuesto());
            stmt.setDouble(6, trabajador.getSalario());
            stmt.setString(7, trabajador.getObservaciones());
            stmt.setInt(8, trabajador.getId());

            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean eliminarTrabajador(int id) {
        String sql = "DELETE FROM trabajadores WHERE id=?";

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
