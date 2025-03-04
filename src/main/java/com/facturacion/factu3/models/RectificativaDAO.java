package com.facturacion.factu3.models;

import com.facturacion.factu3.database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class RectificativaDAO {
    private static Connection connection = DatabaseConnection.obtenerConexion();

    public static List<Rectificativa> obtenerTodasLasRectificativas() {
        List<Rectificativa> rectificativas = new ArrayList<>();
        String sql = "SELECT numeroRectificativaCliente, fechaRectificativaCliente, idClienteRectificativaCliente, " +
                "baseImponibleRectificativaCliente, ivaRectificativaCliente, totalRectificativaCliente, observacionesRectificativaCliente " +
                "FROM rectificativasClientes";

        if (connection == null) {
            System.err.println("❌ Error: No hay conexión a la base de datos.");
            return rectificativas;
        }

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Rectificativa rectificativa = new Rectificativa();
                rectificativa.setNumeroFactura(resultSet.getInt("numeroRectificativaCliente"));
                rectificativa.setFecha(resultSet.getDate("fechaRectificativaCliente"));
                rectificativa.setIdCliente(resultSet.getInt("idClienteRectificativaCliente"));
                rectificativa.setBaseImponible(resultSet.getDouble("baseImponibleRectificativaCliente"));
                rectificativa.setIva(resultSet.getDouble("ivaRectificativaCliente"));
                rectificativa.setTotal(resultSet.getDouble("totalRectificativaCliente"));
                rectificativa.setObservaciones(resultSet.getString("observacionesRectificativaCliente"));

                rectificativas.add(rectificativa);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("❌ Error obteniendo las rectificativas.");
        }
        return rectificativas;
    }
}
