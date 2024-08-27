package org.octoberEats.DB;

import org.octoberEats.Modelos.Menu;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MenuDAO {
    private ConexcionDB conexcionDB;
    private ResultSet resultado;

    // Constructor que acepta ConexcionDB
    public MenuDAO(ConexcionDB conexcionDB) {
        this.conexcionDB = conexcionDB;
    }

    public List<Menu> getMenuDeRestaurante(int idRestaurante) {
        List<Menu> menus = new ArrayList<>();
        try {
            conexcionDB.setConexion();
            conexcionDB.setConsulta("SELECT * FROM menus WHERE idRestaurante = ?");
            conexcionDB.getConsulta().setInt(1, idRestaurante);
            resultado = conexcionDB.getResultado();

            while (resultado.next()) {
                int idMenu = resultado.getInt("idMenu");
                String nombre = resultado.getString("nombre");
                menus.add(new Menu(idMenu, nombre, idRestaurante));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            conexcionDB.cerrarConexion();
        }
        return menus;
    }
}