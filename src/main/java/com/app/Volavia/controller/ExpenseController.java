package com.app.Volavia.controller;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
//import org.springframework.transaction.annotation.Transactional; // Removed
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.app.Volavia.model.Expense;
import com.app.Volavia.model.ExpenseCategory;
import com.app.Volavia.model.Trip;
import com.app.Volavia.service.ExpenseService;
import com.app.Volavia.service.TripService;

@Controller
@RequestMapping("/expenses")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private TripService tripService;

    //@Transactional // Removed
    @PostMapping("/addExpense")
    public String añadirGasto(@RequestParam String descripcion, 
                              @RequestParam double importe,
                              @RequestParam ExpenseCategory category,
                              @RequestParam Long tripId,
                              @RequestParam String countryName,
                              Model model) {

        // Call the service method to add the expense
        expenseService.addExpenseToTrip(descripcion, importe, category, tripId);

        // Actualizar viaje y configurar el model.
        // configurarViajeYModel still needs tripId and countryName, and uses tripService.
        // The initial check for trip existence for adding expense is now in the service.
        // If tripService.findById in configurarViajeYModel fails, it will set an error attribute.
        configurarViajeYModel(tripId, countryName, model);
        
        return "plan-your-trip";
    }

    @PostMapping("/deleteExpense")
    public String eliminarGasto(@RequestParam Long expenseId,
                                @RequestParam Long tripId,
                                @RequestParam String countryName,
                                Model model) {

        // Eliminar el gasto
        expenseService.eliminarPorId(expenseId);
        
        // Actualizar viaje y configurar el model
        configurarViajeYModel(tripId, countryName, model);
        
        return "plan-your-trip";
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
