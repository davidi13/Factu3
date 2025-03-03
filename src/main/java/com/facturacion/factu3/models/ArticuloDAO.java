package com.facturacion.factu3.models;

import com.facturacion.factu3.database.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ArticuloDAO {

    public static List<Articulo> obtenerArticulos() {
        List<Articulo> lista = new ArrayList<>();
        String query = "SELECT * FROM articulos";

        try (Connection conn = DatabaseConnection.obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Articulo articulo = new Articulo(
                        rs.getInt("idArticulo"),
                        rs.getString("codigoArticulo"),
                        rs.getString("codigoBarrasArticulo"),
                        rs.getString("descripcionArticulo"),
                        rs.getInt("familiaArticulo"),
                        rs.getDouble("costeArticulo"),
                        rs.getDouble("margenComercialArticulo"),
                        rs.getDouble("pvpArticulo"),
                        rs.getInt("proveedorArticulo"),
                        rs.getDouble("stockArticulo"),
                        rs.getString("observacionesArticulo")
                );
                lista.add(articulo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public static Articulo obtenerArticuloPorId(int id) {
        String sql = "SELECT * FROM articulos WHERE id = ?";
        try (Connection conn = DatabaseConnection.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Articulo(
                        rs.getInt("id"),
                        rs.getString("codigoArticulo"),
                        rs.getString("descripcionArticulo"),
                        rs.getDouble("pvpArticulo"),
                        rs.getInt("stockArticulo")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static boolean agregarArticulo(Articulo articulo) {
        String query = "INSERT INTO articulos (codigoArticulo, codigoBarrasArticulo, descripcionArticulo, familiaArticulo, " +
                "costeArticulo, margenComercialArticulo, pvpArticulo, proveedorArticulo, stockArticulo, observacionesArticulo) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, articulo.getCodigoArticulo());
            stmt.setString(2, articulo.getCodigoBarras());
            stmt.setString(3, articulo.getDescripcion());
            stmt.setInt(4, articulo.getFamilia());
            stmt.setDouble(5, articulo.getCoste());
            stmt.setDouble(6, articulo.getMargenComercial());
            stmt.setDouble(7, articulo.getPvp());
            stmt.setInt(8, articulo.getProveedor());
            stmt.setDouble(9, articulo.getStock());
            stmt.setString(10, articulo.getObservaciones());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean actualizarArticulo(Articulo articulo) {
        String query = "UPDATE articulos SET codigoArticulo=?, codigoBarrasArticulo=?, descripcionArticulo=?, familiaArticulo=?, " +
                "costeArticulo=?, margenComercialArticulo=?, pvpArticulo=?, proveedorArticulo=?, stockArticulo=?, observacionesArticulo=? " +
                "WHERE idArticulo=?";

        try (Connection conn = DatabaseConnection.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, articulo.getCodigoArticulo());
            stmt.setString(2, articulo.getCodigoBarras());
            stmt.setString(3, articulo.getDescripcion());
            stmt.setInt(4, articulo.getFamilia());
            stmt.setDouble(5, articulo.getCoste());
            stmt.setDouble(6, articulo.getMargenComercial());
            stmt.setDouble(7, articulo.getPvp());
            stmt.setInt(8, articulo.getProveedor());
            stmt.setDouble(9, articulo.getStock());
            stmt.setString(10, articulo.getObservaciones());
            stmt.setInt(11, articulo.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String obtenerSiguienteCodigoArticulo() {
        String sql = "SELECT codigoArticulo FROM articulos ORDER BY idArticulo DESC LIMIT 1";

        try (Connection conn = DatabaseConnection.obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                String ultimoCodigo = rs.getString("codigoArticulo");

                // Extraer el número y sumarle 1
                int numero = Integer.parseInt(ultimoCodigo.replaceAll("\\D", "")) + 1;

                // Devolver nuevo código con formato PROD### (ejemplo: PROD007)
                return String.format("PROD%03d", numero);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Si no hay artículos en la base de datos, comenzamos con "PROD001"
        return "PROD001";
    }



    public static boolean eliminarArticulo(int id) {
        String query = "DELETE FROM articulos WHERE idArticulo=?";

        try (Connection conn = DatabaseConnection.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
