package com.app.Volavia.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.app.Volavia.model.Diary;
import com.app.Volavia.model.Profile;
import com.app.Volavia.model.Trip;
import com.app.Volavia.model.User;
import com.app.Volavia.service.DiaryService;
import com.app.Volavia.service.TripService;
import com.app.Volavia.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {
	
    @Autowired
    private UserService userService;
    
    @Autowired
    private TripService tripService;
    
    @Autowired
    private DiaryService diaryService;
    
    @PostMapping("/register")
    public String registerUser(@RequestParam String username,
					    	   @RequestParam String password, 
					    	   @RequestParam String email, 
					    	   Model model) {
    	User user = new User(email, username, password, Profile.USER);
    	String resultado = userService.registerUser(user);
    	model.addAttribute("mensajeResultado", resultado);
    	if(resultado.equals("Registro exitoso.")) {
    		model.addAttribute("res", "ok");
    	}else {
    		model.addAttribute("res", "ko");
    	}
    	return "register";
    }
    
	@GetMapping("/register")
	 public String registerUser() {
		return "register";
	}
	
	@GetMapping("/manage")
	public String userManagement(Model model) {
		List<User> listaUsuarios = userService.findAll();
		model.addAttribute("usuarios", listaUsuarios);
		return "user-management";
	}
	
	@Transactional
    @PostMapping("/delete")
    public String eliminarUsuario(@RequestParam Long userId,Model model) {
    	Optional<User> usuarioOpt = userService.findById(userId);
    	if (usuarioOpt.isPresent()) {
    		User usuario = usuarioOpt.get();
            for (Trip trip : usuario.getTrips()) {
                // Elimina primero los diarios asociados a cada trip
                Diary diario = diaryService.findByTrip(trip);
                if (diario != null) {
                	diaryService.delete(diario);
                }
            }
            tripService.deleteAll(usuario.getTrips());
    	}
    	
    	userService.eliminarUsuario(userId);
    	List<User> listaUsuarios = userService.findAll();
		model.addAttribute("usuarios", listaUsuarios);
        return "redirect:/user/manage";
    }

}
