package com.app.Volavia.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.app.Volavia.model.Profile;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/auth")
public class AuthController {
	
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    
    @GetMapping("/login")
    public String login() {
        return "login";
    }
    
    @GetMapping("/user-login")
    public String inicio(Model model) {
        return "login";
    }
    
    @GetMapping("/redirectProfile")
    public String redirectProfile(Authentication authentication, HttpSession session) {
        logger.info("Iniciando proceso de redirección para usuario: {}", authentication.getName());
        
        session.setAttribute("usuario", authentication.getName());
        
        // Obtener el perfil del usuario autenticado
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            String perfil = authority.getAuthority(); 
            logger.info("Perfil del usuario: {}", perfil);
            session.setAttribute("perfil", perfil.equals("ROLE_ADMINISTRATOR") ? Profile.ADMINISTRATOR : Profile.USER);
            return "redirect:/map/show";
        }

        logger.warn("No se encontraron autoridades para el usuario: {}", authentication.getName());
        return "redirect:/auth/user-login";
    }
    
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        logger.info("Cerrando sesión para usuario: {}", session.getAttribute("usuario"));
        session.removeAttribute("usuario");
        session.removeAttribute("perfil");
        session.invalidate();
        return "redirect:/auth/user-login";
    }
    
    @GetMapping("/denied-access")
    public String accesoDenegado(){
        logger.warn("Acceso denegado");
        return "denied-access";
    }
    
    @GetMapping("/error-login")
    public String errorLogin(Model model) {
        logger.warn("Error en el login - Credenciales incorrectas");
        model.addAttribute("error", "Usuario o contraseña incorrectos. Por favor, inténtalo de nuevo.");
        return "login";
    }
    
    @GetMapping("/current-user")
    @ResponseBody
    public String getCurrentUser(HttpSession session) {
        String username = (String) session.getAttribute("usuario");
        return username != null ? username : "";
    }
    
}
