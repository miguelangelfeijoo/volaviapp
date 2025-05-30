package com.app.Volavia.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.app.Volavia.model.Trip;
import com.app.Volavia.model.User;
import com.app.Volavia.service.TripService;
import com.app.Volavia.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/map")
public class MapController {
	
	@Autowired
	private TripService tripService;
	
	@Autowired
	private UserService userService;
	
    @GetMapping("/show")
    public String mostrarMapa() {
    	return "map";
    }
    
    @GetMapping("/visitados")
    @ResponseBody
    public List<Map<String, String>> getVisitedCountriesByUser(User username, HttpSession session) {
    	User user = userService.findByUsuario((String)session.getAttribute("usuario"));
        List<Trip> trips = tripService.findByUser(user);

        List<Map<String, String>> response = new ArrayList<>();
        for (Trip trip : trips) {
            Map<String, String> map = new HashMap<>();
            // Usa ISO_A2 o algún identificador compatible con tu GeoJSON
            map.put("countryName", trip.getNombrePais()); // Este nombre debe coincidir con el código que espera el mapa
            response.add(map);
        }

        return response;
    }
    
}
