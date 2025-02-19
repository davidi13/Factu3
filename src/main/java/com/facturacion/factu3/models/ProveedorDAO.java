package com.facturacion.factu3.models;

import com.facturacion.factu3.database.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProveedorDAO {

    public static List<Proveedor> obtenerProveedores() {
        List<Proveedor> listaProveedores = new ArrayList<>();
        String sql = "SELECT id, nombre, direccion, telefono, email, cif, observaciones FROM proveedores";

        try (Connection conn = DatabaseConnection.obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Proveedor proveedor = new Proveedor(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("direccion"),
                        rs.getString("telefono"),
                        rs.getString("email"),
                        rs.getString("cif"),
                        rs.getString("observaciones")
                );
                listaProveedores.add(proveedor);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listaProveedores;
    }

    /**
     * ✅ Método para obtener el siguiente ID disponible en la tabla proveedores
     */
    public static int obtenerSiguienteId() {
        String sql = "SELECT MAX(id) + 1 AS siguienteId FROM proveedores";

        try (Connection conn = DatabaseConnection.obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                int siguienteId = rs.getInt("siguienteId");
                return (siguienteId > 0) ? siguienteId : 1; // Si es NULL, empezar desde 1
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 1; // Si hay un error, devuelve 1 como ID inicial
    }

    /**
     * ✅ Método para agregar un nuevo proveedor
     */
    public static boolean agregarProveedor(Proveedor proveedor) {
        String sql = "INSERT INTO proveedores (nombre, direccion, telefono, email, cif, observaciones) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, proveedor.getNombre());
            stmt.setString(2, proveedor.getDireccion());
            stmt.setString(3, proveedor.getTelefono());
            stmt.setString(4, proveedor.getEmail());
            stmt.setString(5, proveedor.getCif());
            stmt.setString(6, proveedor.getObservaciones());

            return stmt.executeUpdate() > 0; // ✅ Retorna true si se insertó correctamente
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * ✅ Método para obtener un proveedor por su ID
     */
    public static Proveedor obtenerProveedorPorId(int id) {
        String sql = "SELECT id, nombre, direccion, telefono, email, cif, observaciones FROM proveedores WHERE id=?";

        try (Connection conn = DatabaseConnection.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Proveedor(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("direccion"),
                        rs.getString("telefono"),
                        rs.getString("email"),
                        rs.getString("cif"),
                        rs.getString("observaciones")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Si no se encuentra el proveedor, retorna null
    }

    /**
     * ✅ Método para actualizar un proveedor
     */
    public static boolean actualizarProveedor(Proveedor proveedor) {
        String sql = "UPDATE proveedores SET nombre=?, direccion=?, telefono=?, email=?, cif=?, observaciones=? WHERE id=?";

        try (Connection conn = DatabaseConnection.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, proveedor.getNombre());
            stmt.setString(2, proveedor.getDireccion());
            stmt.setString(3, proveedor.getTelefono());
            stmt.setString(4, proveedor.getEmail());
            stmt.setString(5, proveedor.getCif());
            stmt.setString(6, proveedor.getObservaciones());
            stmt.setInt(7, proveedor.getId());

            return stmt.executeUpdate() > 0; // ✅ Retorna true si se actualizó correctamente
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * ✅ Método para eliminar un proveedor
     */
    public static boolean eliminarProveedor(int id) {
        String sql = "DELETE FROM proveedores WHERE id=?";

        try (Connection conn = DatabaseConnection.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0; // ✅ Retorna true si se eliminó correctamente
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
