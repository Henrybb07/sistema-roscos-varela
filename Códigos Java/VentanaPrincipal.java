package poo.roscos;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
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
import javax.swing.table.DefaultTableModel;

public class VentanaPrincipal extends JFrame {

	private static final long serialVersionUID = 1L;

	private static final Color COLOR_FONDO  = new Color(0xFA, 0xF3, 0xE8);
	private static final Color COLOR_MARRON = new Color(0x5C, 0x3A, 0x1E);
	private static final Color COLOR_DORADO = new Color(0xC9, 0xA6, 0x6B);
	private static final Color COLOR_CREMA  = new Color(0xF5, 0xE6, 0xD3);
	private static final Color COLOR_VERDE  = new Color(0x4F, 0x79, 0x42);
	private static final Color COLOR_ROJO   = new Color(0xA9, 0x32, 0x26);
	private static final Color COLOR_BLANCO = new Color(0xFF, 0xFD, 0xF7);
    
	private static final Font F_TITULO = new Font("Serif",     Font.BOLD,  26);
	private static final Font F_SUBTIT = new Font("Serif",     Font.ITALIC,14);
	private static final Font F_ETIQ   = new Font("SansSerif", Font.BOLD,  13);
	private static final Font F_CAMPO  = new Font("SansSerif", Font.PLAIN, 14);
	private static final Font F_BOTON  = new Font("SansSerif", Font.BOLD,  13);
	private static final Font F_TABLA  = new Font("SansSerif", Font.PLAIN, 13);

	private JPanel contentPane;
	private JTabbedPane tabbedPane;

	// Componentes Pestaña PEDIDOS
	private JTextField txtIdPedido;
	private JComboBox<ClienteItem> cmbCliente;
	private JTextField txtVainilla, txtCocoa, txtCafe;
	private JTextField txtDireccionPedido;
	private JTextField txtFechaPedido, txtFechaEntrega;
	private JTextField txtTotal;
	private JComboBox<String> cmbEstadoPedido;
	private JTable tablaPedidos;
	private DefaultTableModel modeloTablaPedidos;

	// Componentes Pestaña CLIENTES
	private JTextField txtIdCliente, txtNombreCafeteria, txtDireccionCliente, txtContactoCliente;
	private JTable tablaClientes;
	private DefaultTableModel modeloTablaClientes;

	// Componentes Pestaña PRODUCCIÓN (NUEVO)
	private JTable tablaProduccion;
	private DefaultTableModel modeloTablaProduccion;

	private final PedidoDAO daoPedido = new PedidoDAO();
	private final ClienteDAO daoCliente = new ClienteDAO();
	private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				VentanaPrincipal frame = new VentanaPrincipal();
				frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public VentanaPrincipal() {
		setTitle("Roscos Varela - Sistema de Gestión Integral");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1120, 780);
		setLocationRelativeTo(null);

		contentPane = new JPanel(new BorderLayout(15, 15));
		contentPane.setBackground(COLOR_FONDO);
		contentPane.setBorder(new EmptyBorder(15, 20, 15, 20));
		setContentPane(contentPane);

		contentPane.add(crearEncabezado(), BorderLayout.NORTH);

		// Menú General por Pestañas
		tabbedPane = new JTabbedPane();
		tabbedPane.setFont(F_ETIQ);
		tabbedPane.setBackground(COLOR_CREMA);
		tabbedPane.setForeground(COLOR_MARRON);

		tabbedPane.addTab("Gestión de Pedidos", crearPanelPedidos());
		tabbedPane.addTab("Gestión de Clientes", crearPanelClientes());
		tabbedPane.addTab("Gestión de Producción", crearPanelProduccion());

		contentPane.add(tabbedPane, BorderLayout.CENTER);

