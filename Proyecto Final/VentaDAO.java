package poo.roscos;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;

public class VentaDAO {

	public ResumenVenta obtenerResumen(LocalDate desde, LocalDate hasta, Integer idCliente) {
		ResumenVenta resumen = new ResumenVenta();
		if (desde == null || hasta == null || desde.isAfter(hasta)) return resumen;

		cargarTotales(resumen, desde, hasta, idCliente);
		cargarDesgloseSabores(resumen, desde, hasta, idCliente);
		cargarCafeterias(resumen, desde, hasta, idCliente);

		return resumen;
	}

	// 1. KPIs: paquetes, ingresos, cafeterías activas, ticket promedio
	private void cargarTotales(ResumenVenta r, LocalDate desde, LocalDate hasta, Integer idCliente) {
		StringBuilder sql = new StringBuilder(
			"SELECT COUNT(*) AS num_pedidos, " +
			"       COALESCE(SUM(cantidad_vainilla + cantidad_cocoa + cantidad_cafe), 0) AS paq, " +
			"       COALESCE(SUM(total), 0) AS ingresos, " +
			"       COUNT(DISTINCT id_cliente) AS cafeterias " +
			"FROM Pedidos " +
			"WHERE estado = 'Entregado' AND fecha_entrega BETWEEN ? AND ?"
		);
		if (idCliente != null) sql.append(" AND id_cliente = ?");

		try (Connection con = Conexion.getConexion();
			 PreparedStatement ps = con.prepareStatement(sql.toString())) {

			ps.setDate(1, Date.valueOf(desde));
			ps.setDate(2, Date.valueOf(hasta));
			if (idCliente != null) ps.setInt(3, idCliente);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					r.setTotalPaquetes(rs.getInt("paq"));
					r.setTotalIngresos(rs.getDouble("ingresos"));
					r.setCafeteriasActivas(rs.getInt("cafeterias"));

					int numPedidos = rs.getInt("num_pedidos");
					r.setTicketPromedio(numPedidos > 0 ? r.getTotalIngresos() / numPedidos : 0);
				}
			}
		} catch (SQLException e) {
			System.out.println("Error al cargar totales de ventas: " + e.getMessage());
		}
	}

	// 2. Desglose por sabor (siempre 3 filas, orden Vainilla/Cocoa/Café)
	//    Los ingresos se calculan en Java (una sola fuente de verdad del precio).
	private void cargarDesgloseSabores(ResumenVenta r, LocalDate desde, LocalDate hasta, Integer idCliente) {
		StringBuilder sql = new StringBuilder(
			"SELECT COALESCE(SUM(cantidad_vainilla), 0) AS v, " +
			"       COALESCE(SUM(cantidad_cocoa), 0)    AS c, " +
			"       COALESCE(SUM(cantidad_cafe), 0)     AS f " +
			"FROM Pedidos " +
			"WHERE estado = 'Entregado' AND fecha_entrega BETWEEN ? AND ?"
		);
		if (idCliente != null) sql.append(" AND id_cliente = ?");

		int paqV = 0, paqC = 0, paqF = 0;
		try (Connection con = Conexion.getConexion();
			 PreparedStatement ps = con.prepareStatement(sql.toString())) {

			ps.setDate(1, Date.valueOf(desde));
			ps.setDate(2, Date.valueOf(hasta));
			if (idCliente != null) ps.setInt(3, idCliente);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					paqV = rs.getInt("v");
					paqC = rs.getInt("c");
					paqF = rs.getInt("f");
				}
			}
		} catch (SQLException e) {
			System.out.println("Error al cargar desglose por sabor: " + e.getMessage());
			return;
		}

		int totalPaq = paqV + paqC + paqF;
		List<ResumenVenta.FilaSabor> filas = r.getDesgloseSabores();

		filas.add(new ResumenVenta.FilaSabor(
			"Vainilla", paqV,
			paqV * Pedido.PRECIO_PAQUETE,
			totalPaq > 0 ? (paqV * 100.0 / totalPaq) : 0
		));
		filas.add(new ResumenVenta.FilaSabor(
			"Cocoa", paqC,
			paqC * Pedido.PRECIO_PAQUETE,
			totalPaq > 0 ? (paqC * 100.0 / totalPaq) : 0
		));
		filas.add(new ResumenVenta.FilaSabor(
			"Café", paqF,
			paqF * Pedido.PRECIO_PAQUETE,
			totalPaq > 0 ? (paqF * 100.0 / totalPaq) : 0
		));
	}

	// 3. Cafeterías con ventas en el periodo
	private void cargarCafeterias(ResumenVenta r, LocalDate desde, LocalDate hasta, Integer idCliente) {
		StringBuilder sql = new StringBuilder(
			"SELECT c.nombre_cafeteria, " +
			"       COUNT(p.id_pedido) AS num_pedidos, " +
			"       SUM(p.cantidad_vainilla + p.cantidad_cocoa + p.cantidad_cafe) AS paquetes, " +
			"       SUM(p.total) AS ingresos " +
			"FROM Pedidos p " +
			"INNER JOIN Clientes c ON p.id_cliente = c.id_cliente " +
			"WHERE p.estado = 'Entregado' AND p.fecha_entrega BETWEEN ? AND ?"
		);
		if (idCliente != null) sql.append(" AND p.id_cliente = ?");
		sql.append(" GROUP BY c.id_cliente, c.nombre_cafeteria ORDER BY ingresos DESC");

		try (Connection con = Conexion.getConexion();
			 PreparedStatement ps = con.prepareStatement(sql.toString())) {

			ps.setDate(1, Date.valueOf(desde));
			ps.setDate(2, Date.valueOf(hasta));
			if (idCliente != null) ps.setInt(3, idCliente);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					r.getCafeterias().add(new ResumenVenta.FilaCafeteria(
						rs.getString("nombre_cafeteria"),
						rs.getInt("num_pedidos"),
						rs.getInt("paquetes"),
						rs.getDouble("ingresos")
					));
				}
			}
		} catch (SQLException e) {
			System.out.println("Error al cargar cafeterías con ventas: " + e.getMessage());
		}
	}

	// PRUEBA RÁPIDA
	public static void main(String[] args) {
		VentaDAO dao = new VentaDAO();
		LocalDate desde = LocalDate.now().withDayOfMonth(1);
		LocalDate hasta = LocalDate.now();

		ResumenVenta r = dao.obtenerResumen(desde, hasta, null);
		System.out.println("--- RESUMEN " + desde + " a " + hasta + " ---");
		System.out.println("Paquetes: " + r.getTotalPaquetes()
				+ " | Ingresos: $" + r.getTotalIngresos()
				+ " | Cafeterías: " + r.getCafeteriasActivas()
				+ " | Ticket prom: $" + r.getTicketPromedio());

		System.out.println("\n--- DESGLOSE POR SABOR ---");
		for (ResumenVenta.FilaSabor f : r.getDesgloseSabores()) {
			System.out.printf("%-10s %4d paq  $%8.2f  %.1f%%%n",
					f.getSabor(), f.getPaquetes(), f.getIngresos(), f.getPorcentaje());
		}

		System.out.println("\n--- CAFETERÍAS CON VENTAS ---");
		for (ResumenVenta.FilaCafeteria f : r.getCafeterias()) {
			System.out.printf("%-25s %3d ped  %5d paq  $%9.2f%n",
					f.getNombre(), f.getNumPedidos(), f.getPaquetes(), f.getIngresos());
		}
	}
}