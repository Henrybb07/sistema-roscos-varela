package poo.roscos;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {
	public Usuario validarCredenciales(String nombreUsuario, String passwordEnClaro) {
		if (nombreUsuario == null || passwordEnClaro == null) return null;

		String sql = "SELECT * FROM Usuarios WHERE nombre_usuario = ? AND activo = TRUE";

		try (Connection con = Conexion.getConexion();
				PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, nombreUsuario.trim());
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					String hashGuardado = rs.getString("password_hash");
					if (HashUtil.coincide(passwordEnClaro, hashGuardado)) {
						return mapear(rs);
					}
				}
			}
		} catch (SQLException e) {
			System.out.println("Error al validar credenciales: " + e.getMessage());
		}
		return null;
	}

	public boolean insertar(String nombreUsuario, String passwordEnClaro, Rol rol) {
		String sql = "INSERT INTO Usuarios (nombre_usuario, password_hash, rol) VALUES (?, ?, ?)";
		try (Connection con = Conexion.getConexion();
				PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

			ps.setString(1, nombreUsuario.trim());
			ps.setString(2, HashUtil.hashear(passwordEnClaro));
			ps.setString(3, rol.name());
			return ps.executeUpdate() > 0;

		} catch (SQLException e) {
			System.out.println("Error al insertar usuario: " + e.getMessage());
		}
		return false;
	}

	/** Actualiza el hash de un usuario existente (por si hay que regenerarlo). */
	public boolean actualizarPassword(String nombreUsuario, String passwordEnClaro) {
		String sql = "UPDATE Usuarios SET password_hash = ? WHERE nombre_usuario = ?";
		try (Connection con = Conexion.getConexion();
				PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, HashUtil.hashear(passwordEnClaro));
			ps.setString(2, nombreUsuario.trim());
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			System.out.println("Error al actualizar password: " + e.getMessage());
		}
		return false;
	}

	public List<Usuario> listar() {
		List<Usuario> lista = new ArrayList<>();
		String sql = "SELECT * FROM Usuarios ORDER BY id_usuario";
		try (Connection con = Conexion.getConexion();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) lista.add(mapear(rs));
		} catch (SQLException e) {
			System.out.println("Error al listar usuarios: " + e.getMessage());
		}
		return lista;
	}

	private Usuario mapear(ResultSet rs) throws SQLException {
		Usuario u = new Usuario();
		u.setIdUsuario(rs.getInt("id_usuario"));
		u.setNombreUsuario(rs.getString("nombre_usuario"));
		u.setPasswordHash(rs.getString("password_hash"));
		u.setRol(Rol.desdeString(rs.getString("rol")));
		u.setActivo(rs.getBoolean("activo"));
		return u;
	}
}