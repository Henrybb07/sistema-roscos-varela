package poo.roscos;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO {

	// 1. LISTAR TODOS LOS PEDIDOS
	public List<Pedido> listar() {
		List<Pedido> lista = new ArrayList<>();
		String sql = "SELECT * FROM Pedidos ORDER BY id_pedido";

		try (Connection con = Conexion.getConexion(); 
			 PreparedStatement ps = con.prepareStatement(sql); 
			 ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				lista.add(mapear(rs));
			}
		} catch (SQLException e) {
			System.out.println("Error al listar pedidos: " + e.getMessage());
		}
		return lista;
	}

    // 2. BUSCAR POR ID
	public Pedido buscarPorId(int id) {
		String sql = "SELECT * FROM Pedidos WHERE id_pedido = ?"; 
		try (Connection con = Conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) { 
			ps.setInt(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return mapear(rs);
				}
			}
		} catch (SQLException e) {
			System.out.println("Error al buscar pedido: " + e.getMessage());
		}
        return null;
	}

    // 3. INSERTAR (Incluye estado)
	public boolean insertar(Pedido p) {
		String sql = "INSERT INTO Pedidos (id_cliente, cantidad_vainilla, cantidad_cocoa, cantidad_cafe, " + 
					 "direccion_entrega, fecha_pedido, fecha_entrega, total, estado) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try (Connection con = Conexion.getConexion();	
			 PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			
			ps.setInt(1, p.getIdCliente());
			ps.setInt(2, p.getCantidadVainilla());
			ps.setInt(3, p.getCantidadCocoa());
			ps.setInt(4, p.getCantidadCafe());
			ps.setString(5, p.getDireccionEntrega());
			ps.setDate(6, Date.valueOf(p.getFechaPedido()));
			ps.setDate(7, Date.valueOf(p.getFechaEntrega()));
			ps.setDouble(8, p.getTotal());
			ps.setString(9, p.getEstado() != null ? p.getEstado() : "Pendiente");

			int filas = ps.executeUpdate();

			if (filas > 0) {
				try (ResultSet rs = ps.getGeneratedKeys()) {
					if (rs.next()) {
						p.setIdPedido(rs.getInt(1));
					}
                }
				return true;
			}
		} catch (SQLException e) {
			System.out.println("Error al insertar pedido: " + e.getMessage());
		}
		return false;
	}

    // 4. ACTUALIZAR
	public boolean actualizar(Pedido p) {
		String sql = "UPDATE Pedidos SET id_cliente = ?, cantidad_vainilla = ?, cantidad_cocoa = ?, " + 
					 "cantidad_cafe = ?, direccion_entrega = ?, fecha_pedido = ?, " + 
					 "fecha_entrega = ?, total = ?, estado = ? WHERE id_pedido = ?";

		try (Connection con = Conexion.getConexion();
			 PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, p.getIdCliente());
			ps.setInt(2, p.getCantidadVainilla());
			ps.setInt(3, p.getCantidadCocoa());
			ps.setInt(4, p.getCantidadCafe());
			ps.setString(5, p.getDireccionEntrega());
			ps.setDate(6, Date.valueOf(p.getFechaPedido()));
			ps.setDate(7, Date.valueOf(p.getFechaEntrega()));
			ps.setDouble(8, p.getTotal());
			ps.setString(9, p.getEstado());
			ps.setInt(10, p.getIdPedido());

			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			System.out.println("Error al actualizar pedido: " + e.getMessage());
		}
		return false;
	}

	// 4.1 ACTUALIZAR SOLO EL ESTADO (Específico para Producción)
	public boolean actualizarEstado(int idPedido, String nuevoEstado) {
		String sql = "UPDATE Pedidos SET estado = ? WHERE id_pedido = ?";
		try (Connection con = Conexion.getConexion();
			 PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, nuevoEstado);
			ps.setInt(2, idPedido);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			System.out.println("Error al actualizar estado del pedido: " + e.getMessage());
		}
		return false;
	}

    // 5. ELIMINAR
	public boolean eliminar(int id) {
		String sql = "DELETE FROM Pedidos WHERE id_pedido = ?";
		try (Connection con = Conexion.getConexion();
			 PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setInt(1, id);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			System.out.println("Error al eliminar pedido: " + e.getMessage());
		}
		return false;
	}

    // 6. LISTAR CLIENTES
	public List<String[]> listarClientes() {
		List<String[]> lista = new ArrayList<>();
		String sql = "SELECT id_cliente, nombre_cafeteria FROM Clientes ORDER BY nombre_cafeteria";

		try (Connection con = Conexion.getConexion();
			 PreparedStatement ps = con.prepareStatement(sql);
			 ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				lista.add(new String[] {
						String.valueOf(rs.getInt("id_cliente")),
						rs.getString("nombre_cafeteria")
				});
			}
		} catch (SQLException e) {
			System.out.println("Error al listar clientes: " + e.getMessage());
		}
		return lista;
	}

    // MÉTODO AUXILIAR MAPEAR
	private Pedido mapear(ResultSet rs) throws SQLException {
		Pedido p = new Pedido();
		p.setIdPedido(rs.getInt("id_pedido"));
		p.setIdCliente(rs.getInt("id_cliente"));
		p.setCantidadVainilla(rs.getInt("cantidad_vainilla"));
		p.setCantidadCocoa(rs.getInt("cantidad_cocoa"));
		p.setCantidadCafe(rs.getInt("cantidad_cafe"));
		p.setDireccionEntrega(rs.getString("direccion_entrega"));
		p.setFechaPedido(rs.getDate("fecha_pedido").toLocalDate());
		p.setFechaEntrega(rs.getDate("fecha_entrega").toLocalDate());
		p.setTotal(rs.getDouble("total"));
		
		// Manejar estado por si acaso viene nulo en registros viejos
		String est = rs.getString("estado");
		p.setEstado(est != null ? est : "Pendiente");
		
		return p;
	}
}