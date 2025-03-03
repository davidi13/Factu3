package com.facturacion.factu3.models;

import com.facturacion.factu3.database.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {
    public static boolean actualizarCliente(Cliente cliente) {
        String sql = "UPDATE clientes SET nombreCliente=?, direccionCliente=?, cpCliente=?, poblacionCliente=?, provinciaCliente=?, paisCliente=?, cifCliente=?, telCliente=?, emailCliente=?, ibanCliente=?, riesgoCliente=?, descuentoCliente=?, observacionesCliente=? WHERE id=?";

        try (Connection conn = DatabaseConnection.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cliente.getNombreCliente());
            stmt.setString(2, cliente.getDireccionCliente());
            stmt.setString(3, cliente.getCpCliente());
            stmt.setString(4, cliente.getPoblacionCliente());
            stmt.setString(5, cliente.getProvinciaCliente());
            stmt.setString(6, cliente.getPaisCliente());
            stmt.setString(7, cliente.getCifCliente());
            stmt.setString(8, cliente.getTelCliente());
            stmt.setString(9, cliente.getEmailCliente());
            stmt.setString(10, cliente.getIbanCliente());
            stmt.setDouble(11, cliente.getRiesgoCliente());
            stmt.setDouble(12, cliente.getDescuentoCliente());
            stmt.setString(13, cliente.getObservacionesCliente());
            stmt.setInt(14, cliente.getId());

            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean eliminarCliente(int id) {
        String sql = "DELETE FROM clientes WHERE id=?";

        try (Connection conn = DatabaseConnection.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * Obtiene el siguiente ID disponible en la tabla clientes.
     * @return El próximo ID a usar.
     */
    public static int obtenerSiguienteId() {
        String sql = "SELECT MAX(id) FROM clientes";
        try (Connection conn = DatabaseConnection.obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt(1) + 1;
            }
        } catch (SQLException e) {
            System.out.println("❌ Error al obtener el siguiente ID: " + e.getMessage());
        }
        return 1; // Si no hay registros, empieza en 1
    }

    /**
     * Agrega un nuevo cliente a la base de datos.
     * @param cliente Objeto Cliente con los datos a insertar.
     * @return true si la inserción fue exitosa, false si falló.
     */
    public static boolean agregarCliente(Cliente cliente) {
        String sql = "INSERT INTO clientes (id, nombreCliente, direccionCliente, cpCliente, poblacionCliente, provinciaCliente, paisCliente, cifCliente, telCliente, emailCliente, ibanCliente, riesgoCliente, descuentoCliente, observacionesCliente) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            int nuevoId = obtenerSiguienteId(); // Obtener el próximo ID disponible
            stmt.setInt(1, nuevoId);
            stmt.setString(2, cliente.getNombreCliente());
            stmt.setString(3, cliente.getDireccionCliente());
            stmt.setString(4, cliente.getCpCliente());
            stmt.setString(5, cliente.getPoblacionCliente());
            stmt.setString(6, cliente.getProvinciaCliente());
            stmt.setString(7, cliente.getPaisCliente());
            stmt.setString(8, cliente.getCifCliente());
            stmt.setString(9, cliente.getTelCliente());
            stmt.setString(10, cliente.getEmailCliente());
            stmt.setString(11, cliente.getIbanCliente());
            stmt.setDouble(12, cliente.getRiesgoCliente());
            stmt.setDouble(13, cliente.getDescuentoCliente());
            stmt.setString(14, cliente.getObservacionesCliente());

            int filasAfectadas = stmt.executeUpdate();
            if (filasAfectadas > 0) {
                System.out.println("✅ Cliente guardado con éxito en la BD.");
                return true;
            } else {
                System.out.println("❌ No se insertaron filas en la BD.");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("❌ Error SQL al guardar cliente: " + e.getMessage());
            return false;
        }
    }

    /**
     * Obtiene la lista de todos los clientes almacenados en la BD.
     * @return Lista de clientes.
     */
    public static List<Cliente> obtenerClientes() {
        List<Cliente> listaClientes = new ArrayList<>();
        String sql = "SELECT * FROM clientes";

        try (Connection conn = DatabaseConnection.obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Cliente cliente = new Cliente(
                        rs.getInt("id"),
                        rs.getString("nombreCliente"),
                        rs.getString("direccionCliente"),
                        rs.getString("cpCliente"),
                        rs.getString("poblacionCliente"),
                        rs.getString("provinciaCliente"),
                        rs.getString("paisCliente"),
                        rs.getString("cifCliente"),
                        rs.getString("telCliente"),
                        rs.getString("emailCliente"),
                        rs.getString("ibanCliente"),
                        rs.getDouble("riesgoCliente"),
                        rs.getDouble("descuentoCliente"),
                        rs.getString("observacionesCliente")
                );
                listaClientes.add(cliente);
            }
            System.out.println("✅ Clientes cargados correctamente.");
        } catch (SQLException e) {
            System.out.println("❌ Error al obtener clientes: " + e.getMessage());
        }

        return listaClientes;
    }

    public static Cliente obtenerClientePorId(int id) {
        String sql = "SELECT * FROM clientes WHERE id = ?";
        try (Connection conn = DatabaseConnection.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Cliente(
                        rs.getInt("id"),
                        rs.getString("nombreCliente"),
                        rs.getString("direccionCliente"),
                        rs.getString("cpCliente"),
                        rs.getString("poblacionCliente"),
                        rs.getString("provinciaCliente"),
                        rs.getString("paisCliente"),
                        rs.getString("cifCliente"),
                        rs.getString("telCliente"),
                        rs.getString("emailCliente"),
                        rs.getString("ibanCliente"),
                        rs.getDouble("riesgoCliente"),
                        rs.getDouble("descuentoCliente"),
                        rs.getString("observacionesCliente")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
