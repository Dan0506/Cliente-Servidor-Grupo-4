package org.octoberEats.Modelos;

import javax.swing.*;

public class Repartidor extends Thread {
    private String estado;
    private int tiempo;
    private JLabel label;

    public Repartidor(int tiempoLlegada, JLabel driv) {
        this.tiempo=tiempoLlegada*1000/2;
        this.label = driv;

    }

    public void run() {
        estado = "En camino al restaurante";
        label.setText(estado);
        System.out.println(estado);
        System.out.println(tiempo);
        try {
            Repartidor.sleep(tiempo);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        estado = "En camino a su dirección";
        label.setText(estado);
        System.out.println(estado);
        System.out.println(tiempo);
        try {
            Repartidor.sleep(tiempo);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        estado = "El repartidor está afuera";
        label.setText(estado);
        System.out.println(estado);
    }
}
