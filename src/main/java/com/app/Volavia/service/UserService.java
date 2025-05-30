package com.app.Volavia.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.Volavia.model.User;
import com.app.Volavia.repository.UserRepository;
import com.app.Volavia.validation.Validation;

@Service
public class UserService {

	@Autowired
	UserRepository userRepository;
	
	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
	
	@Transactional 
	public String registerUser(User user) {
		StringBuilder mensaje = new StringBuilder();
		boolean valido = true;
		
		if(!Validation.validarEmail(user.getEmail())){
			mensaje.append("El campo email no es válido. ");
			valido = false;
		}
		if(!Validation.validarUsuario(user.getUsuario())) {
			mensaje.append("El campo usuario no es válido. ");
			valido = false;
		}
		if(!Validation.validarContrasena(user.getContraseña())){
			mensaje.append("El campo contraseña no es válido. ");
			valido = false;
		} else {
			user.setContraseña(encoder.encode(user.getContraseña()));
		}
		
		if (valido) {
			boolean emailExiste = userRepository.findByEmail(user.getEmail()) != null;
			boolean usuarioExiste = userRepository.findByUsuario(user.getUsuario()) != null;
			
			if (emailExiste) {
				mensaje.append("El email ").append(user.getEmail().toUpperCase()).append(" ya existe. Prueba con otro. ");
			}
			if (usuarioExiste) {
				mensaje.append("El usuario ").append(user.getUsuario().toUpperCase()).append(" ya existe. Prueba con otro. ");
			}
			if (emailExiste || usuarioExiste) {
				return mensaje.toString();
			}
			
			try {
				User p = userRepository.saveAndFlush(user);
				if (p != null) {
					mensaje.append("Registro exitoso.");
				}
			} catch (Exception e) {
				mensaje.append("Error al registrar. Intenta nuevamente");
			}
		}
		
	    return mensaje.toString();
	}

	public User findByUsuario(String usuario) {
		return userRepository.findByUsuario(usuario);
	}
	
	public List<User> findAll() {
		return userRepository.findAll();
	}

	public void eliminarUsuario(Long userId) {
		userRepository.deleteById(userId);
		
	}

	public Optional<User> findById(Long userId) {
		return userRepository.findById(userId);
	}

}
