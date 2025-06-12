package com.app.Volavia.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.app.Volavia.model.Profile;
import com.app.Volavia.model.User;
import com.app.Volavia.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {
	
    @Autowired
    private UserService userService;
    
    
    @PostMapping("/register")
    public String registerUser(@RequestParam String username,
					    	   @RequestParam String password, 
					    	   @RequestParam String email, 
					    	   Model model) {
    	User user = new User(email, username, password, Profile.USER);
	List<String> resultadoMessages = userService.registerUser(user); // Changed to List<String>

	model.addAttribute("mensajesResultado", resultadoMessages); // Add the whole list for display

	// Check if the list contains the success message and only that message
	if (resultadoMessages.size() == 1 && resultadoMessages.contains("Registro exitoso.")) {
    		model.addAttribute("res", "ok");
	} else {
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
	
    @PostMapping("/delete")
    public String eliminarUsuario(@RequestParam Long userId, Model model) {
	userService.deleteUserAndAssociatedData(userId);
	// No es necesario recargar la lista aquí, ya que redirect:/user/manage lo hará.
        return "redirect:/user/manage";
    }

}
