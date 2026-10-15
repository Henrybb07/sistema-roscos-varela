package poo.roscos;

public class Cliente {
	private int idCliente;
	private String nombreCafeteria;
	private String direccion;
	private String contacto;

    // Constructor vacío
	public Cliente() {
	}

    // Constructor con parámetros (útil para consultas)
	public Cliente(int idCliente, String nombreCafeteria, String direccion, String contacto) {
		this.idCliente = idCliente;
		this.nombreCafeteria = nombreCafeteria;
		this.direccion = direccion;
		this.contacto = contacto;
	}

    // Constructor sin ID (útil para inserciones antes de que la BD genere el Auto_Increment)
	public Cliente(String nombreCafeteria, String direccion, String contacto) {
		this.nombreCafeteria = nombreCafeteria;
		this.direccion = direccion;
		this.contacto = contacto;
	}

    // Getters y Setters
	public int getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(int idCliente) {
		this.idCliente = idCliente;
	}

	public String getNombreCafeteria() {
		return nombreCafeteria;
	}

	public void setNombreCafeteria(String nombreCafeteria) {
		this.nombreCafeteria = nombreCafeteria;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getContacto() {
		return contacto;
	}

	public void setContacto(String contacto) {
		this.contacto = contacto;
	}

	// toString (útil para depuración o pruebas rápidas)
	@Override
	public String toString() {
    	return "Cliente [ID=" + idCliente + ", Cafetería=" + nombreCafeteria + ", Dirección=" + direccion + ", Contacto=" + contacto + "]";
	}
}
