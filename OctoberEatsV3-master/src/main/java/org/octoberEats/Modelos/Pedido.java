package org.octoberEats.Modelos;

import javax.swing.*;
import java.util.Random;

public class Pedido extends Thread {
    private String estado;
    private JLabel label;

    public Pedido(JLabel rest) {
        this.label = rest;
    }


    public void run() {
        estado = "Preparando pedido";
        System.out.println(estado);
        int max=10000,min=15000;
        int tiempo = min + (int)(Math.random() * ((max - min) + 1));
        System.out.println(tiempo);
        try {
            Pedido.sleep(tiempo);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        estado = "Pedido listo";
        label.setText(estado);
        System.out.println(estado);
    }
}
