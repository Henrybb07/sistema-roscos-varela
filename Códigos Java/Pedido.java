package poo.roscos;

import java.time.LocalDate;

public class Pedido {

	public static final double PRECIO_PAQUETE = 25.0;
	public static final int DIAS_ENTREGA = 4;

	private int idPedido;
	private int idCliente;
	private int cantidadVainilla;
	private int cantidadCocoa;
	private int cantidadCafe;
	private String direccionEntrega;
	private LocalDate fechaPedido;
	private LocalDate fechaEntrega;
	private double total;

	public Pedido() { }

	// ---------- ID ----------
	public int getIdPedido() { return idPedido; }
	public void setIdPedido(int idPedido) { this.idPedido = idPedido; }

	// ---------- CLIENTE ----------
	public int getIdCliente() { return idCliente; }
	public void setIdCliente(int idCliente) { this.idCliente = idCliente; }

	// ---------- CANTIDADES (calculan total) ----------
	public int getCantidadVainilla() { return cantidadVainilla; }
	public void setCantidadVainilla(int c) {
    	this.cantidadVainilla = c;
        recalcularTotal();
	}

	public int getCantidadCocoa() { return cantidadCocoa; }
	public void setCantidadCocoa(int c) {
    	this.cantidadCocoa = c;
        recalcularTotal();
	}

	public int getCantidadCafe() { return cantidadCafe; }
	public void setCantidadCafe(int c) {
    	this.cantidadCafe = c;
        recalcularTotal();
	}

	// ---------- DIRECCIÓN ----------
	public String getDireccionEntrega() { return direccionEntrega; }
	public void setDireccionEntrega(String d) { this.direccionEntrega = d; }

	// ---------- FECHAS (fecha entrega se debe calcular sola) ----------
	public LocalDate getFechaPedido() { return fechaPedido; }
	public void setFechaPedido(LocalDate f) {
		this.fechaPedido = f;
		recalcularFechaEntrega();
	}

	public LocalDate getFechaEntrega() { return fechaEntrega; }
	public void setFechaEntrega(LocalDate f) { this.fechaEntrega = f; }

    // ---------- TOTAL ----------
	public double getTotal() { return total; }
	public void setTotal(double t) { this.total = t; }

    // REGLAS DE NEGOCIO
    
	/** Regla: cada paquete cuesta $25. Total = suma * 25 */
	public void recalcularTotal() {
    	this.total = (cantidadVainilla + cantidadCocoa + cantidadCafe) * PRECIO_PAQUETE;
	}

    /** Regla: la entrega es 4 días después del pedido */
	public void recalcularFechaEntrega() {
    	if (fechaPedido != null) {
        	this.fechaEntrega = fechaPedido.plusDays(DIAS_ENTREGA);
        }
	}

	/** Método auxiliar que devuelve solo el total calculado sin modificar el campo */
	public double calcularTotal() {
    	return (cantidadVainilla + cantidadCocoa + cantidadCafe) * PRECIO_PAQUETE;
	}

	@Override
	public String toString() {
    	return "Pedido #" + idPedido + " | Cliente " + idCliente + " | V:" + cantidadVainilla + " C:" + cantidadCocoa + " F:" + cantidadCafe + " | Pedido: " + fechaPedido + " → Entrega: " + fechaEntrega + " | Total: $" + total;
	}
}