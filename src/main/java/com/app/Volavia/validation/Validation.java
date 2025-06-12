package com.app.Volavia.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Validation {

	/**
	 * Valida que el nombre del usuario no sea nulo, no este vacio y no contenga
	 * espacios.
	 * 
	 * @param nombre
	 * @return boolean indicando si es válido o no
	 */
	public static boolean validarUsuario(String usuario) {
		if (usuario == null) {
			return false;
		}
		String userRegex = "^[a-zA-Z0-9]+$";
		Pattern pattern = Pattern.compile(userRegex);
		Matcher matcher = pattern.matcher(usuario);
		if (usuario.isEmpty() || usuario.contains(" ") || !matcher.matches()) {
			return false;
		}
		return true;
	}

	/**
	 * Valida que la contraseña cumpla con los siguientes criterios:
	 * - Al menos 8 caracteres de longitud.
	 * - Contiene caracteres alfanuméricos y puede incluir caracteres especiales comunes (e.g., !@#$%^&*).
	 * - No es nula, no está vacía y no contiene espacios.
	 * 
	 * @param password La contraseña a validar.
	 * @return {@code true} si la contraseña es válida, {@code false} en caso contrario.
	 */
	public static boolean validarContrasena(String password) {
		if (password == null) {
			return false;
		}
		String passwordRegex = "^[a-zA-Z0-9!@#$%^&*]+$";
		Pattern pattern = Pattern.compile(passwordRegex);
		Matcher matcher = pattern.matcher(password);

		if (password.isEmpty() || password.contains(" ") || password.length() < 8
				|| !matcher.matches()) {
			return false;
		}

		return true;
	}

	/**
	 * Valida que el email cumpla con un formato estándar.
	 * La expresión regular utilizada es: ^[a-zA-Z0-9_+&*-]+(?:\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\.)+[a-zA-Z]{2,7}$
	 * Además, valida que el email no sea nulo, no esté vacío y no contenga espacios.
	 * 
	 * @param email El email a validar.
	 * @return {@code true} si el email es válido, {@code false} en caso contrario.
	 */
	public static boolean validarEmail(String email) {
		if (email == null) {
			return false;
		}
		// Regex updated to prevent domain parts from starting or ending with a hyphen.
		String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?\\.)+[a-zA-Z]{2,7}$";
		Pattern pattern = Pattern.compile(emailRegex);
		Matcher matcher = pattern.matcher(email);

		if (email.isEmpty() || !matcher.matches() || email.contains(" ")) {
			return false;
		}

		return true;
	}



}
