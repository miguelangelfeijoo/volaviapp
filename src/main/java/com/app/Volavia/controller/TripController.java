package com.app.Volavia.controller;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.app.Volavia.model.Expense;
import com.app.Volavia.model.Trip;
import com.app.Volavia.model.User;
import com.app.Volavia.service.TripService;
import com.app.Volavia.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/trips")
public class TripController {
	
	@Autowired
	private TripService tripService;
	
	@Autowired
	private UserService userService;

    
    @GetMapping("/plan-your-trip")
    public String planificarViaje(@RequestParam String name, Model model, HttpSession session) {
    	User user = userService.findByUsuario((String)session.getAttribute("usuario"));
    	Trip trip;
    	Optional<Trip> optionalTrip = tripService.findByNombrePaisAndUser(name, user);

    	if (optionalTrip.isPresent()) {
    	    trip = optionalTrip.get();
    	} else {
    	    trip = new Trip();
    	    trip.setNombrePais(name);
    	    trip.setImporteTotal(0.0);
    	    trip.setUser(user);
    	    tripService.guardarViaje(trip);
    	}
    	
    	configurarViajeYModel(trip.getId(), name, model);
    	
        return "plan-your-trip";
    }

    
    @PostMapping("/guardarVisita")
    public String guardarVisita(@RequestParam Long tripId,
						        @RequestParam String countryName,
						        @RequestParam LocalDate fechaSalida,
						        @RequestParam LocalDate fechaLlegada,
						        Model model,
						        HttpSession session) {
    	
    	User user = userService.findByUsuario((String)session.getAttribute("usuario"));
    	Trip trip = tripService.findByNombrePaisAndUser(countryName, user).get();
    	
    	trip.setFechaSalida(fechaSalida);
    	trip.setFechaLlegada(fechaLlegada);
    	tripService.guardarViaje(trip);

    	configurarViajeYModel(trip.getId(), countryName, model);
    	model.addAttribute("mensaje", "Visita guardada con éxito!");
    	return "plan-your-trip";
//    	return "redirect:/trips/plan-your-trip?name=" + URLEncoder.encode(countryName, StandardCharsets.UTF_8);
    }
    
    //Metodo para enviar valores de las categorias de los gastos al Graphic.js
    //Anotacion @ResponseBody para devolver una lista y no una plantilla Thymeleaf
    @GetMapping("/valores-grafico")
    @ResponseBody
    public List<Double> obtenerValoresGrafico(@RequestParam Long tripId, HttpSession session) {
        Optional<Trip> optionalTrip = tripService.findById(tripId);

        if (optionalTrip.isEmpty()) {
            return List.of(0.0, 0.0, 0.0, 0.0);
        }

        Trip trip = optionalTrip.get();

        double vuelos = 0.0;
        double alojamiento = 0.0;
        double transporte = 0.0;
        double varios = 0.0;

        for (Expense expense : trip.getExpenses()) {
            if (expense.getCategory() == null || expense.getImporte() == null) continue;

            switch (expense.getCategory()) {
                case FLIGHT -> vuelos += expense.getImporte();
                case HOTELS -> alojamiento += expense.getImporte();
                case TRANSPORT -> transporte += expense.getImporte();
                case OTHER -> varios += expense.getImporte();
            }
        }

        return List.of(vuelos, alojamiento, transporte, varios);
    }

    
    
    // -------------- FUNCION AUXILIAR --------------
    
    private void configurarViajeYModel(Long tripId, String countryName, Model model) {
        
    	// Obtener el viaje actualizado si existe
        Optional<Trip> optionalTrip = tripService.findById(tripId);
        if (!optionalTrip.isPresent()) {
            model.addAttribute("error", "El viaje no existe.");
        } else {
	        Trip trip = optionalTrip.get();
	
	        // Recalcular el importe total
	        double nuevoImporteTotal = 0.0;
	        for (Expense gasto : trip.getExpenses()) {
	            nuevoImporteTotal += gasto.getImporte();
	        }
	        trip.setImporteTotal(nuevoImporteTotal);
	
	        // Guardar el viaje actualizado
	        tripService.guardarViaje(trip);
	
	        // Calcular los días restantes
	        long diasRestantes = 0;
	        if (trip.getFechaSalida() != null) {
	            LocalDate hoy = LocalDate.now();
	            diasRestantes = ChronoUnit.DAYS.between(hoy, trip.getFechaSalida());
	        }
	
	        // Ordenar los gastos por categoría
	        List<Expense> gastosOrdenados = new LinkedList<>(trip.getExpenses());
	        gastosOrdenados.sort(new Comparator<Expense>() {
	            @Override
	            public int compare(Expense e1, Expense e2) {
	                return e1.getCategory().compareTo(e2.getCategory());
	            }
	        });
	
	        // Añadir datos al modelo
	        model.addAttribute("trip", trip);
	        model.addAttribute("countryName", countryName);
	        model.addAttribute("diasRestantes", diasRestantes);
	        model.addAttribute("gastosOrdenados", gastosOrdenados);
        }
    }


}
