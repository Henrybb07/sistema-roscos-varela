package poo.roscos;

import java.util.ArrayList;
import java.util.List;

public class ResumenVenta {

	// KPIs del periodo
	private int totalPaquetes;
	private double totalIngresos;
	private int cafeteriasActivas;
	private double ticketPromedio;

	// Desglose por sabor (siempre 3 filas: Vainilla, Cocoa, Café)
	private List<FilaSabor> desgloseSabores = new ArrayList<>();

	// Cafeterías con ventas en el periodo
	private List<FilaCafeteria> cafeterias = new ArrayList<>();

	public ResumenVenta() {
	}

	// Getters y Setters
	public int getTotalPaquetes() { return totalPaquetes; }
	public void setTotalPaquetes(int totalPaquetes) { this.totalPaquetes = totalPaquetes; }

	public double getTotalIngresos() { return totalIngresos; }
	public void setTotalIngresos(double totalIngresos) { this.totalIngresos = totalIngresos; }

	public int getCafeteriasActivas() { return cafeteriasActivas; }
	public void setCafeteriasActivas(int cafeteriasActivas) { this.cafeteriasActivas = cafeteriasActivas; }

	public double getTicketPromedio() { return ticketPromedio; }
	public void setTicketPromedio(double ticketPromedio) { this.ticketPromedio = ticketPromedio; }

	public List<FilaSabor> getDesgloseSabores() { return desgloseSabores; }
	public void setDesgloseSabores(List<FilaSabor> desgloseSabores) { this.desgloseSabores = desgloseSabores; }

	public List<FilaCafeteria> getCafeterias() { return cafeterias; }
	public void setCafeterias(List<FilaCafeteria> cafeterias) { this.cafeterias = cafeterias; }

	// Indica si hubo ventas en el periodo (para mostrar estado vacío en la UI).
	public boolean hayVentas() {
		return totalPaquetes > 0;
	}

	// FILA DE LA TABLA "DESGLOSE POR SABOR"
	public static class FilaSabor {
		private final String sabor;
		private final int paquetes;
		private final double ingresos;
		private final double porcentaje;

		public FilaSabor(String sabor, int paquetes, double ingresos, double porcentaje) {
			this.sabor = sabor;
			this.paquetes = paquetes;
			this.ingresos = ingresos;
			this.porcentaje = porcentaje;
		}

		public String getSabor() { return sabor; }
		public int getPaquetes() { return paquetes; }
		public double getIngresos() { return ingresos; }
		public double getPorcentaje() { return porcentaje; }
	}

	// FILA DE LA TABLA "CAFETERÍAS CON VENTAS"
	public static class FilaCafeteria {
		private final String nombre;
		private final int numPedidos;
		private final int paquetes;
		private final double ingresos;

		public FilaCafeteria(String nombre, int numPedidos, int paquetes, double ingresos) {
			this.nombre = nombre;
			this.numPedidos = numPedidos;
			this.paquetes = paquetes;
			this.ingresos = ingresos;
		}

		public String getNombre() { return nombre; }
		public int getNumPedidos() { return numPedidos; }
		public int getPaquetes() { return paquetes; }
		public double getIngresos() { return ingresos; }
	}

	@Override
	public String toString() {
		return "ResumenVenta [paquetes=" + totalPaquetes + ", ingresos=$" + totalIngresos
				+ ", cafeterias=" + cafeteriasActivas + "]";
	}
}