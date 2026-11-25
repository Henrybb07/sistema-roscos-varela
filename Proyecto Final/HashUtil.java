package poo.roscos;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class HashUtil {

	private static final String SALT = "RoscosVarelaSalt2025";

	private HashUtil() { }

	/** Devuelve el hash hex (64 caracteres) de la contraseña con salt. */
	public static String hashear(String passwordEnClaro) {
		if (passwordEnClaro == null) return null;
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			String conSalt = SALT + passwordEnClaro;
			byte[] bytes = md.digest(conSalt.getBytes(StandardCharsets.UTF_8));
			StringBuilder sb = new StringBuilder();
			for (byte b : bytes) {
				sb.append(String.format("%02x", b));
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException("SHA-256 no disponible", e);
		}
	}

	/** Compara una contraseña en claro con un hash guardado. */
	public static boolean coincide(String passwordEnClaro, String hashGuardado) {
		if (passwordEnClaro == null || hashGuardado == null) return false;
		return hashear(passwordEnClaro).equalsIgnoreCase(hashGuardado);
	}
}