package poo.roscos;

import java.time.LocalDate;

public class Pedido {
	private int idPedido;
	private int idCliente;
	private int cantidadVainilla;
	private int cantidadCocoa;
	private int cantidadCafe;
	private String direccionEntrega;
	private LocalDate fechaPedido;
	private LocalDate fechaEntrega;
	private double total;
	private String estado;

	public static final double PRECIO_PAQUETE = 25.00;
	public static final int DIAS_ENTREGA = 4;

	public Pedido() {
		this.estado = "Pendiente"; // Estado predeterminado.
	}

	public Pedido(int idCliente, int cantidadVainilla, int cantidadCocoa, int cantidadCafe, String direccionEntrega, LocalDate fechaPedido, LocalDate fechaEntrega) {
		this.idCliente = idCliente;
		this.cantidadVainilla = cantidadVainilla;
		this.cantidadCocoa = cantidadCocoa;
		this.cantidadCafe = cantidadCafe;
		this.direccionEntrega = direccionEntrega;
		this.fechaPedido = fechaPedido;
		this.fechaEntrega = fechaEntrega;
		this.total = (cantidadVainilla + cantidadCocoa + cantidadCafe) * PRECIO_PAQUETE;
		this.estado = "Pendiente";
	}

	// Getters y Setters
	public int getIdPedido() { return idPedido; }
	public void setIdPedido(int idPedido) { this.idPedido = idPedido; }

	public int getIdCliente() { return idCliente; }
	public void setIdCliente(int idCliente) { this.idCliente = idCliente; }

	public int getCantidadVainilla() { return cantidadVainilla; }
	public void setCantidadVainilla(int cantidadVainilla) { 
		this.cantidadVainilla = cantidadVainilla;
		recalcularTotal();
	}

	public int getCantidadCocoa() { return cantidadCocoa; }
	public void setCantidadCocoa(int cantidadCocoa) { 
		this.cantidadCocoa = cantidadCocoa;
		recalcularTotal();
	}

	public int getCantidadCafe() { return cantidadCafe; }
	public void setCantidadCafe(int cantidadCafe) { 
		this.cantidadCafe = cantidadCafe;
		recalcularTotal();
	}

	public String getDireccionEntrega() { return direccionEntrega; }
	public void setDireccionEntrega(String direccionEntrega) { this.direccionEntrega = direccionEntrega; }

	public LocalDate getFechaPedido() { return fechaPedido; }
	public void setFechaPedido(LocalDate fechaPedido) { 
		this.fechaPedido = fechaPedido;
		if (fechaPedido != null) {
			this.fechaEntrega = fechaPedido.plusDays(DIAS_ENTREGA);
		}
	}

	public LocalDate getFechaEntrega() { return fechaEntrega; }
	public void setFechaEntrega(LocalDate fechaEntrega) { this.fechaEntrega = fechaEntrega; }

	public double getTotal() { return total; }
	public void setTotal(double total) { this.total = total; }

	public String getEstado() { return estado; }
	public void setEstado(String estado) { this.estado = estado; }

	private void recalcularTotal() {
		this.total = (this.cantidadVainilla + this.cantidadCocoa + this.cantidadCafe) * PRECIO_PAQUETE;
	}

	@Override
	public String toString() {
		return "Pedido [ID=" + idPedido + ", Cliente=" + idCliente + ", Total=" + total + ", Estado=" + estado + "]";
	}
}