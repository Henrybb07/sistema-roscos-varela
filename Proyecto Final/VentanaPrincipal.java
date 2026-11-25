package poo.roscos;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class VentanaPrincipal extends JFrame {

    private static final long serialVersionUID = 1L;

    // ================= Paleta y fuentes =================
    private static final Color COLOR_FONDO  = new Color(0xFA, 0xF3, 0xE8);
    private static final Color COLOR_MARRON = new Color(0x5C, 0x3A, 0x1E);
    private static final Color COLOR_DORADO = new Color(0xC9, 0xA6, 0x6B);
    private static final Color COLOR_CREMA  = new Color(0xF5, 0xE6, 0xD3);
    private static final Color COLOR_VERDE  = new Color(0x4F, 0x79, 0x42);
    private static final Color COLOR_ROJO   = new Color(0xA9, 0x32, 0x26);
    private static final Color COLOR_BLANCO = new Color(0xFF, 0xFD, 0xF7);
    private static final Color COLOR_ESTADO_SIN  = new Color(0xF8, 0xD7, 0xD5);
    private static final Color COLOR_ESTADO_BAJO = new Color(0xFB, 0xEE, 0xC4);

    private static final Font F_TITULO  = new Font("Serif",     Font.BOLD,  26);
    private static final Font F_SUBTIT  = new Font("Serif",     Font.ITALIC,14);
    private static final Font F_ETIQ    = new Font("SansSerif", Font.BOLD,  13);
    private static final Font F_CAMPO   = new Font("SansSerif", Font.PLAIN, 14);
    private static final Font F_BOTON   = new Font("SansSerif", Font.BOLD,  13);
    private static final Font F_TABLA   = new Font("SansSerif", Font.PLAIN, 13);
    private static final Font F_KPI     = new Font("SansSerif", Font.BOLD,  22);
    private static final Font F_KPI_LBL = new Font("SansSerif", Font.BOLD,  11);

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // ================= Estado =================
    private JPanel contentPane;
    private JTabbedPane tabbedPane;

    // --- Pedidos ---
    private JTextField txtIdPedido;
    private JComboBox<ClienteItem> cmbCliente;
    private JTextField txtVainilla, txtCocoa, txtCafe;
    private JTextField txtDireccionPedido;
    private JTextField txtFechaPedido, txtFechaEntrega;
    private JTextField txtTotal;
    private JComboBox<String> cmbEstadoPedido;
    private JTable tablaPedidos;
    private DefaultTableModel modeloTablaPedidos;

    // --- Clientes ---
    private JTextField txtIdCliente, txtNombreCafeteria, txtDireccionCliente, txtContactoCliente;
    private JTable tablaClientes;
    private DefaultTableModel modeloTablaClientes;

    // --- Insumos producción ---
    private JTable tablaInsProd;
    private DefaultTableModel modeloInsProd;
    private JTextField txtIdInsProd, txtNomInsProd, txtCantInsProd, txtMinInsProd;
    private JComboBox<String> cmbUnidadInsProd;

    // --- Insumos empaquetado ---
    private JTable tablaInsEmp;
    private DefaultTableModel modeloInsEmp;
    private JTextField txtIdInsEmp, txtNomInsEmp, txtCantInsEmp, txtMinInsEmp;
    private JComboBox<String> cmbUnidadInsEmp;

    // --- Planeación ---
    private JTable tablaPlaneacion;
    private DefaultTableModel modeloPlaneacion;

    // --- Ventas ---
    private JComboBox<ClienteItem> cmbFiltroVentas;
    private JTabbedPane subTabsVentas;
    private JLabel lblKpiIngresos, lblKpiPaquetes, lblKpiCafeterias, lblKpiTicket;
    private int subTabVentasActiva = 0;
    private JTable tablaDesgloseMesActual, tablaCafeteriasMesActual;
    private JTable tablaDesgloseMesPasado, tablaCafeteriasMesPasado;
    private JTable tablaDesglosePersonalizado, tablaCafeteriasPersonalizado;
    private JTextField txtDesdeVenta, txtHastaVenta;

    // ================= DAOs =================
    private final PedidoDAO daoPedido = new PedidoDAO();
    private final ClienteDAO daoCliente = new ClienteDAO();
    private final InsumoProduccionDAO daoInsProd = new InsumoProduccionDAO();
    private final InsumoEmpaquetadoDAO daoInsEmp = new InsumoEmpaquetadoDAO();
    private final PlaneacionDAO daoPlaneacion = new PlaneacionDAO();
    private final VentaDAO daoVenta = new VentaDAO();

    public VentanaPrincipal() {
        setTitle("Roscos Varela - Sistema de Gestión Integral");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1120, 780);
        setLocationRelativeTo(null);

        contentPane = new JPanel(new BorderLayout(0, 5));
        contentPane.setBackground(COLOR_FONDO);
        contentPane.setBorder(new EmptyBorder(10, 15, 15, 15));
        setContentPane(contentPane);

        contentPane.add(crearEncabezado(), BorderLayout.NORTH);

        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(F_ETIQ);
        tabbedPane.setBackground(COLOR_CREMA);
        tabbedPane.setForeground(COLOR_MARRON);
        tabbedPane.setOpaque(true);

        construirPestanasSegunRol();

        contentPane.add(tabbedPane, BorderLayout.CENTER);

        cargarClientesCombo();
        cargarTablaPedidos();

        if (Sesion.puedeVerClientes())   cargarTablaClientes();
        if (Sesion.puedeVerInsumos())   { cargarTablaInsProd(); cargarTablaInsEmp(); }
        if (Sesion.puedeVerVentas())    { cargarFiltroVentas(); refrescarVentasSegunSubTab(); }

        configurarEventos();
    }

    private void construirPestanasSegunRol() {
        tabbedPane.addTab("Gestión de Pedidos", crearPanelPedidos());
        if (Sesion.puedeVerClientes())   tabbedPane.addTab("Gestión de Clientes", crearPanelClientes());
        if (Sesion.puedeVerInsumos())    tabbedPane.addTab("Gestión de Insumos", crearPanelInsumos());
        if (Sesion.puedeVerPlaneacion()) tabbedPane.addTab("Planeación", crearPanelPlaneacion());
        if (Sesion.puedeVerVentas())     tabbedPane.addTab("Ventas", crearPanelVentas());
    }

    // ============================================================
    // ENCABEZADO GLOBAL (LOGO + TÍTULOS + USUARIO)
    // ============================================================
    private JPanel crearEncabezado() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COLOR_FONDO);

        // ---- IZQUIERDA: LOGO ----
        JPanel izquierda = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        izquierda.setBackground(COLOR_FONDO);
        
        JLabel lblLogo = new JLabel();
        try {
            URL urlLogo = getClass().getResource("/imagenes/logo2025.png");
            if (urlLogo != null) {
                ImageIcon iconoOriginal = new ImageIcon(urlLogo);
                Image img = iconoOriginal.getImage();
                Image imgEscalada = img.getScaledInstance(-1, 90, Image.SCALE_SMOOTH);
                lblLogo.setIcon(new ImageIcon(imgEscalada));
            }
        } catch (Exception e) { }
        izquierda.add(lblLogo);
        panel.add(izquierda, BorderLayout.WEST);

        // ---- CENTRO: TÍTULOS ----
        JPanel centro = new JPanel();
        centro.setLayout(new BoxLayout(centro, BoxLayout.Y_AXIS));
        centro.setBackground(COLOR_FONDO);
        centro.add(Box.createVerticalStrut(15));

        JLabel titulo = new JLabel("Roscos Varela");
        titulo.setFont(F_TITULO);
        titulo.setForeground(COLOR_MARRON);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitulo = new JLabel("Control de Ventas, Clientes, Producción e Insumos");
        subtitulo.setFont(F_SUBTIT);
        subtitulo.setForeground(COLOR_DORADO);
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        centro.add(titulo);
        centro.add(subtitulo);
        panel.add(centro, BorderLayout.CENTER);

        // ---- DERECHA: USUARIO Y CERRAR SESIÓN ----
        JPanel derecha = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 4));
        derecha.setBackground(COLOR_FONDO);

        Usuario u = Sesion.getUsuario();
        if (u != null) {
            JLabel lblUsuario = new JLabel("👤 " + u.getNombreUsuario() + "  (" + u.getRol().etiquetaBonita() + ")");
            lblUsuario.setFont(F_ETIQ);
            lblUsuario.setForeground(COLOR_MARRON);
            derecha.add(lblUsuario);
        }

        JButton btnCerrarSesion = boton("Cerrar sesión", COLOR_ROJO, Color.WHITE, e -> cerrarSesion());
        btnCerrarSesion.setPreferredSize(new Dimension(140, 30));
        derecha.add(btnCerrarSesion);

        panel.add(derecha, BorderLayout.EAST);

        JPanel contenedor = new JPanel(new BorderLayout());
        contenedor.setBackground(COLOR_FONDO);
        contenedor.add(panel, BorderLayout.CENTER);

        JSeparator linea = new JSeparator();
        linea.setForeground(COLOR_DORADO);
        contenedor.add(linea, BorderLayout.SOUTH);

        return contenedor;
    }

    private void cerrarSesion() {
        int resp = JOptionPane.showConfirmDialog(this, "¿Cerrar sesión y volver al login?", "Cerrar sesión", JOptionPane.YES_NO_OPTION);
        if (resp != JOptionPane.YES_OPTION) return;

        Sesion.cerrar();
        dispose();

        java.awt.EventQueue.invokeLater(() -> {
            LoginDialog login = new LoginDialog();
            login.setVisible(true);
            if (!login.isLoginExitoso()) {
                System.exit(0);
            }
            new VentanaPrincipal().setVisible(true);
        });
    }

    // ============================================================
    // PESTAÑA PEDIDOS
    // ============================================================
    private JPanel crearPanelPedidos() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(COLOR_FONDO);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.add(crearFormularioPedidos(), BorderLayout.CENTER);
        panel.add(crearTablaPedidosUI(), BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearFormularioPedidos() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(COLOR_FONDO);
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(5, 8, 5, 8);
        g.anchor = GridBagConstraints.WEST;

        boolean soloLectura = Sesion.esProduccion();

        txtIdPedido = crearCampo(6);
        cmbCliente = crearComboCliente(260);
        g.gridx = 0; g.gridy = 0; panel.add(etiqueta("ID Pedido:"), g);
        g.gridx = 1; panel.add(txtIdPedido, g);
        g.gridx = 2; panel.add(etiqueta("Cliente:"), g);
        g.gridx = 3; panel.add(cmbCliente, g);

        txtVainilla = crearCampo(5); txtVainilla.setText("0");
        txtCocoa    = crearCampo(5); txtCocoa.setText("0");
        txtCafe     = crearCampo(5); txtCafe.setText("0");

        JPanel panelCant = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        panelCant.setBackground(COLOR_FONDO);
        panelCant.add(etiqueta("Vainilla:")); panelCant.add(txtVainilla);
        panelCant.add(etiqueta("Cocoa:"));    panelCant.add(txtCocoa);
        panelCant.add(etiqueta("Café:"));     panelCant.add(txtCafe);

        g.gridx = 0; g.gridy = 1; panel.add(etiqueta("Cantidades:"), g);
        g.gridx = 1; g.gridwidth = 3; panel.add(panelCant, g);
        g.gridwidth = 1;

        txtDireccionPedido = crearCampo(45);
        txtDireccionPedido.setEditable(false);
        txtDireccionPedido.setBackground(COLOR_CREMA);

        g.gridx = 0; g.gridy = 2; panel.add(etiqueta("Dirección:"), g);
        g.gridx = 1; g.gridwidth = 3; panel.add(txtDireccionPedido, g);
        g.gridwidth = 1;

        txtFechaPedido  = crearCampo(12);
        txtFechaEntrega = crearCampo(12);
        txtFechaEntrega.setEditable(false);
        txtFechaEntrega.setBackground(COLOR_CREMA);

        cmbEstadoPedido = crearComboEstado(110);

        JPanel panelFechas = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        panelFechas.setBackground(COLOR_FONDO);
        panelFechas.add(etiqueta("Pedido:"));  panelFechas.add(txtFechaPedido);
        panelFechas.add(etiqueta("Entrega:")); panelFechas.add(txtFechaEntrega);
        panelFechas.add(etiqueta("Estado:"));  panelFechas.add(cmbEstadoPedido);

        g.gridx = 0; g.gridy = 3; panel.add(etiqueta("Detalles:"), g);
        g.gridx = 1; g.gridwidth = 3; panel.add(panelFechas, g);
        g.gridwidth = 1;

        txtTotal = crearCampo(10);
        txtTotal.setEditable(false);
        txtTotal.setFont(new Font("SansSerif", Font.BOLD, 15));
        txtTotal.setForeground(COLOR_MARRON);
        txtTotal.setBackground(COLOR_CREMA);

        g.gridx = 0; g.gridy = 4; panel.add(etiqueta("Total:"), g);
        g.gridx = 1; panel.add(txtTotal, g);
        JLabel lblP = new JLabel("  (paquete a $25.00)");
        lblP.setFont(F_SUBTIT); lblP.setForeground(COLOR_DORADO);
        g.gridx = 2; g.gridwidth = 2; panel.add(lblP, g);
        g.gridwidth = 1;

        JPanel pBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 8));
        pBotones.setBackground(COLOR_FONDO);

        if (soloLectura) {
            pBotones.add(boton("Buscar pedido", COLOR_DORADO, Color.WHITE, e -> accionPedido("buscar")));
            pBotones.add(boton("Marcar Realizado", COLOR_VERDE, Color.WHITE, e -> marcarPedidoRealizado()));
            pBotones.add(boton("Marcar Entregado", COLOR_MARRON, Color.WHITE, e -> marcarPedidoEntregado()));
            pBotones.add(boton("Limpiar", COLOR_CREMA, COLOR_MARRON, e -> limpiarPedido()));

            txtVainilla.setEditable(false);
            txtCocoa.setEditable(false);
            txtCafe.setEditable(false);
            txtFechaPedido.setEditable(false);
            cmbCliente.setEnabled(false);
            cmbEstadoPedido.setEnabled(false);
        } else {
            pBotones.add(boton("Buscar",   COLOR_DORADO, Color.WHITE, e -> accionPedido("buscar")));
            pBotones.add(boton("Nuevo",    COLOR_MARRON, Color.WHITE, e -> accionPedido("nuevo")));
            pBotones.add(boton("Guardar",  COLOR_VERDE,  Color.WHITE, e -> accionPedido("guardar")));
            pBotones.add(boton("Eliminar", COLOR_ROJO,   Color.WHITE, e -> accionPedido("eliminar")));
            pBotones.add(boton("Limpiar",  COLOR_CREMA,  COLOR_MARRON, e -> accionPedido("limpiar")));
        }

        g.gridx = 0; g.gridy = 5; g.gridwidth = 4;
        g.fill = GridBagConstraints.HORIZONTAL;
        panel.add(pBotones, g);

        return panel;
    }

    private JScrollPane crearTablaPedidosUI() {
        String[] cols = {"ID", "Cliente", "Vainilla", "Cocoa", "Café", "Dirección", "F. Pedido", "F. Entrega", "Total", "Estado"};
        modeloTablaPedidos = new DefaultTableModel(cols, 0) {
            private static final long serialVersionUID = 1L;
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        tablaPedidos = new JTable(modeloTablaPedidos);
        configurarEstiloTabla(tablaPedidos);

        tablaPedidos.getColumnModel().getColumn(0).setPreferredWidth(35);
        tablaPedidos.getColumnModel().getColumn(1).setPreferredWidth(100);
        tablaPedidos.getColumnModel().getColumn(2).setPreferredWidth(50);
        tablaPedidos.getColumnModel().getColumn(3).setPreferredWidth(50);
        tablaPedidos.getColumnModel().getColumn(4).setPreferredWidth(50);
        tablaPedidos.getColumnModel().getColumn(5).setPreferredWidth(220);
        tablaPedidos.getColumnModel().getColumn(6).setPreferredWidth(80);
        tablaPedidos.getColumnModel().getColumn(7).setPreferredWidth(80);
        tablaPedidos.getColumnModel().getColumn(8).setPreferredWidth(70);
        tablaPedidos.getColumnModel().getColumn(9).setPreferredWidth(85);

        JScrollPane scroll = new JScrollPane(tablaPedidos);
        scroll.setBorder(new LineBorder(COLOR_DORADO, 1, true));
        scroll.setPreferredSize(new Dimension(1040, 200));
        scroll.getViewport().setBackground(COLOR_BLANCO);
        return scroll;
    }

    // ============================================================
    // PESTAÑA CLIENTES
    // ============================================================
    private JPanel crearPanelClientes() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(COLOR_FONDO);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.add(crearFormularioClientes(), BorderLayout.CENTER);
        panel.add(crearTablaClientesUI(), BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearFormularioClientes() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(COLOR_FONDO);
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 10, 8, 10);
        g.anchor = GridBagConstraints.WEST;

        txtIdCliente         = crearCampo(6);
        txtNombreCafeteria   = crearCampo(30);
        txtDireccionCliente  = crearCampo(45);
        txtContactoCliente   = crearCampo(25);

        g.gridx = 0; g.gridy = 0; panel.add(etiqueta("ID Cliente:"), g);
        g.gridx = 1; panel.add(txtIdCliente, g);
        g.gridx = 2; panel.add(etiqueta("Cafetería:"), g);
        g.gridx = 3; panel.add(txtNombreCafeteria, g);

        g.gridx = 0; g.gridy = 1; panel.add(etiqueta("Dirección:"), g);
        g.gridx = 1; g.gridwidth = 3; panel.add(txtDireccionCliente, g);
        g.gridwidth = 1;

        g.gridx = 0; g.gridy = 2; panel.add(etiqueta("Contacto:"), g);
        g.gridx = 1; panel.add(txtContactoCliente, g);

        JPanel pBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 12));
        pBotones.setBackground(COLOR_FONDO);
        pBotones.add(boton("Buscar",   COLOR_DORADO, Color.WHITE, e -> accionCliente("buscar")));
        pBotones.add(boton("Nuevo",    COLOR_MARRON, Color.WHITE, e -> accionCliente("nuevo")));
        pBotones.add(boton("Guardar",  COLOR_VERDE,  Color.WHITE, e -> accionCliente("guardar")));
        pBotones.add(boton("Eliminar", COLOR_ROJO,   Color.WHITE, e -> accionCliente("eliminar")));
        pBotones.add(boton("Limpiar",  COLOR_CREMA,  COLOR_MARRON, e -> accionCliente("limpiar")));

        g.gridx = 0; g.gridy = 3; g.gridwidth = 4;
        g.fill = GridBagConstraints.HORIZONTAL;
        panel.add(pBotones, g);

        return panel;
    }

    private JScrollPane crearTablaClientesUI() {
        String[] cols = {"ID", "Nombre de Cafetería", "Dirección", "Contacto"};
        modeloTablaClientes = new DefaultTableModel(cols, 0) {
            private static final long serialVersionUID = 1L;
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        tablaClientes = new JTable(modeloTablaClientes);
        configurarEstiloTabla(tablaClientes);

        tablaClientes.getColumnModel().getColumn(0).setPreferredWidth(50);
        tablaClientes.getColumnModel().getColumn(1).setPreferredWidth(200);
        tablaClientes.getColumnModel().getColumn(2).setPreferredWidth(350);
        tablaClientes.getColumnModel().getColumn(3).setPreferredWidth(150);

        JScrollPane scroll = new JScrollPane(tablaClientes);
        scroll.setBorder(new LineBorder(COLOR_DORADO, 1, true));
        scroll.setPreferredSize(new Dimension(1040, 220));
        scroll.getViewport().setBackground(COLOR_BLANCO);
        return scroll;
    }

    // ============================================================
    // PESTAÑA INSUMOS
    // ============================================================
    private JPanel crearPanelInsumos() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(COLOR_FONDO);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JTabbedPane subTabs = new JTabbedPane();
        subTabs.setFont(F_ETIQ);
        subTabs.setBackground(COLOR_CREMA);
        subTabs.setForeground(COLOR_MARRON);
        subTabs.setOpaque(true);
        subTabs.addTab("Insumos de Producción", crearSubPanelInsProd());
        subTabs.addTab("Insumos de Empaquetado", crearSubPanelInsEmp());

        panel.add(subTabs, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearSubPanelInsProd() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(COLOR_FONDO);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.add(crearFormularioInsProd(), BorderLayout.CENTER);
        panel.add(crearTablaInsProdUI(), BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearFormularioInsProd() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(COLOR_FONDO);
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6, 8, 6, 8);
        g.anchor = GridBagConstraints.WEST;

        txtIdInsProd   = crearCampo(6);
        txtNomInsProd  = crearCampo(30);
        txtCantInsProd = crearCampo(10);
        txtMinInsProd  = crearCampo(10);
        cmbUnidadInsProd = crearComboUnidadProd(100);

        g.gridx = 0; g.gridy = 0; panel.add(etiqueta("ID:"), g);
        g.gridx = 1; panel.add(txtIdInsProd, g);
        g.gridx = 2; panel.add(etiqueta("Nombre:"), g);
        g.gridx = 3; panel.add(txtNomInsProd, g);

        g.gridx = 0; g.gridy = 1; panel.add(etiqueta("Cantidad:"), g);
        g.gridx = 1; panel.add(txtCantInsProd, g);
        g.gridx = 2; panel.add(etiqueta("Unidad:"), g);
        g.gridx = 3; panel.add(cmbUnidadInsProd, g);

        g.gridx = 0; g.gridy = 2; panel.add(etiqueta("Stock mínimo:"), g);
        g.gridx = 1; panel.add(txtMinInsProd, g);

        JPanel pBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        pBotones.setBackground(COLOR_FONDO);
        pBotones.add(boton("Buscar",   COLOR_DORADO, Color.WHITE, e -> accionInsProd("buscar")));
        pBotones.add(boton("Nuevo",    COLOR_MARRON, Color.WHITE, e -> accionInsProd("nuevo")));
        pBotones.add(boton("Guardar",  COLOR_VERDE,  Color.WHITE, e -> accionInsProd("guardar")));
        pBotones.add(boton("Eliminar", COLOR_ROJO,   Color.WHITE, e -> accionInsProd("eliminar")));
        pBotones.add(boton("Limpiar",  COLOR_CREMA,  COLOR_MARRON, e -> accionInsProd("limpiar")));
        pBotones.add(boton("Actualizar lista", COLOR_MARRON, Color.WHITE, e -> cargarTablaInsProd()));

        g.gridx = 0; g.gridy = 3; g.gridwidth = 4;
        g.fill = GridBagConstraints.HORIZONTAL;
        panel.add(pBotones, g);

        return panel;
    }

    private JScrollPane crearTablaInsProdUI() {
        String[] cols = {"ID", "Nombre", "Cantidad", "Unidad", "Stock mínimo", "Estado"};
        modeloInsProd = new DefaultTableModel(cols, 0) {
            private static final long serialVersionUID = 1L;
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaInsProd = new JTable(modeloInsProd);
        configurarEstiloTabla(tablaInsProd);

        tablaInsProd.getColumnModel().getColumn(0).setPreferredWidth(50);
        tablaInsProd.getColumnModel().getColumn(1).setPreferredWidth(300);
        tablaInsProd.getColumnModel().getColumn(2).setPreferredWidth(100);
        tablaInsProd.getColumnModel().getColumn(3).setPreferredWidth(80);
        tablaInsProd.getColumnModel().getColumn(4).setPreferredWidth(110);
        tablaInsProd.getColumnModel().getColumn(5).setPreferredWidth(100);
        tablaInsProd.getColumnModel().getColumn(5).setCellRenderer(crearRendererEstado());

        tablaInsProd.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tablaInsProd.getSelectedRow() >= 0) {
                int id = (int) modeloInsProd.getValueAt(tablaInsProd.getSelectedRow(), 0);
                cargarInsProdPorId(id);
            }
        });

        JScrollPane scroll = new JScrollPane(tablaInsProd);
        scroll.setBorder(new LineBorder(COLOR_DORADO, 1, true));
        scroll.setPreferredSize(new Dimension(1040, 260));
        scroll.getViewport().setBackground(COLOR_BLANCO);
        return scroll;
    }

    private JPanel crearSubPanelInsEmp() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(COLOR_FONDO);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.add(crearFormularioInsEmp(), BorderLayout.CENTER);
        panel.add(crearTablaInsEmpUI(), BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearFormularioInsEmp() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(COLOR_FONDO);
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6, 8, 6, 8);
        g.anchor = GridBagConstraints.WEST;

        txtIdInsEmp   = crearCampo(6);
        txtNomInsEmp  = crearCampo(30);
        txtCantInsEmp = crearCampo(10);
        txtMinInsEmp  = crearCampo(10);
        cmbUnidadInsEmp = crearComboUnidadEmp(100);

        g.gridx = 0; g.gridy = 0; panel.add(etiqueta("ID:"), g);
        g.gridx = 1; panel.add(txtIdInsEmp, g);
        g.gridx = 2; panel.add(etiqueta("Nombre:"), g);
        g.gridx = 3; panel.add(txtNomInsEmp, g);

        g.gridx = 0; g.gridy = 1; panel.add(etiqueta("Cantidad:"), g);
        g.gridx = 1; panel.add(txtCantInsEmp, g);
        g.gridx = 2; panel.add(etiqueta("Unidad:"), g);
        g.gridx = 3; panel.add(cmbUnidadInsEmp, g);

        g.gridx = 0; g.gridy = 2; panel.add(etiqueta("Stock mínimo:"), g);
        g.gridx = 1; panel.add(txtMinInsEmp, g);

        JPanel pBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        pBotones.setBackground(COLOR_FONDO);
        pBotones.add(boton("Buscar",   COLOR_DORADO, Color.WHITE, e -> accionInsEmp("buscar")));
        pBotones.add(boton("Nuevo",    COLOR_MARRON, Color.WHITE, e -> accionInsEmp("nuevo")));
        pBotones.add(boton("Guardar",  COLOR_VERDE,  Color.WHITE, e -> accionInsEmp("guardar")));
        pBotones.add(boton("Eliminar", COLOR_ROJO,   Color.WHITE, e -> accionInsEmp("eliminar")));
        pBotones.add(boton("Limpiar",  COLOR_CREMA,  COLOR_MARRON, e -> accionInsEmp("limpiar")));
        pBotones.add(boton("Actualizar lista", COLOR_MARRON, Color.WHITE, e -> cargarTablaInsEmp()));

        g.gridx = 0; g.gridy = 3; g.gridwidth = 4;
        g.fill = GridBagConstraints.HORIZONTAL;
        panel.add(pBotones, g);

        return panel;
    }

    private JScrollPane crearTablaInsEmpUI() {
        String[] cols = {"ID", "Nombre", "Cantidad", "Unidad", "Stock mínimo", "Estado"};
        modeloInsEmp = new DefaultTableModel(cols, 0) {
            private static final long serialVersionUID = 1L;
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaInsEmp = new JTable(modeloInsEmp);
        configurarEstiloTabla(tablaInsEmp);

        tablaInsEmp.getColumnModel().getColumn(0).setPreferredWidth(50);
        tablaInsEmp.getColumnModel().getColumn(1).setPreferredWidth(300);
        tablaInsEmp.getColumnModel().getColumn(2).setPreferredWidth(100);
        tablaInsEmp.getColumnModel().getColumn(3).setPreferredWidth(80);
        tablaInsEmp.getColumnModel().getColumn(4).setPreferredWidth(110);
        tablaInsEmp.getColumnModel().getColumn(5).setPreferredWidth(100);
        tablaInsEmp.getColumnModel().getColumn(5).setCellRenderer(crearRendererEstado());

        tablaInsEmp.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tablaInsEmp.getSelectedRow() >= 0) {
                int id = (int) modeloInsEmp.getValueAt(tablaInsEmp.getSelectedRow(), 0);
                cargarInsEmpPorId(id);
            }
        });

        JScrollPane scroll = new JScrollPane(tablaInsEmp);
        scroll.setBorder(new LineBorder(COLOR_DORADO, 1, true));
        scroll.setPreferredSize(new Dimension(1040, 260));
        scroll.getViewport().setBackground(COLOR_BLANCO);
        return scroll;
    }

    private DefaultTableCellRenderer crearRendererEstado() {
        return new DefaultTableCellRenderer() {
            private static final long serialVersionUID = 1L;
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean foc, int r, int c) {
                Component comp = super.getTableCellRendererComponent(t, v, sel, foc, r, c);
                if (!sel) {
                    String est = String.valueOf(v);
                    if ("Sin stock".equals(est))      comp.setBackground(COLOR_ESTADO_SIN);
                    else if ("Bajo".equals(est))     comp.setBackground(COLOR_ESTADO_BAJO);
                    else                             comp.setBackground(COLOR_BLANCO);
                    comp.setForeground(COLOR_MARRON);
                } else {
                    comp.setBackground(COLOR_DORADO);
                    comp.setForeground(Color.WHITE);
                }
                return comp;
            }
        };
    }

    // ============================================================
    // PESTAÑA PLANEACIÓN
    // ============================================================
    private JPanel crearPanelPlaneacion() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(COLOR_FONDO);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel superior = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        superior.setBackground(COLOR_FONDO);

        JLabel lblInfo = new JLabel("Calcula kg de harina e insumos a comprar según pedidos pendientes:");
        lblInfo.setFont(F_ETIQ);
        lblInfo.setForeground(COLOR_MARRON);

        JButton btnCalcular = boton("Calcular planeación", COLOR_VERDE, Color.WHITE, e -> calcularPlaneacion());

        superior.add(lblInfo);
        superior.add(btnCalcular);

        panel.add(superior, BorderLayout.NORTH);
        panel.add(crearTablaPlaneacionUI(), BorderLayout.CENTER);
        return panel;
    }

    private JScrollPane crearTablaPlaneacionUI() {
        String[] cols = {"ID Pedido", "Sabor", "Paq. Pedidos", "Kg Harina",
                         "Insumo", "Necesario", "En Stock", "A Comprar", "Unidad"};

        modeloPlaneacion = new DefaultTableModel(cols, 0) {
            private static final long serialVersionUID = 1L;
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        tablaPlaneacion = new JTable(modeloPlaneacion);
        configurarEstiloTabla(tablaPlaneacion);

        tablaPlaneacion.getColumnModel().getColumn(0).setPreferredWidth(70);
        tablaPlaneacion.getColumnModel().getColumn(1).setPreferredWidth(80);
        tablaPlaneacion.getColumnModel().getColumn(2).setPreferredWidth(90);
        tablaPlaneacion.getColumnModel().getColumn(3).setPreferredWidth(90);
        tablaPlaneacion.getColumnModel().getColumn(4).setPreferredWidth(160);
        tablaPlaneacion.getColumnModel().getColumn(5).setPreferredWidth(80);
        tablaPlaneacion.getColumnModel().getColumn(6).setPreferredWidth(80);
        tablaPlaneacion.getColumnModel().getColumn(7).setPreferredWidth(90);
        tablaPlaneacion.getColumnModel().getColumn(8).setPreferredWidth(70);

        JScrollPane scroll = new JScrollPane(tablaPlaneacion);
        scroll.setBorder(new LineBorder(COLOR_DORADO, 1, true));
        scroll.setPreferredSize(new Dimension(1040, 400));
        scroll.getViewport().setBackground(COLOR_BLANCO);
        return scroll;
    }

    private void calcularPlaneacion() {
        modeloPlaneacion.setRowCount(0);
        List<Object[]> pedidos = daoPlaneacion.pedidosPendientes();
        String[] sabores = {"Vainilla", "Cocoa", "Café"};
        int[] cantidadesPedido = new int[3];

        for (Object[] p : pedidos) {
            int idPedido = (Integer) p[0];
            cantidadesPedido[0] = (Integer) p[1];
            cantidadesPedido[1] = (Integer) p[2];
            cantidadesPedido[2] = (Integer) p[3];

            for (int i = 0; i < 3; i++) {
                int paquetes = cantidadesPedido[i];
                if (paquetes <= 0) continue;
                String sabor = sabores[i];

                double harinaBase = (double) paquetes / PlanProduccion.PAQUETES_POR_KG_HARINA;
                double mantecaTeorica = harinaBase * 0.50;
                double mantecaReal = Math.ceil(mantecaTeorica / 0.250) * 0.250;
                double harinaReal = mantecaReal * 2.0;

                List<Object[]> receta = daoPlaneacion.recetaPorSabor(sabor);
                boolean primeraFilaDelSabor = true;

                for (Object[] fila : receta) {
                    int idInsumo        = (Integer) fila[0];
                    String nombreInsumo = (String)  fila[1];
                    double cantReceta   = (Double)  fila[2];
                    String unidad       = (String)  fila[3];

                    double necesario = harinaReal * cantReceta;
                    double enStock   = daoPlaneacion.stockInsumo(idInsumo);
                    double aComprar  = Math.max(0, necesario - enStock);

                    Object colId       = primeraFilaDelSabor ? idPedido : "";
                    Object colSabor    = primeraFilaDelSabor ? sabor : "";
                    Object colPaquetes = primeraFilaDelSabor ? paquetes : "";
                    Object colHarina   = primeraFilaDelSabor ? String.format("%.2f", harinaReal) : "";

                    modeloPlaneacion.addRow(new Object[] {
                        colId, colSabor, colPaquetes, colHarina,
                        nombreInsumo,
                        String.format("%.2f", necesario),
                        String.format("%.2f", enStock),
                        String.format("%.2f", aComprar),
                        unidad
                    });

                    primeraFilaDelSabor = false;
                }
            }
        }

        if (modeloPlaneacion.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No hay pedidos pendientes por planear.", "Sin datos", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // ============================================================
    // PESTAÑA VENTAS
    // ============================================================
    private JPanel crearPanelVentas() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(COLOR_FONDO);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel norte = new JPanel(new BorderLayout(0, 10));
        norte.setBackground(COLOR_FONDO);

        JPanel panelFiltro = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 4));
        panelFiltro.setBackground(COLOR_FONDO);
        panelFiltro.add(etiqueta("Cafetería:"));
        cmbFiltroVentas = crearComboCliente(260);
        panelFiltro.add(cmbFiltroVentas);
        norte.add(panelFiltro, BorderLayout.NORTH);

        JPanel panelKpis = new JPanel(new GridLayout(1, 4, 12, 0));
        panelKpis.setBackground(COLOR_FONDO);
        lblKpiIngresos   = crearEtiquetaKpi("$0.00");
        lblKpiPaquetes   = crearEtiquetaKpi("0");
        lblKpiCafeterias = crearEtiquetaKpi("0");
        lblKpiTicket     = crearEtiquetaKpi("$0.00");
        panelKpis.add(crearTarjetaKpi(lblKpiIngresos,   "Ingresos"));
        panelKpis.add(crearTarjetaKpi(lblKpiPaquetes,   "Paquetes vendidos"));
        panelKpis.add(crearTarjetaKpi(lblKpiCafeterias, "Cafeterías activas"));
        panelKpis.add(crearTarjetaKpi(lblKpiTicket,     "Ticket promedio"));
        norte.add(panelKpis, BorderLayout.CENTER);

        panel.add(norte, BorderLayout.NORTH);

        subTabsVentas = new JTabbedPane();
        subTabsVentas.setFont(F_ETIQ);
        subTabsVentas.setBackground(COLOR_CREMA);
        subTabsVentas.setForeground(COLOR_MARRON);
        subTabsVentas.setOpaque(true);

        subTabsVentas.addTab("Mes actual", crearSubPanelVentas(false, 0));
        subTabsVentas.addTab("Mes pasado", crearSubPanelVentas(false, 1));
        subTabsVentas.addTab("Periodo personalizado", crearSubPanelVentas(true, 2));

        subTabsVentas.addChangeListener(e -> {
            subTabVentasActiva = subTabsVentas.getSelectedIndex();
            refrescarVentasSegunSubTab();
        });

        panel.add(subTabsVentas, BorderLayout.CENTER);
        cmbFiltroVentas.addActionListener(e -> refrescarVentasSegunSubTab());

        return panel;
    }

    private JPanel crearSubPanelVentas(boolean conRango, int indice) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(COLOR_FONDO);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        if (conRango) {
            JPanel panelRango = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
            panelRango.setBackground(COLOR_FONDO);
            panelRango.add(etiqueta("Desde:"));
            txtDesdeVenta = crearCampo(12);
            txtDesdeVenta.setText(LocalDate.now().withDayOfMonth(1).format(FMT));
            panelRango.add(txtDesdeVenta);

            panelRango.add(etiqueta("Hasta:"));
            txtHastaVenta = crearCampo(12);
            txtHastaVenta.setText(LocalDate.now().format(FMT));
            panelRango.add(txtHastaVenta);

            JButton btnCalcular = boton("Calcular", COLOR_VERDE, Color.WHITE, e -> calcularVentasPersonalizado());
            panelRango.add(btnCalcular);

            panel.add(panelRango, BorderLayout.NORTH);
        }

        JTable tablaDesglose   = crearTablaDesgloseSaborLocal();
        JTable tablaCafeterias = crearTablaCafeteriasVentaLocal();

        if (indice == 0) {
            tablaDesgloseMesActual     = tablaDesglose;
            tablaCafeteriasMesActual   = tablaCafeterias;
        } else if (indice == 1) {
            tablaDesgloseMesPasado     = tablaDesglose;
            tablaCafeteriasMesPasado   = tablaCafeterias;
        } else {
            tablaDesglosePersonalizado = tablaDesglose;
            tablaCafeteriasPersonalizado = tablaCafeterias;
        }

        JPanel contenido = new JPanel(new GridLayout(2, 1, 0, 10));
        contenido.setBackground(COLOR_FONDO);
        contenido.add(crearPanelConTabla("Desglose por sabor", tablaDesglose));
        contenido.add(crearPanelConTabla("Cafeterías con ventas en el periodo", tablaCafeterias));

        panel.add(contenido, BorderLayout.CENTER);
        return panel;
    }

    private JTable crearTablaDesgloseSaborLocal() {
        String[] cols = {"Sabor", "Paquetes", "Ingresos", "% del total"};
        DefaultTableModel modelo = new DefaultTableModel(cols, 0) {
            private static final long serialVersionUID = 1L;
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tabla = new JTable(modelo);
        configurarEstiloTabla(tabla);
        tabla.getColumnModel().getColumn(0).setPreferredWidth(120);
        tabla.getColumnModel().getColumn(1).setPreferredWidth(100);
        tabla.getColumnModel().getColumn(2).setPreferredWidth(120);
        tabla.getColumnModel().getColumn(3).setPreferredWidth(100);
        return tabla;
    }

    private JTable crearTablaCafeteriasVentaLocal() {
        String[] cols = {"Cafetería", "Pedidos", "Paquetes", "Ingresos"};
        DefaultTableModel modelo = new DefaultTableModel(cols, 0) {
            private static final long serialVersionUID = 1L;
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tabla = new JTable(modelo);
        configurarEstiloTabla(tabla);
        tabla.getColumnModel().getColumn(0).setPreferredWidth(250);
        tabla.getColumnModel().getColumn(1).setPreferredWidth(90);
        tabla.getColumnModel().getColumn(2).setPreferredWidth(100);
        tabla.getColumnModel().getColumn(3).setPreferredWidth(120);
        return tabla;
    }

    private JPanel crearPanelConTabla(String titulo, JTable tabla) {
        JPanel p = new JPanel(new BorderLayout(0, 4));
        p.setBackground(COLOR_FONDO);
        JLabel lbl = new JLabel(titulo);
        lbl.setFont(F_ETIQ);
        lbl.setForeground(COLOR_MARRON);
        p.add(lbl, BorderLayout.NORTH);
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(new LineBorder(COLOR_DORADO, 1, true));
        scroll.getViewport().setBackground(COLOR_BLANCO);
        p.add(scroll, BorderLayout.CENTER);
        return p;
    }

    private JLabel crearEtiquetaKpi(String valorInicial) {
        JLabel l = new JLabel(valorInicial);
        l.setFont(F_KPI);
        l.setForeground(COLOR_MARRON);
        l.setHorizontalAlignment(JLabel.CENTER);
        return l;
    }

    private JPanel crearTarjetaKpi(JLabel valor, String titulo) {
        JPanel card = new JPanel(new BorderLayout(0, 4));
        card.setBackground(COLOR_CREMA);
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(COLOR_DORADO, 1, true),
            new EmptyBorder(10, 10, 10, 10)
        ));
        card.add(valor, BorderLayout.CENTER);

        JLabel lblTitulo = new JLabel(titulo, JLabel.CENTER);
        lblTitulo.setFont(F_KPI_LBL);
        lblTitulo.setForeground(COLOR_DORADO);
        card.add(lblTitulo, BorderLayout.SOUTH);

        return card;
    }

    private void cargarFiltroVentas() {
        if (cmbFiltroVentas == null) return;
        DefaultComboBoxModel<ClienteItem> modelo = new DefaultComboBoxModel<>();
        modelo.addElement(new ClienteItem(0, "Todas las cafeterías", ""));
        for (Cliente c : daoCliente.listar()) {
            modelo.addElement(new ClienteItem(c.getIdCliente(), c.getNombreCafeteria(), c.getDireccion()));
        }
        cmbFiltroVentas.setModel(modelo);
    }

    private void refrescarVentasSegunSubTab() {
        switch (subTabVentasActiva) {
            case 0: calcularVentasMesActual();   break;
            case 1: calcularVentasMesPasado();   break;
            case 2: break;
        }
    }

    private void calcularVentasMesActual() {
        LocalDate hoy = LocalDate.now();
        calcularVentas(hoy.withDayOfMonth(1), hoy, tablaDesgloseMesActual, tablaCafeteriasMesActual);
    }

    private void calcularVentasMesPasado() {
        LocalDate hoy = LocalDate.now();
        calcularVentas(
            hoy.minusMonths(1).withDayOfMonth(1),
            hoy.withDayOfMonth(1).minusDays(1),
            tablaDesgloseMesPasado, tablaCafeteriasMesPasado
        );
    }

    private void calcularVentasPersonalizado() {
        try {
            LocalDate desde = LocalDate.parse(txtDesdeVenta.getText().trim(), FMT);
            LocalDate hasta = LocalDate.parse(txtHastaVenta.getText().trim(), FMT);
            if (desde.isAfter(hasta)) {
                JOptionPane.showMessageDialog(this, "La fecha 'Desde' no puede ser posterior a 'Hasta'.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            calcularVentas(desde, hasta, tablaDesglosePersonalizado, tablaCafeteriasPersonalizado);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Escribe fechas válidas (yyyy-MM-dd).", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void calcularVentas(LocalDate desde, LocalDate hasta, JTable tablaDesglose, JTable tablaCafeterias) {
        Integer idCliente = null;
        ClienteItem sel = (ClienteItem) cmbFiltroVentas.getSelectedItem();
        if (sel != null && sel.id > 0) idCliente = sel.id;

        ResumenVenta r = daoVenta.obtenerResumen(desde, hasta, idCliente);
        refrescarKpisVenta(r);

        if (tablaDesglose != null)   refrescarTablaDesgloseSabor(tablaDesglose, r);
        if (tablaCafeterias != null) refrescarTablaCafeteriasVenta(tablaCafeterias, r);
    }

    private void refrescarKpisVenta(ResumenVenta r) {
        lblKpiIngresos.setText(String.format("$%.2f", r.getTotalIngresos()));
        lblKpiPaquetes.setText(String.valueOf(r.getTotalPaquetes()));
        lblKpiCafeterias.setText(String.valueOf(r.getCafeteriasActivas()));
        lblKpiTicket.setText(String.format("$%.2f", r.getTicketPromedio()));
    }

    private void refrescarTablaDesgloseSabor(JTable tabla, ResumenVenta r) {
        DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
        modelo.setRowCount(0);
        if (!r.hayVentas()) {
            modelo.addRow(new Object[]{"Sin ventas en el periodo", "-", "-", "-"});
            return;
        }
        for (ResumenVenta.FilaSabor f : r.getDesgloseSabores()) {
            modelo.addRow(new Object[]{
                f.getSabor(), f.getPaquetes(),
                String.format("$%.2f", f.getIngresos()),
                String.format("%.1f%%", f.getPorcentaje())
            });
        }
        modelo.addRow(new Object[]{"TOTAL", r.getTotalPaquetes(), String.format("$%.2f", r.getTotalIngresos()), "100%"});
    }

    private void refrescarTablaCafeteriasVenta(JTable tabla, ResumenVenta r) {
        DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
        modelo.setRowCount(0);
        if (!r.hayVentas()) {
            modelo.addRow(new Object[]{"Sin ventas en el periodo", "-", "-", "-"});
            return;
        }
        for (ResumenVenta.FilaCafeteria f : r.getCafeterias()) {
            modelo.addRow(new Object[]{
                f.getNombre(), f.getNumPedidos(), f.getPaquetes(), String.format("$%.2f", f.getIngresos())
            });
        }
    }

    // ============================================================
    // CARGA DE DATOS
    // ============================================================
    private void cargarClientesCombo() {
        DefaultComboBoxModel<ClienteItem> modelo = new DefaultComboBoxModel<>();
        List<Cliente> lista = daoCliente.listar();
        for (Cliente c : lista) {
            modelo.addElement(new ClienteItem(c.getIdCliente(), c.getNombreCafeteria(), c.getDireccion()));
        }
        cmbCliente.setModel(modelo);
        if (cmbCliente.getItemCount() > 0) {
            txtDireccionPedido.setText(cmbCliente.getItemAt(0).direccion);
        }
    }

    private void cargarTablaPedidos() {
        modeloTablaPedidos.setRowCount(0);
        for (Pedido p : daoPedido.listar()) {
            modeloTablaPedidos.addRow(new Object[]{
                    p.getIdPedido(), nombreCliente(p.getIdCliente()), p.getCantidadVainilla(),
                    p.getCantidadCocoa(), p.getCantidadCafe(), p.getDireccionEntrega(),
                    p.getFechaPedido(), p.getFechaEntrega(), String.format("$%.2f", p.getTotal()), p.getEstado()
            });
        }
    }

    private void cargarTablaClientes() {
        modeloTablaClientes.setRowCount(0);
        for (Cliente c : daoCliente.listar()) {
            modeloTablaClientes.addRow(new Object[]{
                    c.getIdCliente(), c.getNombreCafeteria(), c.getDireccion(), c.getContacto()
            });
        }
    }

    private void cargarTablaInsProd() {
        if (modeloInsProd == null) return;
        modeloInsProd.setRowCount(0);
        for (InsumoProduccion i : daoInsProd.listar()) {
            modeloInsProd.addRow(new Object[]{
                    i.getIdInsumo(), i.getNombreInsumo(), i.getCantidad(), i.getUnidad(),
                    i.getStockMinimo(), i.getEstado()
            });
        }
    }

    private void cargarTablaInsEmp() {
        if (modeloInsEmp == null) return;
        modeloInsEmp.setRowCount(0);
        for (InsumoEmpaquetado i : daoInsEmp.listar()) {
            modeloInsEmp.addRow(new Object[]{
                    i.getIdInsumoEmpaque(), i.getNombreInsumo(), i.getCantidad(), i.getUnidad(),
                    i.getStockMinimo(), i.getEstado()
            });
        }
    }

    private String nombreCliente(int id) {
        for (int i = 0; i < cmbCliente.getItemCount(); i++) {
            ClienteItem ci = cmbCliente.getItemAt(i);
            if (ci.id == id) return ci.nombre;
        }
        return "Cliente " + id;
    }

    private void configurarEventos() {
        DocumentListener dl = new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { recalcularPedido(); }
            public void removeUpdate(DocumentEvent e) { recalcularPedido(); }
            public void changedUpdate(DocumentEvent e) { recalcularPedido(); }
        };
        txtVainilla.getDocument().addDocumentListener(dl);
        txtCocoa.getDocument().addDocumentListener(dl);
        txtCafe.getDocument().addDocumentListener(dl);
        txtFechaPedido.getDocument().addDocumentListener(dl);

        cmbCliente.addActionListener(e -> {
            ClienteItem sel = (ClienteItem) cmbCliente.getSelectedItem();
            if (sel != null) txtDireccionPedido.setText(sel.direccion);
        });

        tablaPedidos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tablaPedidos.getSelectedRow() >= 0) {
                int id = (int) modeloTablaPedidos.getValueAt(tablaPedidos.getSelectedRow(), 0);
                cargarPedidoPorId(id);
            }
        });

        if (tablaClientes != null) {
            tablaClientes.getSelectionModel().addListSelectionListener(e -> {
                if (!e.getValueIsAdjusting() && tablaClientes.getSelectedRow() >= 0) {
                    int id = (int) modeloTablaClientes.getValueAt(tablaClientes.getSelectedRow(), 0);
                    cargarClientePorId(id);
                }
            });
        }
    }

    private void recalcularPedido() {
        try {
            int v = parsearInt(txtVainilla.getText());
            int c = parsearInt(txtCocoa.getText());
            int f = parsearInt(txtCafe.getText());
            txtTotal.setText(String.format("$%.2f", (v + c + f) * Pedido.PRECIO_PAQUETE));
        } catch (Exception e) {
            txtTotal.setText("$0.00");
        }
        try {
            LocalDate fp = LocalDate.parse(txtFechaPedido.getText().trim(), FMT);
            txtFechaEntrega.setText(fp.plusDays(Pedido.DIAS_ENTREGA).format(FMT));
        } catch (Exception e) {
            txtFechaEntrega.setText("");
        }
    }

    private int parsearInt(String s) {
        if (s == null || s.trim().isEmpty()) return 0;
        return Integer.parseInt(s.trim());
    }

    private double parsearDouble(String s) {
        if (s == null || s.trim().isEmpty()) return 0;
        return Double.parseDouble(s.trim().replace(",", "."));
    }

    // ============================================================
    // ACCIONES DE PEDIDOS
    // ============================================================
    private void accionPedido(String cmd) {
        switch (cmd) {
            case "buscar":   buscarPedido();   break;
            case "nuevo":    limpiarPedido();  break;
            case "guardar":  guardarPedido();  break;
            case "eliminar": eliminarPedido(); break;
            case "limpiar":  limpiarPedido();  break;
        }
    }

    private void buscarPedido() {
        try {
            int id = Integer.parseInt(txtIdPedido.getText().trim());
            cargarPedidoPorId(id);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Escribe un ID válido.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void cargarPedidoPorId(int id) {
        Pedido p = daoPedido.buscarPorId(id);
        if (p == null) {
            JOptionPane.showMessageDialog(this, "No existe el pedido #" + id, "No encontrado", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        txtIdPedido.setText(String.valueOf(p.getIdPedido()));
        for (int i = 0; i < cmbCliente.getItemCount(); i++) {
            if (cmbCliente.getItemAt(i).id == p.getIdCliente()) { cmbCliente.setSelectedIndex(i); break; }
        }
        txtVainilla.setText(String.valueOf(p.getCantidadVainilla()));
        txtCocoa.setText(String.valueOf(p.getCantidadCocoa()));
        txtCafe.setText(String.valueOf(p.getCantidadCafe()));
        txtDireccionPedido.setText(p.getDireccionEntrega());
        txtFechaPedido.setText(p.getFechaPedido().format(FMT));
        txtFechaEntrega.setText(p.getFechaEntrega().format(FMT));
        txtTotal.setText(String.format("$%.2f", p.getTotal()));
        cmbEstadoPedido.setSelectedItem(p.getEstado());
    }

    private void guardarPedido() {
        try {
            Pedido p = new Pedido();
            try { p.setIdPedido(Integer.parseInt(txtIdPedido.getText().trim())); } catch (Exception e) { p.setIdPedido(0); }

            ClienteItem ci = (ClienteItem) cmbCliente.getSelectedItem();
            if (ci == null) throw new IllegalStateException("Selecciona un cliente.");
            p.setIdCliente(ci.id);
            p.setCantidadVainilla(parsearInt(txtVainilla.getText()));
            p.setCantidadCocoa(parsearInt(txtCocoa.getText()));
            p.setCantidadCafe(parsearInt(txtCafe.getText()));
            p.setDireccionEntrega(txtDireccionPedido.getText().trim());
            p.setFechaPedido(LocalDate.parse(txtFechaPedido.getText().trim(), FMT));
            p.setEstado(cmbEstadoPedido.getSelectedItem().toString());

            boolean ok = (p.getIdPedido() == 0) ? daoPedido.insertar(p) : daoPedido.actualizar(p);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Pedido guardado exitosamente.");
                cargarTablaPedidos();
                if (p.getIdPedido() == 0) txtIdPedido.setText(String.valueOf(p.getIdPedido()));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al guardar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarPedido() {
        try {
            int id = Integer.parseInt(txtIdPedido.getText().trim());
            if (JOptionPane.showConfirmDialog(this, "¿Eliminar pedido #" + id + "?", "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                if (daoPedido.eliminar(id)) {
                    JOptionPane.showMessageDialog(this, "Pedido eliminado.");
                    limpiarPedido();
                    cargarTablaPedidos();
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "ID de pedido inválido.");
        }
    }

    private void limpiarPedido() {
        txtIdPedido.setText("");
        txtVainilla.setText("0");
        txtCocoa.setText("0");
        txtCafe.setText("0");
        txtFechaPedido.setText(LocalDate.now().format(FMT));
        cmbEstadoPedido.setSelectedIndex(0);
        recalcularPedido();
        if (cmbCliente.getItemCount() > 0) {
            cmbCliente.setSelectedIndex(0);
            txtDireccionPedido.setText(cmbCliente.getItemAt(0).direccion);
        }
        tablaPedidos.clearSelection();
    }

    private void marcarPedidoRealizado() {
        int idPedido = obtenerIdPedidoSeleccionado();
        if (idPedido <= 0) return;
        if (daoPedido.actualizarEstado(idPedido, "Realizado")) {
            JOptionPane.showMessageDialog(this, "Pedido #" + idPedido + " marcado como REALIZADO.");
            cargarTablaPedidos();
            if (Sesion.puedeVerVentas()) refrescarVentasSegunSubTab();
        } else {
            JOptionPane.showMessageDialog(this, "Error al actualizar el estado.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void marcarPedidoEntregado() {
        int idPedido = obtenerIdPedidoSeleccionado();
        if (idPedido <= 0) return;
        if (daoPedido.actualizarEstado(idPedido, "Entregado")) {
            JOptionPane.showMessageDialog(this, "Pedido #" + idPedido + " marcado como ENTREGADO.");
            cargarTablaPedidos();
            if (Sesion.puedeVerVentas()) refrescarVentasSegunSubTab();
        } else {
            JOptionPane.showMessageDialog(this, "Error al actualizar el estado.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private int obtenerIdPedidoSeleccionado() {
        int fila = tablaPedidos.getSelectedRow();
        if (fila < 0) {
            try {
                int id = Integer.parseInt(txtIdPedido.getText().trim());
                if (id > 0) return id;
            } catch (Exception ignored) { }
            JOptionPane.showMessageDialog(this, "Selecciona un pedido de la tabla o busca uno por ID.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return -1;
        }
        return (int) modeloTablaPedidos.getValueAt(fila, 0);
    }

    // ============================================================
    // ACCIONES DE CLIENTES
    // ============================================================
    private void accionCliente(String cmd) {
        switch (cmd) {
            case "buscar":   buscarCliente();   break;
            case "nuevo":    limpiarCliente();  break;
            case "guardar":  guardarCliente();  break;
            case "eliminar": eliminarCliente(); break;
            case "limpiar":  limpiarCliente();  break;
        }
    }

    private void buscarCliente() {
        try {
            int id = Integer.parseInt(txtIdCliente.getText().trim());
            cargarClientePorId(id);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Escribe un ID válido.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void cargarClientePorId(int id) {
        Cliente c = daoCliente.buscarPorId(id);
        if (c == null) {
            JOptionPane.showMessageDialog(this, "No existe el cliente #" + id);
            return;
        }
        txtIdCliente.setText(String.valueOf(c.getIdCliente()));
        txtNombreCafeteria.setText(c.getNombreCafeteria());
        txtDireccionCliente.setText(c.getDireccion());
        txtContactoCliente.setText(c.getContacto());
    }

    private void guardarCliente() {
        try {
            Cliente c = new Cliente();
            try { c.setIdCliente(Integer.parseInt(txtIdCliente.getText().trim())); } catch (Exception e) { c.setIdCliente(0); }
            c.setNombreCafeteria(txtNombreCafeteria.getText().trim());
            c.setDireccion(txtDireccionCliente.getText().trim());
            c.setContacto(txtContactoCliente.getText().trim());

            if (c.getNombreCafeteria().isEmpty() || c.getDireccion().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nombre y dirección obligatorios.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            boolean ok = (c.getIdCliente() == 0) ? daoCliente.insertar(c) : daoCliente.actualizar(c);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Cliente guardado con éxito.");
                cargarTablaClientes();
                cargarClientesCombo();
                if (Sesion.puedeVerVentas()) cargarFiltroVentas();
                if (c.getIdCliente() == 0) txtIdCliente.setText(String.valueOf(c.getIdCliente()));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void eliminarCliente() {
        try {
            int id = Integer.parseInt(txtIdCliente.getText().trim());
            if (JOptionPane.showConfirmDialog(this, "¿Eliminar cliente #" + id + "?", "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                if (daoCliente.eliminar(id)) {
                    JOptionPane.showMessageDialog(this, "Cliente eliminado.");
                    limpiarCliente();
                    cargarTablaClientes();
                    cargarClientesCombo();
                    if (Sesion.puedeVerVentas()) cargarFiltroVentas();
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo eliminar (puede tener pedidos asociados).");
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "ID de cliente inválido.");
        }
    }

    private void limpiarCliente() {
        txtIdCliente.setText("");
        txtNombreCafeteria.setText("");
        txtDireccionCliente.setText("");
        txtContactoCliente.setText("");
        tablaClientes.clearSelection();
    }

    // ============================================================
    // ACCIONES DE INSUMOS
    // ============================================================
    private void accionInsProd(String cmd) {
        switch (cmd) {
            case "buscar":   buscarInsProd();   break;
            case "nuevo":    limpiarInsProd();  break;
            case "guardar":  guardarInsProd();  break;
            case "eliminar": eliminarInsProd(); break;
            case "limpiar":  limpiarInsProd();  break;
        }
    }

    private void buscarInsProd() {
        try {
            int id = Integer.parseInt(txtIdInsProd.getText().trim());
            cargarInsProdPorId(id);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Escribe un ID válido.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void cargarInsProdPorId(int id) {
        InsumoProduccion i = daoInsProd.buscarPorId(id);
        if (i == null) {
            JOptionPane.showMessageDialog(this, "No existe el insumo #" + id, "No encontrado", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        txtIdInsProd.setText(String.valueOf(i.getIdInsumo()));
        txtNomInsProd.setText(i.getNombreInsumo());
        txtCantInsProd.setText(String.valueOf(i.getCantidad()));
        cmbUnidadInsProd.setSelectedItem(i.getUnidad());
        txtMinInsProd.setText(String.valueOf(i.getStockMinimo()));
    }

    private void guardarInsProd() {
        try {
            InsumoProduccion i = new InsumoProduccion();
            try { i.setIdInsumo(Integer.parseInt(txtIdInsProd.getText().trim())); } catch (Exception e) { i.setIdInsumo(0); }
            i.setNombreInsumo(txtNomInsProd.getText().trim());
            i.setCantidad(parsearDouble(txtCantInsProd.getText()));
            i.setUnidad(cmbUnidadInsProd.getSelectedItem().toString());
            i.setStockMinimo(parsearDouble(txtMinInsProd.getText()));

            if (i.getNombreInsumo().isEmpty()) {
                JOptionPane.showMessageDialog(this, "El nombre es obligatorio.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (i.getCantidad() < 0 || i.getStockMinimo() < 0) {
                JOptionPane.showMessageDialog(this, "Las cantidades no pueden ser negativas.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            boolean ok = (i.getIdInsumo() == 0) ? daoInsProd.insertar(i) : daoInsProd.actualizar(i);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Insumo guardado con éxito.");
                cargarTablaInsProd();
                if (i.getIdInsumo() == 0) txtIdInsProd.setText(String.valueOf(i.getIdInsumo()));
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo guardar el insumo.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarInsProd() {
        try {
            int id = Integer.parseInt(txtIdInsProd.getText().trim());
            if (JOptionPane.showConfirmDialog(this, "¿Eliminar insumo #" + id + "?", "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                if (daoInsProd.eliminar(id)) {
                    JOptionPane.showMessageDialog(this, "Insumo eliminado.");
                    limpiarInsProd();
                    cargarTablaInsProd();
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo eliminar (puede estar en uso en recetas o lotes).");
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "ID inválido.");
        }
    }

    private void limpiarInsProd() {
        txtIdInsProd.setText("");
        txtNomInsProd.setText("");
        txtCantInsProd.setText("0");
        cmbUnidadInsProd.setSelectedIndex(0);
        txtMinInsProd.setText("0");
        tablaInsProd.clearSelection();
    }

    private void accionInsEmp(String cmd) {
        switch (cmd) {
            case "buscar":   buscarInsEmp();   break;
            case "nuevo":    limpiarInsEmp();  break;
            case "guardar":  guardarInsEmp();  break;
            case "eliminar": eliminarInsEmp(); break;
            case "limpiar":  limpiarInsEmp();  break;
        }
    }

    private void buscarInsEmp() {
        try {
            int id = Integer.parseInt(txtIdInsEmp.getText().trim());
            cargarInsEmpPorId(id);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Escribe un ID válido.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void cargarInsEmpPorId(int id) {
        InsumoEmpaquetado i = daoInsEmp.buscarPorId(id);
        if (i == null) {
            JOptionPane.showMessageDialog(this, "No existe el insumo #" + id, "No encontrado", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        txtIdInsEmp.setText(String.valueOf(i.getIdInsumoEmpaque()));
        txtNomInsEmp.setText(i.getNombreInsumo());
        txtCantInsEmp.setText(String.valueOf(i.getCantidad()));
        cmbUnidadInsEmp.setSelectedItem(i.getUnidad());
        txtMinInsEmp.setText(String.valueOf(i.getStockMinimo()));
    }

    private void guardarInsEmp() {
        try {
            InsumoEmpaquetado i = new InsumoEmpaquetado();
            try { i.setIdInsumoEmpaque(Integer.parseInt(txtIdInsEmp.getText().trim())); } catch (Exception e) { i.setIdInsumoEmpaque(0); }
            i.setNombreInsumo(txtNomInsEmp.getText().trim());
            i.setCantidad(parsearInt(txtCantInsEmp.getText()));
            i.setUnidad(cmbUnidadInsEmp.getSelectedItem().toString());
            i.setStockMinimo(parsearInt(txtMinInsEmp.getText()));

            if (i.getNombreInsumo().isEmpty()) {
                JOptionPane.showMessageDialog(this, "El nombre es obligatorio.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (i.getCantidad() < 0 || i.getStockMinimo() < 0) {
                JOptionPane.showMessageDialog(this, "Las cantidades no pueden ser negativas.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            boolean ok = (i.getIdInsumoEmpaque() == 0) ? daoInsEmp.insertar(i) : daoInsEmp.actualizar(i);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Insumo guardado con éxito.");
                cargarTablaInsEmp();
                if (i.getIdInsumoEmpaque() == 0) txtIdInsEmp.setText(String.valueOf(i.getIdInsumoEmpaque()));
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo guardar el insumo.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarInsEmp() {
        try {
            int id = Integer.parseInt(txtIdInsEmp.getText().trim());
            if (JOptionPane.showConfirmDialog(this, "¿Eliminar insumo #" + id + "?", "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                if (daoInsEmp.eliminar(id)) {
                    JOptionPane.showMessageDialog(this, "Insumo eliminado.");
                    limpiarInsEmp();
                    cargarTablaInsEmp();
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo eliminar el insumo.");
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "ID inválido.");
        }
    }

    private void limpiarInsEmp() {
        txtIdInsEmp.setText("");
        txtNomInsEmp.setText("");
        txtCantInsEmp.setText("0");
        cmbUnidadInsEmp.setSelectedIndex(0);
        txtMinInsEmp.setText("0");
        tablaInsEmp.clearSelection();
    }

    // ============================================================
    // AUXILIARES DE UI
    // ============================================================
    private JTextField crearCampo(int cols) {
        JTextField tf = new JTextField(cols);
        tf.setFont(F_CAMPO);
        tf.setBackground(COLOR_BLANCO);
        tf.setForeground(COLOR_MARRON);
        tf.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(COLOR_DORADO, 1, true), new EmptyBorder(4, 6, 4, 6)));
        return tf;
    }

    private JLabel etiqueta(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(F_ETIQ);
        l.setForeground(COLOR_MARRON);
        return l;
    }

    private JButton boton(String texto, Color fondo, Color textoColor, java.awt.event.ActionListener accion) {
        JButton b = new JButton(texto);
        b.setFont(F_BOTON);
        b.setBackground(fondo);
        b.setForeground(textoColor);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(COLOR_MARRON, 1, true), new EmptyBorder(6, 14, 6, 14)));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setUI(new BasicButtonUI());
        b.addActionListener(accion);
        return b;
    }

    private JComboBox<ClienteItem> crearComboCliente(int width) {
        JComboBox<ClienteItem> cb = new JComboBox<>();
        cb.setFont(F_CAMPO);
        cb.setBackground(COLOR_BLANCO);
        cb.setForeground(COLOR_MARRON);
        cb.setPreferredSize(new Dimension(width, 30));
        cb.setBorder(new LineBorder(COLOR_DORADO, 1, true));
        cb.setUI(new BasicComboBoxUI());
        return cb;
    }

    private JComboBox<String> crearComboEstado(int width) {
        JComboBox<String> cb = new JComboBox<>(new String[]{"Pendiente", "Realizado", "Entregado"});
        cb.setFont(F_CAMPO);
        cb.setBackground(COLOR_BLANCO);
        cb.setForeground(COLOR_MARRON);
        cb.setPreferredSize(new Dimension(width, 25));
        cb.setBorder(new LineBorder(COLOR_DORADO, 1, true));
        cb.setUI(new BasicComboBoxUI());
        return cb;
    }

    private JComboBox<String> crearComboUnidadProd(int width) {
        JComboBox<String> cb = new JComboBox<>(new String[]{"kg", "g", "ml", "unidad"});
        cb.setFont(F_CAMPO);
        cb.setBackground(COLOR_BLANCO);
        cb.setForeground(COLOR_MARRON);
        cb.setPreferredSize(new Dimension(width, 25));
        cb.setBorder(new LineBorder(COLOR_DORADO, 1, true));
        cb.setUI(new BasicComboBoxUI());
        return cb;
    }

    private JComboBox<String> crearComboUnidadEmp(int width) {
        JComboBox<String> cb = new JComboBox<>(new String[]{"unidad", "paquete", "rollo"});
        cb.setFont(F_CAMPO);
        cb.setBackground(COLOR_BLANCO);
        cb.setForeground(COLOR_MARRON);
        cb.setPreferredSize(new Dimension(width, 25));
        cb.setBorder(new LineBorder(COLOR_DORADO, 1, true));
        cb.setUI(new BasicComboBoxUI());
        return cb;
    }

    private void configurarEstiloTabla(JTable tabla) {
        tabla.setFont(F_TABLA);
        tabla.setRowHeight(24);
        tabla.setBackground(COLOR_BLANCO);
        tabla.setForeground(COLOR_MARRON);
        tabla.setGridColor(COLOR_CREMA);
        tabla.setSelectionBackground(COLOR_DORADO);
        tabla.setSelectionForeground(Color.WHITE);

        // --- SOLUCIÓN AL ENCABEZADO EN BLANCO ---
        JTableHeader header = tabla.getTableHeader();
        header.setBackground(COLOR_MARRON);
        header.setForeground(Color.WHITE);
        header.setFont(F_ETIQ);
        header.setOpaque(true);

        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer() {
            private static final long serialVersionUID = 1L;
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(COLOR_MARRON);
                c.setForeground(Color.WHITE);
                setFont(F_ETIQ);
                setHorizontalAlignment(JLabel.CENTER);
                return c;
            }
        };
        header.setDefaultRenderer(headerRenderer);
    }

    private static class ClienteItem {
        final int id;
        final String nombre;
        final String direccion;

        ClienteItem(int id, String nombre, String direccion) {
            this.id = id;
            this.nombre = nombre;
            this.direccion = direccion;
        }

        @Override public String toString() { return nombre; }
    }
}