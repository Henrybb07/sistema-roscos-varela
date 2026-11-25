package poo.roscos;

public enum Rol {
	ADMINISTRADOR,
	PRODUCCION,
	VENTAS;

	/** Texto bonito para mostrar en la UI. */
	public String etiquetaBonita() {
		switch (this) {
		case ADMINISTRADOR: return "Administrador (Gerente)";
		case PRODUCCION:    return "Encargado de Producción";
		case VENTAS:        return "Encargado de Ventas";
		default:            return name();
		}
	}

	/** Convierte el string guardado en la BD a Rol. Devuelve null si no es válido. */
	public static Rol desdeString(String s) {
		if (s == null) return null;
		try {
			return Rol.valueOf(s.trim().toUpperCase());
		} catch (IllegalArgumentException e) {
			return null;
		}
	}
}