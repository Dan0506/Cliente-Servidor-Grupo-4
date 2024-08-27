package org.octoberEats.DB;

import org.octoberEats.Modelos.Restaurante;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RestauranteDAO {
    ResultSet resultado = null;
    private ConexcionDB connectionDB;

    // Constructor
    public RestauranteDAO(ConexcionDB connectionDB) {
        this.connectionDB = connectionDB;
    }

    // Consulta los restaurantes de la base de datos
    public List<Restaurante> consultarRestaurantes() {
        List<Restaurante> restaurantes = new ArrayList<>();
        try {
            connectionDB.setConexion();
            connectionDB.setConsulta("SELECT idRestaurante, nombre, direccion FROM restaurantes");
            resultado = connectionDB.getResultado();

            while (resultado.next()) {
                int idRestaurante = resultado.getInt("idRestaurante");
                String nombre = resultado.getString("nombre");
                String direccion = resultado.getString("direccion");
                restaurantes.add(new Restaurante(idRestaurante, nombre, direccion));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connectionDB.cerrarConexion();
        }
        return restaurantes;
    }

    public Restaurante consultarRestaurantePorNombre(String nombreRestaurante) {
        Restaurante restaurante = null;
        try {
            connectionDB.setConexion();
            connectionDB.setConsulta("SELECT idRestaurante, nombre, direccion FROM restaurantes WHERE nombre = ?");
            connectionDB.getConsulta().setString(1, nombreRestaurante);
            resultado = connectionDB.getResultado();

            if (resultado.next()) {
                int idRestaurante = resultado.getInt("idRestaurante");
                String nombre = resultado.getString("nombre");
                String direccion = resultado.getString("direccion");
                restaurante = new Restaurante(idRestaurante, nombre, direccion);
            }
        } catch (SQLException e) {
            System.err.println("Error al consultar el restaurante: " + e.getMessage());
        } finally {
            connectionDB.cerrarConexion();
        }
        return restaurante;
    }
}