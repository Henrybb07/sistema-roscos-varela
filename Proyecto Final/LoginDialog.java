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
import java.awt.Image;
import java.awt.Insets;
import java.io.File;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicButtonUI;

public class LoginDialog extends JDialog {

    private static final long serialVersionUID = 1L;

    // Paleta
    private static final Color COLOR_FONDO  = new Color(0xFA, 0xF3, 0xE8);
    private static final Color COLOR_MARRON = new Color(0x5C, 0x3A, 0x1E);
    private static final Color COLOR_DORADO = new Color(0xC9, 0xA6, 0x6B);
    private static final Color COLOR_VERDE  = new Color(0x4F, 0x79, 0x42);
    private static final Color COLOR_BLANCO = new Color(0xFF, 0xFD, 0xF7);
    private static final Color COLOR_ROJO   = new Color(0xA9, 0x32, 0x26);

    private static final Font F_TITULO = new Font("Serif",     Font.BOLD,  26);
    private static final Font F_ETIQ   = new Font("SansSerif", Font.BOLD,  13);
    private static final Font F_CAMPO  = new Font("SansSerif", Font.PLAIN, 14);
    private static final Font F_BOTON  = new Font("SansSerif", Font.BOLD,  13);

    private JTextField txtUsuario;
    private JPasswordField txtPassword;
    private JLabel lblError;
    private JButton btnEntrar;

    private final UsuarioDAO dao = new UsuarioDAO();
    private boolean loginExitoso = false;

    public LoginDialog() {
        super((java.awt.Frame) null, "Roscos Varela - Iniciar Sesión", true);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setResizable(false);
        construirUI();
        pack();
        setLocationRelativeTo(null);
    }

    private void construirUI() {
        JPanel content = new JPanel(new BorderLayout(10, 15));
        content.setBackground(COLOR_FONDO);
        content.setBorder(new EmptyBorder(20, 40, 20, 40));
        setContentPane(content);

        // ==========================================
        // 1. LOGO (NORTE)
        // ==========================================
        JPanel panelLogo = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelLogo.setBackground(COLOR_FONDO);
        
        JLabel lblLogo = new JLabel();
        try {
            // Busca el archivo logo2025.png en la carpeta "imagenes" de tu proyecto
            URL urlLogo = getClass().getResource("/imagenes/logo2025.png");
            if (urlLogo != null) {
                ImageIcon iconoOriginal = new ImageIcon(urlLogo);
                // Redimensionar la imagen a 200px de ancho para que se vea bien
                Image img = iconoOriginal.getImage();
                Image imgEscalada = img.getScaledInstance(200, -1, Image.SCALE_SMOOTH);
                lblLogo.setIcon(new ImageIcon(imgEscalada));
            } else {
                lblLogo.setText("ROSCOS VARELA");
                lblLogo.setFont(F_TITULO);
                lblLogo.setForeground(COLOR_MARRON);
                System.out.println("Advertencia: No se encontró /imagenes/logo2025.png");
            }
        } catch (Exception e) {
            lblLogo.setText("ROSCOS VARELA");
            lblLogo.setFont(F_TITULO);
            lblLogo.setForeground(COLOR_MARRON);
        }
        panelLogo.add(lblLogo);
        content.add(panelLogo, BorderLayout.NORTH);

        // ==========================================
        // 2. FORMULARIO (CENTRO)
        // ==========================================
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(COLOR_FONDO);
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6, 6, 6, 6);
        g.anchor = GridBagConstraints.WEST;

        txtUsuario = new JTextField(18);
        txtUsuario.setFont(F_CAMPO);
        txtUsuario.setBackground(COLOR_BLANCO);
        txtUsuario.setForeground(COLOR_MARRON);
        txtUsuario.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(COLOR_DORADO, 1, true), new EmptyBorder(5, 7, 5, 7)));

        txtPassword = new JPasswordField(18);
        txtPassword.setFont(F_CAMPO);
        txtPassword.setBackground(COLOR_BLANCO);
        txtPassword.setForeground(COLOR_MARRON);
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(COLOR_DORADO, 1, true), new EmptyBorder(5, 7, 5, 7)));

        g.gridx = 0; g.gridy = 0;
        form.add(etiqueta("Usuario:"), g);
        g.gridx = 1;
        form.add(txtUsuario, g);

        g.gridx = 0; g.gridy = 1;
        form.add(etiqueta("Contraseña:"), g);
        g.gridx = 1;
        form.add(txtPassword, g);

        lblError = new JLabel(" ");
        lblError.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblError.setForeground(COLOR_ROJO);
        g.gridx = 0; g.gridy = 2; g.gridwidth = 2;
        form.add(lblError, g);
        g.gridwidth = 1;

        content.add(form, BorderLayout.CENTER);

        // ==========================================
        // 3. BOTONES Y ESLOGAN (SUR)
        // ==========================================
        JPanel panelSur = new JPanel();
        panelSur.setLayout(new BoxLayout(panelSur, BoxLayout.Y_AXIS));
        panelSur.setBackground(COLOR_FONDO);

        // Botones
        JPanel pBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        pBotones.setBackground(COLOR_FONDO);

        btnEntrar = boton("Entrar", COLOR_VERDE,  Color.WHITE, e -> intentarLogin());
        JButton btnSalir = boton("Salir",  COLOR_MARRON, Color.WHITE, e -> cancelar());

        btnEntrar.setPreferredSize(new Dimension(120, 35));
        btnSalir.setPreferredSize(new Dimension(100, 35));

        pBotones.add(btnEntrar);
        pBotones.add(btnSalir);
        
        // Eslogan
        JLabel lblEslogan = new JLabel("Enróscate en el sabor");
        lblEslogan.setFont(new Font("Serif", Font.ITALIC, 12));
        lblEslogan.setForeground(COLOR_DORADO);
        lblEslogan.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panelSur.add(pBotones);
        panelSur.add(Box.createVerticalStrut(10));
        panelSur.add(lblEslogan);

        content.add(panelSur, BorderLayout.SOUTH);

        getRootPane().setDefaultButton(btnEntrar);
    }

    private void intentarLogin() {
        String usuario  = txtUsuario.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (usuario.isEmpty() || password.isEmpty()) {
            lblError.setText("Escribe usuario y contraseña.");
            return;
        }

        btnEntrar.setEnabled(false);
        lblError.setText("Verificando...");

        Usuario u = dao.validarCredenciales(usuario, password);

        if (u == null) {
            lblError.setText("Usuario o contraseña incorrectos.");
            txtPassword.setText("");
            txtPassword.requestFocusInWindow();
            btnEntrar.setEnabled(true);
            return;
        }

        Sesion.iniciar(u);
        loginExitoso = true;
        dispose();
    }

    private void cancelar() {
        loginExitoso = false;
        dispose();
    }

    public boolean isLoginExitoso() {
        return loginExitoso;
    }

    private JLabel etiqueta(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(F_ETIQ);
        l.setForeground(COLOR_MARRON);
        return l;
    }

    private JButton boton(String texto, Color fondo, Color textoColor,
                          java.awt.event.ActionListener accion) {
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
}