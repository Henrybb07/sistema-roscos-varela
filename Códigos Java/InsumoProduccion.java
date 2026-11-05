package poo.roscos;

public class InsumoProduccion {
	private int idInsumo;
	private String nombreInsumo;
	private double cantidad;
	private String unidad;
	private double stockMinimo;

	public InsumoProduccion() {
	}

	public InsumoProduccion(int idInsumo, String nombreInsumo, double cantidad, String unidad, double stockMinimo) {
		this.idInsumo = idInsumo;
		this.nombreInsumo = nombreInsumo;
		this.cantidad = cantidad;
		this.unidad = unidad;
		this.stockMinimo = stockMinimo;
	}

	public InsumoProduccion(String nombreInsumo, double cantidad, String unidad, double stockMinimo) {
		this.nombreInsumo = nombreInsumo;
		this.cantidad = cantidad;
		this.unidad = unidad;
		this.stockMinimo = stockMinimo;
	}

	public int getIdInsumo() { return idInsumo; }
	public void setIdInsumo(int idInsumo) { this.idInsumo = idInsumo; }

	public String getNombreInsumo() { return nombreInsumo; }
	public void setNombreInsumo(String nombreInsumo) { this.nombreInsumo = nombreInsumo; }

	public double getCantidad() { return cantidad; }
	public void setCantidad(double cantidad) { this.cantidad = cantidad; }

	public String getUnidad() { return unidad; }
	public void setUnidad(String unidad) { this.unidad = unidad; }

	public double getStockMinimo() { return stockMinimo; }
	public void setStockMinimo(double stockMinimo) { this.stockMinimo = stockMinimo; }

	/** Estado calculado "Sin stock", "Bajo" o "OK". */
	public String getEstado() {
		if (cantidad <= 0) return "Sin stock";
		if (cantidad <= stockMinimo) return "Bajo";
		return "OK";
	}

	@Override
	public String toString() {
		return "InsumoProduccion [ID=" + idInsumo + ", " + nombreInsumo + ", " + cantidad + " " + unidad + "]";
	}
}