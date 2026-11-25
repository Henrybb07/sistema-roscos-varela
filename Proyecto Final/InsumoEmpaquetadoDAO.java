package poo.roscos;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InsumoEmpaquetadoDAO {

	public List<InsumoEmpaquetado> listar() {
		List<InsumoEmpaquetado> lista = new ArrayList<>();
		String sql = "SELECT * FROM Insumos_Empaquetado ORDER BY id_insumo_empaque";
		try (Connection con = Conexion.getConexion();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				lista.add(mapear(rs));
			}
		} catch (SQLException e) {
			System.out.println("Error al listar insumos de empaquetado: " + e.getMessage());
		}
		return lista;
	}

	public InsumoEmpaquetado buscarPorId(int id) {
		String sql = "SELECT * FROM Insumos_Empaquetado WHERE id_insumo_empaque = ?";
		try (Connection con = Conexion.getConexion();
				PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setInt(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) return mapear(rs);
			}
		} catch (SQLException e) {
			System.out.println("Error al buscar insumo de empaquetado: " + e.getMessage());
		}
		return null;
	}

	public boolean insertar(InsumoEmpaquetado i) {
		String sql = "INSERT INTO Insumos_Empaquetado (nombre_insumo, cantidad, unidad, stock_minimo) VALUES (?, ?, ?, ?)";
		try (Connection con = Conexion.getConexion();
				PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			ps.setString(1, i.getNombreInsumo());
			ps.setInt(2, i.getCantidad());
			ps.setString(3, i.getUnidad());
			ps.setInt(4, i.getStockMinimo());
			if (ps.executeUpdate() > 0) {
				try (ResultSet rs = ps.getGeneratedKeys()) {
					if (rs.next()) i.setIdInsumoEmpaque(rs.getInt(1));
				}
				return true;
			}
		} catch (SQLException e) {
			System.out.println("Error al insertar insumo de empaquetado: " + e.getMessage());
		}
		return false;
	}

	public boolean actualizar(InsumoEmpaquetado i) {
		String sql = "UPDATE Insumos_Empaquetado SET nombre_insumo = ?, cantidad = ?, unidad = ?, stock_minimo = ? WHERE id_insumo_empaque = ?";
		try (Connection con = Conexion.getConexion();
				PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, i.getNombreInsumo());
			ps.setInt(2, i.getCantidad());
			ps.setString(3, i.getUnidad());
			ps.setInt(4, i.getStockMinimo());
			ps.setInt(5, i.getIdInsumoEmpaque());
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			System.out.println("Error al actualizar insumo de empaquetado: " + e.getMessage());
		}
		return false;
	}

	public boolean eliminar(int id) {
		String sql = "DELETE FROM Insumos_Empaquetado WHERE id_insumo_empaque = ?";
		try (Connection con = Conexion.getConexion();
				PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setInt(1, id);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			System.out.println("Error al eliminar insumo de empaquetado: " + e.getMessage());
		}
		return false;
	}

	private InsumoEmpaquetado mapear(ResultSet rs) throws SQLException {
		InsumoEmpaquetado i = new InsumoEmpaquetado();
		i.setIdInsumoEmpaque(rs.getInt("id_insumo_empaque"));
		i.setNombreInsumo(rs.getString("nombre_insumo"));
		i.setCantidad(rs.getInt("cantidad"));
		i.setUnidad(rs.getString("unidad"));
		i.setStockMinimo(rs.getInt("stock_minimo"));
		return i;
	}
}