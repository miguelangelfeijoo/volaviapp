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
		String userRegex = "^[a-zA-Z0-9]+$";
		Pattern pattern = Pattern.compile(userRegex);
		Matcher matcher = pattern.matcher(usuario);
		if (usuario == null || usuario.isEmpty() || usuario.contains(" ") || !matcher.matches()) {
			return false;
		}
		return true;
	}

	/**
	 * Valida que la contraseña cumpla con la expresión regular tenga al menos 5
	 * caracteres alfanuméricos no sea nula, no este vacía y no contenga espacios
	 * 
	 * @param password
	 * @return boolean indicando si es válido o no
	 */
	public static boolean validarContrasena(String password) {
		String passwordRegex = "^[a-zA-Z0-9]+$";
		Pattern pattern = Pattern.compile(passwordRegex);
		Matcher matcher = pattern.matcher(password);

		if (password == null || password.isEmpty() || password.contains(" ")
				|| !matcher.matches()) {
			return false;
		}

		return true;
	}

	/**
	 * Valida que el email cumpla con la expresión regular que no sea nulo, no este
	 * vacío y no contenga espacios
	 * 
	 * @param email
	 * @return boolean indicando si es válido o no
	 */
	public static boolean validarEmail(String email) {
		String emailRegex = "^[a-z0-9_.]+@+[a-z]+\\.[a-z]{2,3}$";
		Pattern pattern = Pattern.compile(emailRegex);
		Matcher matcher = pattern.matcher(email);

		if (email == null || email.isEmpty() || !matcher.matches() || email.contains(" ")) {
			return false;
		}

		return true;
	}



}
