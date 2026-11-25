package poo.roscos;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

	// 1. LISTAR TODOS LOS CLIENTES
	public List<Cliente> listar() {
		List<Cliente> lista = new ArrayList<>();
		String sql = "SELECT * FROM Clientes ORDER BY id_cliente";

		try (Connection con = Conexion.getConexion();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				lista.add(mapear(rs));
			}
		} catch (SQLException e) {
			System.out.println("Error al listar clientes: " + e.getMessage());
		}
		return lista;
	}

	// 2. BUSCAR POR ID
	public Cliente buscarPorId(int id) {
		String sql = "SELECT * FROM Clientes WHERE id_cliente = ?";
        
		try (Connection con = Conexion.getConexion();
				PreparedStatement ps = con.prepareStatement(sql)) {
            
			ps.setInt(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return mapear(rs);
				}
			}
		} catch (SQLException e) {
			System.out.println("Error al buscar cliente: " + e.getMessage());
		}
		return null; //Se supone que regresa "No encontrado"
	}

    // 3. INSERTAR
	public boolean insertar(Cliente c) {
		String sql = "INSERT INTO Clientes (nombre_cafeteria, direccion, contacto) VALUES (?, ?, ?)";
        
		try (Connection con = Conexion.getConexion();
				PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
			ps.setString(1, c.getNombreCafeteria());
			ps.setString(2, c.getDireccion());
			ps.setString(3, c.getContacto());

			int filas = ps.executeUpdate();

			if (filas > 0) {
				try (ResultSet rs = ps.getGeneratedKeys()) {
					if (rs.next()) {
						c.setIdCliente(rs.getInt(1));
					}
				}
				return true;
			}
		} catch (SQLException e) {
			System.out.println("Error al insertar cliente: " + e.getMessage());
		}
		return false;
	}

	// 4. ACTUALIZAR
	public boolean actualizar(Cliente c) {
		String sql = "UPDATE Clientes SET nombre_cafeteria = ?, direccion = ?, contacto = ? WHERE id_cliente = ?";

		try (Connection con = Conexion.getConexion();
				PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, c.getNombreCafeteria());
			ps.setString(2, c.getDireccion());
			ps.setString(3, c.getContacto());
			ps.setInt(4, c.getIdCliente());

			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			System.out.println("Error al actualizar cliente: " + e.getMessage());
		}
		return false;
	}

	// 5. ELIMINAR
	public boolean eliminar(int id) {
		String sql = "DELETE FROM Clientes WHERE id_cliente = ?";

		try (Connection con = Conexion.getConexion();
				PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, id);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			System.out.println("Error al eliminar cliente (puede tener pedidos asociados): " + e.getMessage());
		}
		return false;
	}

	// MÉTODO AUXILIAR: Mapear una fila del ResultSet a un objeto Cliente
	private Cliente mapear(ResultSet rs) throws SQLException {
		Cliente c = new Cliente();
		c.setIdCliente(rs.getInt("id_cliente"));
		c.setNombreCafeteria(rs.getString("nombre_cafeteria"));
		c.setDireccion(rs.getString("direccion"));
		c.setContacto(rs.getString("contacto"));
		return c;
	}

    // PRUEBA RÁPIDA
	public static void main(String[] args) {
		ClienteDAO dao = new ClienteDAO();

		System.out.println("--- LISTA DE CLIENTES ---");
		for (Cliente c : dao.listar()) {
			System.out.println(c);
		}
	}
}