package poo.roscos;

public class PlanProduccion {

	// 2 kg de harina = 350 roscos = 50 paquetes de 7 piezas. Por ende, 1 kg = 25 paquetes.
	public static final int PAQUETES_POR_KG_HARINA = 25;

	private String sabor;
	private int paquetesPedidos;
	private double kgHarinaNecesarios;

	public PlanProduccion() {
	}

	public PlanProduccion(String sabor, int paquetesPedidos, double kgHarinaNecesarios) {
		this.sabor = sabor;
		this.paquetesPedidos = paquetesPedidos;
		this.kgHarinaNecesarios = kgHarinaNecesarios;
	}

	public String getSabor() { return sabor; }
	public void setSabor(String sabor) { this.sabor = sabor; }

	public int getPaquetesPedidos() { return paquetesPedidos; }
	public void setPaquetesPedidos(int paquetesPedidos) { this.paquetesPedidos = paquetesPedidos; }

	public double getKgHarinaNecesarios() { return kgHarinaNecesarios; }
	public void setKgHarinaNecesarios(double kgHarinaNecesarios) { this.kgHarinaNecesarios = kgHarinaNecesarios; }
}