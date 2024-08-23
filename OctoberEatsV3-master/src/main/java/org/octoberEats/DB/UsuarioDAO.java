package org.octoberEats.DB;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {
    private ResultSet resultado = null;
    private ConexcionDB connectionDB; // Asegúrate de usar el nombre correcto

    public UsuarioDAO(ConexcionDB connectionDB) {
        this.connectionDB = connectionDB;
    }

    public String insertarUsuario(String nombre, String password) {
        try {
            connectionDB.setConexion();
            connectionDB.setConsulta("Insert into users(nombre, password) values(?,?)");
            connectionDB.getConsulta().setString(1, nombre);
            connectionDB.getConsulta().setString(2, password);
            return connectionDB.getConsulta().executeUpdate() > 0 ? "Nuevo usuario registrado" : "Error en el registro!";
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connectionDB.cerrarConexion();
        }
        return null;
    }
}