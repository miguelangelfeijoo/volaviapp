package com.app.Volavia.service;

import java.util.ArrayList; // Added
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.Volavia.model.Diary;
import com.app.Volavia.model.Trip;
import com.app.Volavia.model.User;
import com.app.Volavia.repository.UserRepository;
import com.app.Volavia.validation.Validation;

@Service
public class UserService {

	private final UserRepository userRepository; // Made final
	private final DiaryService diaryService; // Made final
	private final BCryptPasswordEncoder encoder;
	
	@Autowired
    public UserService(UserRepository userRepository, DiaryService diaryService, BCryptPasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.diaryService = diaryService;
        this.encoder = encoder;
    }

	@Transactional 
	public List<String> registerUser(User user) { // Changed return type
		List<String> mensajes = new ArrayList<>(); // Changed to List<String>
		boolean valido = true;
		
		if(!Validation.validarEmail(user.getEmail())){
			mensajes.add("El campo email no es válido."); // Changed to add
			valido = false;
		}
		if(!Validation.validarUsuario(user.getUsuario())) {
			mensajes.add("El campo usuario no es válido."); // Changed to add
			valido = false;
		}
		if(!Validation.validarContrasena(user.getContraseña())){
			mensajes.add("El campo contraseña no es válido."); // Changed to add
			valido = false;
		} else {
			user.setContraseña(encoder.encode(user.getContraseña()));
		}
		
		if (valido) {
			boolean emailExiste = userRepository.findByEmail(user.getEmail()) != null;
			boolean usuarioExiste = userRepository.findByUsuario(user.getUsuario()) != null;
			
			if (emailExiste) {
				mensajes.add("El email " + user.getEmail().toUpperCase() + " ya existe. Prueba con otro."); // Changed to add
			}
			if (usuarioExiste) {
				mensajes.add("El usuario " + user.getUsuario().toUpperCase() + " ya existe. Prueba con otro."); // Changed to add
			}

			// If errors were added due to existing email/user, return them now
			if (!mensajes.isEmpty()) {
				return mensajes;
			}
			
			try {
				User p = userRepository.saveAndFlush(user);
				if (p != null) {
					mensajes.add("Registro exitoso."); // Added success message
				} else {
					// This case might be redundant if saveAndFlush throws an exception on failure
					mensajes.add("Error al registrar. No se pudo guardar el usuario.");
				}
			} catch (Exception e) {
				// Log the exception e.getMessage() or e.printStackTrace() for debugging
				mensajes.add("Error al registrar. Intenta nuevamente más tarde."); // Changed to add
			}
		}
		// If !valido, messages for invalid fields would have been added already.
		// If 'valido' but save failed, error message is added.
		// If 'valido' and save succeeded, success message is added.
	    return mensajes;
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

	@Transactional
	public void deleteUserAndAssociatedData(Long userId) {
		Optional<User> userOpt = userRepository.findById(userId);
		if (userOpt.isPresent()) {
			User user = userOpt.get();
			for (Trip trip : user.getTrips()) {
				Diary diary = diaryService.findByTrip(trip);
				if (diary != null) {
					diaryService.delete(diary);
				}
			}
			userRepository.deleteById(userId);
		}
	}

	public Optional<User> findById(Long userId) {
		return userRepository.findById(userId);
	}

}
