package poo.roscos;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InsumoProduccionDAO {

	public List<InsumoProduccion> listar() {
		List<InsumoProduccion> lista = new ArrayList<>();
		String sql = "SELECT * FROM Insumos_Produccion ORDER BY id_insumo";
		try (Connection con = Conexion.getConexion();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				lista.add(mapear(rs));
			}
		} catch (SQLException e) {
			System.out.println("Error al listar insumos de producción: " + e.getMessage());
		}
		return lista;
	}

	public InsumoProduccion buscarPorId(int id) {
		String sql = "SELECT * FROM Insumos_Produccion WHERE id_insumo = ?";
		try (Connection con = Conexion.getConexion();
				PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setInt(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) return mapear(rs);
			}
		} catch (SQLException e) {
			System.out.println("Error al buscar insumo de producción: " + e.getMessage());
		}
		return null;
	}

	public boolean insertar(InsumoProduccion i) {
		String sql = "INSERT INTO Insumos_Produccion (nombre_insumo, cantidad, unidad, stock_minimo) VALUES (?, ?, ?, ?)";
		try (Connection con = Conexion.getConexion();
				PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			ps.setString(1, i.getNombreInsumo());
			ps.setDouble(2, i.getCantidad());
			ps.setString(3, i.getUnidad());
			ps.setDouble(4, i.getStockMinimo());
			if (ps.executeUpdate() > 0) {
				try (ResultSet rs = ps.getGeneratedKeys()) {
					if (rs.next()) i.setIdInsumo(rs.getInt(1));
				}
				return true;
			}
		} catch (SQLException e) {
			System.out.println("Error al insertar insumo de producción: " + e.getMessage());
		}
		return false;
	}

	public boolean actualizar(InsumoProduccion i) {
		String sql = "UPDATE Insumos_Produccion SET nombre_insumo = ?, cantidad = ?, unidad = ?, stock_minimo = ? WHERE id_insumo = ?";
		try (Connection con = Conexion.getConexion();
				PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, i.getNombreInsumo());
			ps.setDouble(2, i.getCantidad());
			ps.setString(3, i.getUnidad());
			ps.setDouble(4, i.getStockMinimo());
			ps.setInt(5, i.getIdInsumo());
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			System.out.println("Error al actualizar insumo de producción: " + e.getMessage());
		}
		return false;
	}

	public boolean eliminar(int id) {
		String sql = "DELETE FROM Insumos_Produccion WHERE id_insumo = ?";
		try (Connection con = Conexion.getConexion();
				PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setInt(1, id);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			System.out.println("Error al eliminar insumo de producción (puede estar en recetas): " + e.getMessage());
		}
		return false;
	}

	private InsumoProduccion mapear(ResultSet rs) throws SQLException {
		InsumoProduccion i = new InsumoProduccion();
		i.setIdInsumo(rs.getInt("id_insumo"));
		i.setNombreInsumo(rs.getString("nombre_insumo"));
		i.setCantidad(rs.getDouble("cantidad"));
		i.setUnidad(rs.getString("unidad"));
		i.setStockMinimo(rs.getDouble("stock_minimo"));
		return i;
	}
}