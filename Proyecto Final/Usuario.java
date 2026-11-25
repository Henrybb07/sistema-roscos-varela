package poo.roscos;

public class Usuario {

	private int idUsuario;
	private String nombreUsuario;
	private String passwordHash;
	private Rol rol;
	private boolean activo;

	public Usuario() {
		this.activo = true;
	}

	public Usuario(int idUsuario, String nombreUsuario, String passwordHash, Rol rol, boolean activo) {
		this.idUsuario = idUsuario;
		this.nombreUsuario = nombreUsuario;
		this.passwordHash = passwordHash;
		this.rol = rol;
		this.activo = activo;
	}

	public int getIdUsuario() { return idUsuario; }
	public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

	public String getNombreUsuario() { return nombreUsuario; }
	public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }

	public String getPasswordHash() { return passwordHash; }
	public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

	public Rol getRol() { return rol; }
	public void setRol(Rol rol) { this.rol = rol; }

	public boolean isActivo() { return activo; }
	public void setActivo(boolean activo) { this.activo = activo; }

    @Override
	public String toString() {
    	return "Usuario [id=" + idUsuario + ", nombre=" + nombreUsuario + ", rol=" + rol + ", activo=" + activo + "]";
	}
}