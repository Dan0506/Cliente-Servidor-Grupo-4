package org.octoberEats.Modelos;

public class Restaurante {
    private int idRestaurante;
    private String nombre;
    private String direccion;

    public Restaurante(int idRestaurante, String nombre, String direccion) {
        this.idRestaurante = idRestaurante;
        this.nombre = nombre;
        this.direccion = direccion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public int getIdRestaurante() {
        return idRestaurante;
    }

    public void setIdRestaurante(int idRestaurante) {
        this.idRestaurante = idRestaurante;
    }

    @Override
    public String toString() {
        return nombre;  // Muestra el nombre del restaurante en el JComboBox
    }
}