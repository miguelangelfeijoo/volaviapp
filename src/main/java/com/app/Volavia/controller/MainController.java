package com.app.Volavia.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;

@RequestMapping("/")
@Controller
public class MainController {
	
	//************************************ INICIO ************************************
	@GetMapping("/")
	public String inicio(Model model) {
		return "login";
	}

	//************************************ VOLVER ************************************
	@GetMapping("/volver")
    public String volver(Model model,HttpSession session) {
		return "login";
    }
    


    

     

    

    




		
	
 
    
 
    
}//