		cargarClientesCombo();
		cargarTablaPedidos();
		cargarTablaClientes();
		cargarTablaProduccion();
		configurarEventos();
	}

	private JPanel crearEncabezado() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBackground(COLOR_FONDO);

		JLabel titulo = new JLabel("Roscos Varela");
		titulo.setFont(F_TITULO);
		titulo.setForeground(COLOR_MARRON);
		titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

		JLabel subtitulo = new JLabel("Control de Ventas, Clientes y Producción");
		subtitulo.setFont(F_SUBTIT);
		subtitulo.setForeground(COLOR_DORADO);
		subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

		JSeparator linea = new JSeparator();
		linea.setForeground(COLOR_DORADO);
		linea.setMaximumSize(new Dimension(Integer.MAX_VALUE, 2));

		panel.add(titulo);
		panel.add(subtitulo);
		panel.add(Box.createVerticalStrut(6));
		panel.add(linea);
		return panel;
	}

	// PESTAÑA 1: PEDIDOS
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

		txtIdPedido = crearCampo(6);
		cmbCliente = new JComboBox<>();
		cmbCliente.setFont(F_CAMPO);
		cmbCliente.setBackground(COLOR_BLANCO);
		cmbCliente.setPreferredSize(new Dimension(260, 30));

		g.gridx = 0; g.gridy = 0; panel.add(etiqueta("ID Pedido:"), g);
		g.gridx = 1; panel.add(txtIdPedido, g);
		g.gridx = 2; panel.add(etiqueta("Cliente:"), g);
		g.gridx = 3; panel.add(cmbCliente, g);

		// Cantidades
		txtVainilla = crearCampo(5); txtVainilla.setText("0");
		txtCocoa    = crearCampo(5); txtCocoa.setText("0");
		txtCafe     = crearCampo(5); txtCafe.setText("0");

		JPanel panelCant = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
		panelCant.setBackground(COLOR_FONDO);
		panelCant.add(etiqueta("Vainilla:")); panelCant.add(txtVainilla);
		panelCant.add(etiqueta("Cocoa:")); panelCant.add(txtCocoa);
		panelCant.add(etiqueta("Café:")); panelCant.add(txtCafe);

		g.gridx = 0; g.gridy = 1; panel.add(etiqueta("Cantidades:"), g);
		g.gridx = 1; g.gridwidth = 3; panel.add(panelCant, g);
		g.gridwidth = 1;

		// Dirección
		txtDireccionPedido = crearCampo(45);
		txtDireccionPedido.setEditable(false);
		txtDireccionPedido.setBackground(COLOR_CREMA);

		g.gridx = 0; g.gridy = 2; panel.add(etiqueta("Dirección:"), g);
		g.gridx = 1; g.gridwidth = 3; panel.add(txtDireccionPedido, g);
		g.gridwidth = 1;

		// Fechas y Estado
		txtFechaPedido  = crearCampo(12);
		txtFechaEntrega = crearCampo(12);
		txtFechaEntrega.setEditable(false);
		txtFechaEntrega.setBackground(COLOR_CREMA);

		cmbEstadoPedido = new JComboBox<>(new String[]{"Pendiente", "Realizado", "Entregado"});
		cmbEstadoPedido.setFont(F_CAMPO);
		cmbEstadoPedido.setBackground(COLOR_BLANCO);

		JPanel panelFechas = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
		panelFechas.setBackground(COLOR_FONDO);
		panelFechas.add(etiqueta("Pedido:")); panelFechas.add(txtFechaPedido);
		panelFechas.add(etiqueta("Entrega:")); panelFechas.add(txtFechaEntrega);
		panelFechas.add(etiqueta("Estado:")); panelFechas.add(cmbEstadoPedido);

		g.gridx = 0; g.gridy = 3; panel.add(etiqueta("Detalles:"), g);
		g.gridx = 1; g.gridwidth = 3; panel.add(panelFechas, g);
		g.gridwidth = 1;

		// Total
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

		// Botones
		JPanel pBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 8));
		pBotones.setBackground(COLOR_FONDO);
		pBotones.add(boton("Buscar",   COLOR_DORADO, Color.WHITE, e -> accionPedido("buscar")));
		pBotones.add(boton("Nuevo",    COLOR_MARRON, Color.WHITE, e -> accionPedido("nuevo")));
		pBotones.add(boton("Guardar",  COLOR_VERDE,  Color.WHITE, e -> accionPedido("guardar")));
		pBotones.add(boton("Eliminar", COLOR_ROJO,   Color.WHITE, e -> accionPedido("eliminar")));
		pBotones.add(boton("Limpiar",  COLOR_CREMA,  COLOR_MARRON, e -> accionPedido("limpiar")));

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
		return scroll;
	}


	// PESTAÑA 2: CLIENTES
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

		txtIdCliente = crearCampo(6);
		txtNombreCafeteria = crearCampo(30);
		txtDireccionCliente = crearCampo(45);
		txtContactoCliente = crearCampo(25);

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
		return scroll;
	}



	// PESTAÑA 3: PRODUCCIÓN
	private JPanel crearPanelProduccion() {
		JPanel panel = new JPanel(new BorderLayout(10, 10));
		panel.setBackground(COLOR_FONDO);
		panel.setBorder(new EmptyBorder(10, 10, 10, 10));

		// Panel superior con instrucciones y botón de acción rápida
		JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
		panelSuperior.setBackground(COLOR_FONDO);
		
		JLabel lblInfo = new JLabel("Pedidos pendientes de elaboración y surtido en planta:");
		lblInfo.setFont(F_ETIQ);
		lblInfo.setForeground(COLOR_MARRON);

		JButton btnMarcarRealizado = boton("Marcar como Realizado", COLOR_VERDE, Color.WHITE, e -> marcarPedidoRealizado());
		JButton btnMarcarEntregado = boton("Marcar como Entregado", COLOR_DORADO, Color.WHITE, e -> marcarPedidoEntregado());
		JButton btnActualizarProd  = boton("Actualizar Lista", COLOR_MARRON, Color.WHITE, e -> cargarTablaProduccion());

		panelSuperior.add(lblInfo);
		panelSuperior.add(btnMarcarRealizado);
		panelSuperior.add(btnMarcarEntregado);
		panelSuperior.add(btnActualizarProd);

		panel.add(panelSuperior, BorderLayout.NORTH);
		panel.add(crearTablaProduccionUI(), BorderLayout.CENTER);

		return panel;
	}

	private JScrollPane crearTablaProduccionUI() {
		String[] cols = {"ID Pedido", "Cliente", "Vainilla", "Cocoa", "Café", "F. Entrega", "Estado Actual"};
		modeloTablaProduccion = new DefaultTableModel(cols, 0) {
			private static final long serialVersionUID = 1L;
			@Override public boolean isCellEditable(int r, int c) { return false; }
		};

		tablaProduccion = new JTable(modeloTablaProduccion);
		configurarEstiloTabla(tablaProduccion);

		tablaProduccion.getColumnModel().getColumn(0).setPreferredWidth(70);
		tablaProduccion.getColumnModel().getColumn(1).setPreferredWidth(220);
		tablaProduccion.getColumnModel().getColumn(2).setPreferredWidth(70);
		tablaProduccion.getColumnModel().getColumn(3).setPreferredWidth(70);
		tablaProduccion.getColumnModel().getColumn(4).setPreferredWidth(70);
		tablaProduccion.getColumnModel().getColumn(5).setPreferredWidth(120);
		tablaProduccion.getColumnModel().getColumn(6).setPreferredWidth(120);

		JScrollPane scroll = new JScrollPane(tablaProduccion);
		scroll.setBorder(new LineBorder(COLOR_DORADO, 1, true));
		scroll.setPreferredSize(new Dimension(1040, 400));
		return scroll;
	}


	// LOGICA PARA CARGA DE DATOS
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
		List<Pedido> lista = daoPedido.listar();
		for (Pedido p : lista) {
			modeloTablaPedidos.addRow(new Object[]{
					p.getIdPedido(), nombreCliente(p.getIdCliente()), p.getCantidadVainilla(), 
					p.getCantidadCocoa(), p.getCantidadCafe(), p.getDireccionEntrega(), 
					p.getFechaPedido(), p.getFechaEntrega(), String.format("$%.2f", p.getTotal()), p.getEstado()
			});
		}
	}

	private void cargarTablaClientes() {
		modeloTablaClientes.setRowCount(0);
		List<Cliente> lista = daoCliente.listar();
		for (Cliente c : lista) {
			modeloTablaClientes.addRow(new Object[]{
					c.getIdCliente(), c.getNombreCafeteria(), c.getDireccion(), c.getContacto()
			});
		}
	}

	private void cargarTablaProduccion() {
		modeloTablaProduccion.setRowCount(0);
		List<Pedido> lista = daoPedido.listar();
		for (Pedido p : lista) {
			modeloTablaProduccion.addRow(new Object[]{
					p.getIdPedido(), nombreCliente(p.getIdCliente()), p.getCantidadVainilla(), 
					p.getCantidadCocoa(), p.getCantidadCafe(), p.getFechaEntrega(), p.getEstado()
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
		
		tablaClientes.getSelectionModel().addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting() && tablaClientes.getSelectedRow() >= 0) {
				int id = (int) modeloTablaClientes.getValueAt(tablaClientes.getSelectedRow(), 0);
				cargarClientePorId(id);
			}
		});
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


	// ACCIONES DE PEDIDOS
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
				cargarTablaProduccion();
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
					cargarTablaProduccion();
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


	// ACCIONES DE CLIENTES
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


	// ACCIONES DE PRODUCCIÓN
	private void marcarPedidoRealizado() {
		int fila = tablaProduccion.getSelectedRow();
		if (fila < 0) {
			JOptionPane.showMessageDialog(this, "Selecciona un pedido de la tabla de producción.", "Aviso", JOptionPane.WARNING_MESSAGE);
			return;
		}
		int idPedido = (int) modeloTablaProduccion.getValueAt(fila, 0);
		if (daoPedido.actualizarEstado(idPedido, "Realizado")) {
			JOptionPane.showMessageDialog(this, "Pedido #" + idPedido + " marcado como REALIZADO.");
			cargarTablaProduccion();
			cargarTablaPedidos();
		} else {
			JOptionPane.showMessageDialog(this, "Error al actualizar el estado.", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void marcarPedidoEntregado() {
		int fila = tablaProduccion.getSelectedRow();
		if (fila < 0) {
			JOptionPane.showMessageDialog(this, "Selecciona un pedido de la tabla de producción.", "Aviso", JOptionPane.WARNING_MESSAGE);
			return;
		}
		int idPedido = (int) modeloTablaProduccion.getValueAt(fila, 0);
		if (daoPedido.actualizarEstado(idPedido, "Entregado")) {
			JOptionPane.showMessageDialog(this, "Pedido #" + idPedido + " marcado como ENTREGADO.");
			cargarTablaProduccion();
			cargarTablaPedidos();
		} else {
			JOptionPane.showMessageDialog(this, "Error al actualizar el estado.", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}


	// MÉTODOS AUXILIARES DE UI
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
		b.addActionListener(accion);
		return b;
	}

	private void configurarEstiloTabla(JTable tabla) {
		tabla.setFont(F_TABLA);
		tabla.setRowHeight(24);
		tabla.setBackground(COLOR_BLANCO);
		tabla.setForeground(COLOR_MARRON);
		tabla.setGridColor(COLOR_CREMA);
		tabla.getTableHeader().setBackground(COLOR_MARRON);
		tabla.getTableHeader().setForeground(Color.WHITE);
		tabla.getTableHeader().setFont(F_ETIQ);
		tabla.setSelectionBackground(COLOR_DORADO);
		tabla.setSelectionForeground(Color.WHITE);
	}

	private static class ClienteItem {
		final int id;
		final String nombre;
		final String direccion;
		
		ClienteItem(int id, String nombre, String direccion) { 
			this.id = id; this.nombre = nombre; this.direccion = direccion; 
		}
		
		@Override public String toString() { return nombre; }
	}
}