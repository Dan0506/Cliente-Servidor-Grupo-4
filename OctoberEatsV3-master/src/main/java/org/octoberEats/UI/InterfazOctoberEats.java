package org.octoberEats.UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import org.octoberEats.DB.ConexcionDB;
import org.octoberEats.DB.RestauranteDAO;
import org.octoberEats.DB.MenuDAO;
import org.octoberEats.DB.ItemMenuDAO;
import org.octoberEats.Modelos.*;
import org.octoberEats.Modelos.Menu;

public class InterfazOctoberEats extends JFrame {
    private JComboBox<Restaurante> restauranteComboBox;
    private JList<ItemMenu> menuList;
    private DefaultListModel<ItemMenu> menuModel;
    private JButton addButton;
    private JButton removeButton;
    private JButton viewCartButton;
    private JButton proceedButton;
    private JTextArea pedidoArea;
    private JTextField cantidadField;
    private Carrito carrito;

    public InterfazOctoberEats() {
        setTitle("Pedido");
        setSize(800, 600); // Tamaño de la ventana principal
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Crear el diálogo de inicio de sesión
        JDialog loginDialog = new JDialog(this, "Iniciar Sesión", true);
        loginDialog.setSize(400, 250); // Tamaño del diálogo de inicio de sesión
        loginDialog.setLayout(new GridLayout(3, 2));

        // Campos de texto para usuario y contraseña
        JTextField userField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        // Establecer tamaño preferido más pequeño para los campos de texto
        Dimension fieldSize = new Dimension(100, 25); // Ancho de 100 y altura de 25
        userField.setPreferredSize(fieldSize);
        passwordField.setPreferredSize(fieldSize);

        // Botones de iniciar y salir
        JButton loginButton = new JButton("Iniciar");
        JButton exitButton = new JButton("Salir");

        // Añadir componentes al diálogo
        loginDialog.add(new JLabel("Usuario:"));
        loginDialog.add(userField);
        loginDialog.add(new JLabel("Contraseña:"));
        loginDialog.add(passwordField);
        loginDialog.add(loginButton);
        loginDialog.add(exitButton);

        // Acción para el botón de iniciar sesión
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = userField.getText();
                String password = new String(passwordField.getPassword());
                if (verifyCredentials(username, password)) {
                    JOptionPane.showMessageDialog(loginDialog, "Inicio de sesión exitoso");
                    loginDialog.dispose(); // Cerrar el diálogo al iniciar sesión
                } else {
                    JOptionPane.showMessageDialog(loginDialog, "Usuario o contraseña incorrectos", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Acción para el botón de salir
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0); // Cerrar la aplicación
            }
        });

        // Mostrar el diálogo de inicio de sesión
        loginDialog.setLocationRelativeTo(this);
        loginDialog.setVisible(true);

        // Configuración de la interfaz de pedidos
        carrito = new Carrito();
        restauranteComboBox = new JComboBox<>();
        menuModel = new DefaultListModel<>();
        menuList = new JList<>(menuModel);
        addButton = new JButton("Agregar");
        removeButton = new JButton("Eliminar");
        viewCartButton = new JButton("Ver Carrito");
        proceedButton = new JButton("Proceder con el Pago");
        pedidoArea = new JTextArea();
        cantidadField = new JTextField("1", 5);

        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Seleccione Restaurante:"));
        topPanel.add(restauranteComboBox);
        topPanel.add(new JLabel("Restaurante:"));
        JLabel reststatus = new JLabel("Pendiente");
        topPanel.add(reststatus);
        topPanel.add(new JLabel("Repartidor:"));
        JLabel drivstatus = new JLabel("Pendiente");
        topPanel.add(drivstatus);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(new JScrollPane(menuList), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(new JLabel("Cantidad:"));
        bottomPanel.add(cantidadField);
        bottomPanel.add(addButton);
        bottomPanel.add(removeButton);
        bottomPanel.add(viewCartButton);
        bottomPanel.add(proceedButton);
        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
        add(new JScrollPane(pedidoArea), BorderLayout.EAST);

        // Cargar restaurantes
        ConexcionDB conexcionDB = new ConexcionDB();
        RestauranteDAO restauranteDAO = new RestauranteDAO(conexcionDB);
        List<Restaurante> restaurantes = restauranteDAO.consultarRestaurantes();
        for (Restaurante restaurante : restaurantes) {
            restauranteComboBox.addItem(restaurante);
        }

        // Cargar menú cuando se selecciona un restaurante
        restauranteComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Restaurante selectedRestaurante = (Restaurante) restauranteComboBox.getSelectedItem();
                if (selectedRestaurante != null) {
                    loadMenu(selectedRestaurante.getIdRestaurante());
                }
            }
        });

        // Acción para agregar ítems al carrito
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ItemMenu selectedItem = menuList.getSelectedValue();
                if (selectedItem != null) {
                    try {
                        int cantidad = Integer.parseInt(cantidadField.getText());
                        if (cantidad >= 1) {
                            for (int i = 0; i < cantidad; i++) {
                                carrito.agregarPlato(selectedItem);
                            }
                            actualizarPedido();
                        } else {
                            JOptionPane.showMessageDialog(InterfazOctoberEats.this, "La cantidad mínima es 1", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(InterfazOctoberEats.this, "Ingrese una cantidad válida", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        // Acción para eliminar ítems del carrito
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                carrito.vaciarCarrito();
                actualizarPedido();
            }
        });

        // Acción para ver el carrito
        viewCartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarCarrito();
            }
        });

        // Acción para proceder con el pago
        proceedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reststatus.setText("Preparando pedido");
                procederConPago(reststatus, drivstatus);
            }
        });
    }

    private void loadMenu(int restauranteId) {
        menuModel.clear();
        ConexcionDB conexcionDB = new ConexcionDB();
        MenuDAO menuDAO = new MenuDAO(conexcionDB);
        List<Menu> menus = menuDAO.getMenuDeRestaurante(restauranteId);
        for (Menu menu : menus) {
            ItemMenuDAO itemMenuDAO = new ItemMenuDAO(conexcionDB);
            List<ItemMenu> items = itemMenuDAO.getItemMenu(menu.getIdMenu());
            for (ItemMenu item : items) {
                menuModel.addElement(item);
            }
        }
    }

    private void actualizarPedido() {
        pedidoArea.setText("");
        for (ItemMenu item : carrito.getPlatos()) {
            pedidoArea.append(item.getNombre() + " - " + item.getPrecio() + "\n");
        }
        pedidoArea.append("\nTotal: " + carrito.calcularTotal());
    }

    private void mostrarCarrito() {
        StringBuilder carritoInfo = new StringBuilder("Carrito de Compras:\n");
        for (ItemMenu item : carrito.getPlatos()) {
            carritoInfo.append(item.getNombre()).append(" - ").append(item.getPrecio()).append("\n");
        }
        carritoInfo.append("\nTotal: ").append(carrito.calcularTotal());
        JOptionPane.showMessageDialog(this, carritoInfo.toString(), "Carrito", JOptionPane.INFORMATION_MESSAGE);
    }

    private void procederConPago(JLabel rest, JLabel driv) {
        String[] opciones = {"Tarjeta", "Efectivo"};
        int seleccion = JOptionPane.showOptionDialog(this, "Seleccione el método de pago", "Método de Pago", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, opciones, opciones[0]);
        if (seleccion != -1) {
            String metodoPago = opciones[seleccion];
            double total = carrito.calcularTotal();

            // Generar un tiempo de llegada al azar entre 10 y 30 minutos
            int tiempoLlegada = 10 + (int)(Math.random() * 21);

            // Generar el mensaje de confirmación usando el método centralizado
            String mensajeConfirmacion = generarMensajeConfirmacion(metodoPago, total, tiempoLlegada, rest, driv);

            JOptionPane.showMessageDialog(this, mensajeConfirmacion, "Confirmación de Pago", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private String generarMensajeConfirmacion(String metodoPago, double total, int tiempoLlegada, JLabel rest, JLabel driv) {
        StringBuilder mensaje = new StringBuilder("Detalle de la Compra:\n");
        for (ItemMenu item : carrito.getPlatos()) {
            mensaje.append(item.getNombre()).append(" - ").append(item.getPrecio()).append("\n");
        }
        mensaje.append("\nTotal: ").append(total);
        mensaje.append("\nMétodo de Pago: ").append(metodoPago);
        mensaje.append("\nSu entrega será en: Universidad Fidélitas");
        mensaje.append("\nTiempo de llegada estimado: ").append(tiempoLlegada).append(" minutos");
        Pedido thread1 = new Pedido(rest);
        Repartidor thread2 = new Repartidor(tiempoLlegada, driv);
        thread1.start();
        try {
            thread1.join(); // wait for the worker thread to finish
        } catch (InterruptedException e) {
            // Handle exception if necessary
        }
        thread2.start();

        return mensaje.toString();
    }

    // Método para verificar las credenciales (ejemplo simple)
    private boolean verifyCredentials(String username, String password) {
        // Aquí puedes añadir la lógica para verificar el usuario y contraseña con la base de datos
        return "admin".equals(username) && "password".equals(password);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            InterfazOctoberEats frame = new InterfazOctoberEats();
            frame.setVisible(true);
        });
    }
}