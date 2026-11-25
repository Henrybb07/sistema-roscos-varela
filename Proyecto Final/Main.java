package poo.roscos;

import java.awt.EventQueue;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

public class Main {

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ignored) { }

		EventQueue.invokeLater(() -> {

			LoginDialog login = new LoginDialog();
			login.setVisible(true);

			if (!login.isLoginExitoso()) {
				System.exit(0);
			}

			Usuario u = Sesion.getUsuario();
			JOptionPane.showMessageDialog(null, "Bienvenido, " + u.getNombreUsuario() + "\nRol: " + u.getRol().etiquetaBonita());
			VentanaPrincipal ventana = new VentanaPrincipal();
			ventana.setVisible(true);
		});
	}
}