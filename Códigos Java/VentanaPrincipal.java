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
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
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
    private static final Font F_TITULO = new Font("Serif",     Font.BOLD,  28);
    private static final Font F_SUBTIT = new Font("Serif",     Font.ITALIC,15);
    private static final Font F_ETIQ   = new Font("SansSerif", Font.BOLD,  13);
    private static final Font F_CAMPO  = new Font("SansSerif", Font.PLAIN, 14);
    private static final Font F_BOTON  = new Font("SansSerif", Font.BOLD,  13);
    private static final Font F_TABLA  = new Font("SansSerif", Font.PLAIN, 13);

	private JPanel contentPane;
	private JTextField txtId;
	private JComboBox<ClienteItem> cmbCliente;
	private JTextField txtVainilla, txtCocoa, txtCafe;
	private JTextField txtDireccion;
	private JTextField txtFechaPedido, txtFechaEntrega;
	private JTextField txtTotal;
	private JTable tabla;
	private DefaultTableModel modeloTabla;

	private final PedidoDAO dao = new PedidoDAO();
	private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VentanaPrincipal frame = new VentanaPrincipal();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public VentanaPrincipal() {
		setTitle("Roscos Varela - Gestión de Pedidos");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1050, 720);
		setLocationRelativeTo(null);

		contentPane = new JPanel(new BorderLayout(15, 15));
		contentPane.setBackground(COLOR_FONDO);
		contentPane.setBorder(new EmptyBorder(15, 20, 15, 20));
		setContentPane(contentPane);

		contentPane.add(crearEncabezado(),  BorderLayout.NORTH);
		contentPane.add(crearFormulario(),  BorderLayout.CENTER);
		contentPane.add(crearTabla(),       BorderLayout.SOUTH);

		cargarClientes();
		cargarTabla();
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

		JLabel subtitulo = new JLabel("Sistema de Gestión de Pedidos");
		subtitulo.setFont(F_SUBTIT);
		subtitulo.setForeground(COLOR_DORADO);
		subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

		JSeparator linea = new JSeparator();
		linea.setForeground(COLOR_DORADO);
		linea.setMaximumSize(new Dimension(Integer.MAX_VALUE, 2));

		panel.add(titulo);
		panel.add(subtitulo);
		panel.add(Box.createVerticalStrut(8));
		panel.add(linea);
		return panel;
	}

	private JPanel crearFormulario() {
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBackground(COLOR_FONDO);
		GridBagConstraints g = new GridBagConstraints();
		g.insets = new Insets(6, 8, 6, 8);
		g.anchor = GridBagConstraints.WEST;

		txtId = crearCampo(6);
		cmbCliente = new JComboBox<>();
		cmbCliente.setFont(F_CAMPO);
		cmbCliente.setBackground(COLOR_BLANCO);
		cmbCliente.setPreferredSize(new Dimension(260, 30));

		g.gridx = 0; g.gridy = 0; panel.add(etiqueta("ID Pedido:"), g);
		g.gridx = 1; panel.add(txtId, g);
		g.gridx = 2; panel.add(etiqueta("Cliente:"), g);
		g.gridx = 3; panel.add(cmbCliente, g);

		// -------- Fila 1: Cantidades --------
		txtVainilla = crearCampo(5);
		txtCocoa    = crearCampo(5);
		txtCafe     = crearCampo(5);
		txtVainilla.setText("0");
		txtCocoa.setText("0");
		txtCafe.setText("0");

		JPanel panelCantidades = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
		panelCantidades.setBackground(COLOR_FONDO);
		panelCantidades.add(etiqueta("Vainilla:"));
		panelCantidades.add(txtVainilla);
		panelCantidades.add(etiqueta("Cocoa:"));
		panelCantidades.add(txtCocoa);
		panelCantidades.add(etiqueta("Café:"));
		panelCantidades.add(txtCafe);

		g.gridx = 0; g.gridy = 1; panel.add(etiqueta("Cantidades:"), g);
		g.gridx = 1; g.gridwidth = 3; panel.add(panelCantidades, g);
		g.gridwidth = 1;

		// -------- Fila 2: Dirección --------
		txtDireccion = crearCampo(40);
		g.gridx = 0; g.gridy = 2; panel.add(etiqueta("Dirección:"), g);
		g.gridx = 1; g.gridwidth = 3; panel.add(txtDireccion, g);
		g.gridwidth = 1;

		// -------- Fila 3: Fechas --------
		txtFechaPedido  = crearCampo(12);
		txtFechaEntrega = crearCampo(12);
		txtFechaEntrega.setEditable(false);
		txtFechaEntrega.setBackground(COLOR_CREMA);

		JPanel panelFechas = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
		panelFechas.setBackground(COLOR_FONDO);
		panelFechas.add(etiqueta("Pedido:"));
		panelFechas.add(txtFechaPedido);
		panelFechas.add(etiqueta("Entrega (+4 días):"));
		panelFechas.add(txtFechaEntrega);

		g.gridx = 0; g.gridy = 3; panel.add(etiqueta("Fechas:"), g);
		g.gridx = 1; g.gridwidth = 3; panel.add(panelFechas, g);
		g.gridwidth = 1;

		// -------- Fila 4: Total --------
		txtTotal = crearCampo(10);
		txtTotal.setEditable(false);
		txtTotal.setFont(new Font("SansSerif", Font.BOLD, 16));
		txtTotal.setForeground(COLOR_MARRON);
		txtTotal.setBackground(COLOR_CREMA);

		g.gridx = 0; g.gridy = 4; panel.add(etiqueta("Total:"), g);
		g.gridx = 1; panel.add(txtTotal, g);
		JLabel lblPrecio = new JLabel("  (cada paquete a $25.00)");
		lblPrecio.setFont(F_SUBTIT);
		lblPrecio.setForeground(COLOR_DORADO);
		g.gridx = 2; g.gridwidth = 2; panel.add(lblPrecio, g);
		g.gridwidth = 1;

		// -------- Fila 5: Botones --------
		JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 12));
		panelBotones.setBackground(COLOR_FONDO);

		panelBotones.add(boton("Buscar",   COLOR_DORADO, Color.WHITE, "buscar"));
		panelBotones.add(boton("Nuevo",    COLOR_MARRON, Color.WHITE, "nuevo"));
		panelBotones.add(boton("Guardar",  COLOR_VERDE,  Color.WHITE, "guardar"));
		panelBotones.add(boton("Eliminar", COLOR_ROJO,   Color.WHITE, "eliminar"));
		panelBotones.add(boton("Limpiar",  COLOR_CREMA,  COLOR_MARRON, "limpiar"));

		g.gridx = 0; g.gridy = 5; g.gridwidth = 4;
		g.fill = GridBagConstraints.HORIZONTAL;
		panel.add(panelBotones, g);

		return panel;
	}


    // TABLA
	private JScrollPane crearTabla() {
		String[] columnas = {"ID", "Cliente", "Vainilla", "Cocoa", "Café", "Dirección", "F. Pedido", "F. Entrega", "Total"};
		modeloTabla = new DefaultTableModel(columnas, 0) {
			private static final long serialVersionUID = 1L;
			@Override public boolean isCellEditable(int r, int c) { return false; }
		};

		tabla = new JTable(modeloTabla);
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

		JScrollPane scroll = new JScrollPane(tabla);
		scroll.setBorder(new LineBorder(COLOR_DORADO, 1, true));
		scroll.setPreferredSize(new Dimension(1000, 260));
		return scroll;
	}


	// CARGA DE DATOS
	private void cargarClientes() {
		DefaultComboBoxModel<ClienteItem> modelo = new DefaultComboBoxModel<>();
		for (String[] c : dao.listarClientes()) {
			modelo.addElement(new ClienteItem(Integer.parseInt(c[0]), c[1]));
		}
		cmbCliente.setModel(modelo);
	}

	private void cargarTabla() {
		modeloTabla.setRowCount(0);
		List<Pedido> lista = dao.listar();
		for (Pedido p : lista) {
			modeloTabla.addRow(new Object[]{
					p.getIdPedido(), nombreCliente(p.getIdCliente()), p.getCantidadVainilla(), p.getCantidadCocoa(), p.getCantidadCafe(),p.getDireccionEntrega(), p.getFechaPedido(), p.getFechaEntrega(), String.format("$%.2f", p.getTotal())
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

	// EVENTOS
	private void configurarEventos() {
		DocumentListener dl = new DocumentListener() {
			public void insertUpdate(DocumentEvent e) { recalcular(); }
			public void removeUpdate(DocumentEvent e) { recalcular(); }
			public void changedUpdate(DocumentEvent e) { recalcular(); }
		};
		txtVainilla.getDocument().addDocumentListener(dl);
		txtCocoa.getDocument().addDocumentListener(dl);
		txtCafe.getDocument().addDocumentListener(dl);
		txtFechaPedido.getDocument().addDocumentListener(dl);

		tabla.getSelectionModel().addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting() && tabla.getSelectedRow() >= 0) {
				int id = (int) modeloTabla.getValueAt(tabla.getSelectedRow(), 0);
				cargarPedido(id);
			}
		});
	}

	private void recalcular() {
		try {
			int v = parsear(txtVainilla.getText());
			int c = parsear(txtCocoa.getText());
			int f = parsear(txtCafe.getText());
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

	private int parsear(String s) {
		if (s == null || s.trim().isEmpty()) return 0;
		return Integer.parseInt(s.trim());
	}


    // ACCIONES
	private void accion(String cmd) {
		switch (cmd) {
			case "buscar":   buscar();   break;
			case "nuevo":    limpiar();  break;
			case "guardar":  guardar();  break;
			case "eliminar": eliminar(); break;
			case "limpiar":  limpiar();  break;
		}
	}

	private void buscar() {
		try {
			int id = Integer.parseInt(txtId.getText().trim());
			cargarPedido(id);
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this, "Escribe un ID válido.", "Error", JOptionPane.WARNING_MESSAGE);
		}
	}

	private void cargarPedido(int id) {
		Pedido p = dao.buscarPorId(id);
		if (p == null) {
			JOptionPane.showMessageDialog(this, "No existe el pedido #" + id, "No encontrado", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		txtId.setText(String.valueOf(p.getIdPedido()));
		for (int i = 0; i < cmbCliente.getItemCount(); i++) {
			if (cmbCliente.getItemAt(i).id == p.getIdCliente()) {
				cmbCliente.setSelectedIndex(i); break;
			}
		}
		txtVainilla.setText(String.valueOf(p.getCantidadVainilla()));
		txtCocoa.setText(String.valueOf(p.getCantidadCocoa()));
		txtCafe.setText(String.valueOf(p.getCantidadCafe()));
		txtDireccion.setText(p.getDireccionEntrega());
		txtFechaPedido.setText(p.getFechaPedido().format(FMT));
		txtFechaEntrega.setText(p.getFechaEntrega().format(FMT));
		txtTotal.setText(String.format("$%.2f", p.getTotal()));
	}

	private void guardar() {
		try {
			Pedido p = leerFormulario();
			boolean ok;
			if (p.getIdPedido() == 0) {
				ok = dao.insertar(p);
				if (ok) {
					txtId.setText(String.valueOf(p.getIdPedido()));
					JOptionPane.showMessageDialog(this, "Pedido insertado con ID " + p.getIdPedido());
				}
			} else {
				ok = dao.actualizar(p);
				if (ok) JOptionPane.showMessageDialog(this, "Pedido #" + p.getIdPedido() + " actualizado.");
			}
			if (ok) cargarTabla();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private Pedido leerFormulario() {
		Pedido p = new Pedido();
		try { p.setIdPedido(Integer.parseInt(txtId.getText().trim())); }
		catch (Exception e) { p.setIdPedido(0); }

		ClienteItem ci = (ClienteItem) cmbCliente.getSelectedItem();
		if (ci == null) throw new IllegalStateException("Selecciona un cliente.");
		p.setIdCliente(ci.id);

		p.setCantidadVainilla(parsear(txtVainilla.getText()));
		p.setCantidadCocoa(parsear(txtCocoa.getText()));
		p.setCantidadCafe(parsear(txtCafe.getText()));
		p.setDireccionEntrega(txtDireccion.getText().trim());
		p.setFechaPedido(LocalDate.parse(txtFechaPedido.getText().trim(), FMT));
		return p;
	}

	private void eliminar() {
		try {
			int id = Integer.parseInt(txtId.getText().trim());
			int r = JOptionPane.showConfirmDialog(this, "¿Eliminar el pedido #" + id + "?", "Confirmar",
					JOptionPane.YES_NO_OPTION);
			if (r == JOptionPane.YES_OPTION) {
				if (dao.eliminar(id)) {
					JOptionPane.showMessageDialog(this, "Pedido eliminado.");
					limpiar();
					cargarTabla();
				} else {
					JOptionPane.showMessageDialog(this, "No se encontró el pedido.");
				}
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "ID inválido.");
		}
	}

	private void limpiar() {
		txtId.setText("");
		txtVainilla.setText("0");
		txtCocoa.setText("0");
		txtCafe.setText("0");
		txtDireccion.setText("");
		txtFechaPedido.setText(LocalDate.now().format(FMT));
		recalcular();
		if (cmbCliente.getItemCount() > 0) cmbCliente.setSelectedIndex(0);
		tabla.clearSelection();
	}

	// AUXILIARES DE UI
	private JTextField crearCampo(int cols) {
		JTextField tf = new JTextField(cols);
		tf.setFont(F_CAMPO);
		tf.setBackground(COLOR_BLANCO);
		tf.setForeground(COLOR_MARRON);
		tf.setBorder(BorderFactory.createCompoundBorder(
				new LineBorder(COLOR_DORADO, 1, true),
				new EmptyBorder(4, 6, 4, 6)));
		return tf;
	}

	private JLabel etiqueta(String texto) {
		JLabel l = new JLabel(texto);
		l.setFont(F_ETIQ);
		l.setForeground(COLOR_MARRON);
		return l;
	}

	private JButton boton(String texto, Color fondo, Color textoColor, String cmd) {
		JButton b = new JButton(texto);
		b.setFont(F_BOTON);
		b.setBackground(fondo);
		b.setForeground(textoColor);
		b.setFocusPainted(false);
		b.setBorder(BorderFactory.createCompoundBorder(
				new LineBorder(COLOR_MARRON, 1, true), new EmptyBorder(8, 18, 8, 18)));
		b.setCursor(new Cursor(Cursor.HAND_CURSOR));
		b.addActionListener(e -> accion(cmd));
		return b;
	}


	// CLASE AUXILIAR PARA EL COMBOBOX
	private static class ClienteItem {
		final int id;
		final String nombre;
		ClienteItem(int id, String nombre) { this.id = id; this.nombre = nombre; }
		@Override public String toString() { return nombre; }
	}
}