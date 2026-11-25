package poo.roscos;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlaneacionDAO {

	// Trae todos los pedidos que NO están entregados ni realizados.
	public List<Object[]> pedidosPendientes() {
		List<Object[]> lista = new ArrayList<>();
		String sql = "SELECT id_pedido, cantidad_vainilla, cantidad_cocoa, cantidad_cafe " + "FROM Pedidos WHERE estado NOT IN ('Realizado', 'Entregado') ORDER BY id_pedido";

		try (Connection con = Conexion.getConexion();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				lista.add(new Object[]{
						rs.getInt("id_pedido"),
						rs.getInt("cantidad_vainilla"),
						rs.getInt("cantidad_cocoa"),
						rs.getInt("cantidad_cafe")
				});
			}
		} catch (SQLException e) {
			System.out.println("Error al leer pedidos pendientes: " + e.getMessage());
		}
		return lista;
	}

	// Trae la receta de un sabor específico (id_insumo, nombre, cantidad_por_kg_harina, unidad).
	public List<Object[]> recetaPorSabor(String sabor) {
		List<Object[]> lista = new ArrayList<>();
		String sql = "SELECT r.id_insumo, i.nombre_insumo, r.cantidad, r.unidad " +
				"FROM Recetas r " +
				"INNER JOIN Insumos_Produccion i ON r.id_insumo = i.id_insumo " +
				"WHERE r.sabor = ? ORDER BY r.id_insumo";
		try (Connection con = Conexion.getConexion();
				PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, sabor);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					lista.add(new Object[]{
							rs.getInt("id_insumo"),
							rs.getString("nombre_insumo"),
							rs.getDouble("cantidad"),
							rs.getString("unidad")
					});
				}
			}
		} catch (SQLException e) {
			System.out.println("Error al leer receta: " + e.getMessage());
		}
		return lista;
	}

	// Trae el stock real de la tabla Insumos_Produccion.
	public double stockInsumo(int idInsumo) {
		String sql = "SELECT cantidad FROM Insumos_Produccion WHERE id_insumo = ?";
		try (Connection con = Conexion.getConexion();
				PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setInt(1, idInsumo);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) return rs.getDouble("cantidad");
			}
		} catch (SQLException e) {
			System.out.println("Error al leer stock: " + e.getMessage());
		}
		return 0;
	}
}