package octoberEats.UI;

import javax.swing.*;
import java.awt.*;

public class VentanaLogIn extends JFrame {
    private JPanel mainPanel;

    public VentanaLogIn() {
        setTitle("Ventana LogIn");
        setSize(320, 568);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Crear un ImagePanel y agregarlo al JFrame
        mainPanel = new ImagePanel("src/main/java/octoberEats/imagenes/Designer.jpeg");
        add(mainPanel);

        setVisible(true);
    }

    // Clase interna para manejar el JPanel con la imagen
    private static class ImagePanel extends JPanel {
        private final Image image;

        public ImagePanel(String imagePath) {
            // Cargar la imagen desde el archivo
            ImageIcon icon = new ImageIcon(imagePath);
            image = icon.getImage();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // Dibujar la imagen en el JPanel
            g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        }
    }

}

