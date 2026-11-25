package poo.roscos;

public class InsumoEmpaquetado {
	private int idInsumoEmpaque;
	private String nombreInsumo;
	private int cantidad;
	private String unidad;
	private int stockMinimo;

	public InsumoEmpaquetado() {
	}

	public InsumoEmpaquetado(int idInsumoEmpaque, String nombreInsumo, int cantidad, String unidad, int stockMinimo) {
		this.idInsumoEmpaque = idInsumoEmpaque;
		this.nombreInsumo = nombreInsumo;
		this.cantidad = cantidad;
		this.unidad = unidad;
		this.stockMinimo = stockMinimo;
	}

	public InsumoEmpaquetado(String nombreInsumo, int cantidad, String unidad, int stockMinimo) {
		this.nombreInsumo = nombreInsumo;
		this.cantidad = cantidad;
		this.unidad = unidad;
		this.stockMinimo = stockMinimo;
	}

	public int getIdInsumoEmpaque() { return idInsumoEmpaque; }
	public void setIdInsumoEmpaque(int idInsumoEmpaque) { this.idInsumoEmpaque = idInsumoEmpaque; }

	public String getNombreInsumo() { return nombreInsumo; }
	public void setNombreInsumo(String nombreInsumo) { this.nombreInsumo = nombreInsumo; }

	public int getCantidad() { return cantidad; }
	public void setCantidad(int cantidad) { this.cantidad = cantidad; }

	public String getUnidad() { return unidad; }
	public void setUnidad(String unidad) { this.unidad = unidad; }

	public int getStockMinimo() { return stockMinimo; }
	public void setStockMinimo(int stockMinimo) { this.stockMinimo = stockMinimo; }

	/** Estado calculado para la UI: "Sin stock", "Bajo" o "OK". */
	public String getEstado() {
		if (cantidad <= 0) return "Sin stock";
		if (cantidad <= stockMinimo) return "Bajo";
		return "OK";
	}

	@Override
	public String toString() {
		return "InsumoEmpaquetado [ID=" + idInsumoEmpaque + ", " + nombreInsumo + ", " + cantidad + " " + unidad + "]";
	}
}