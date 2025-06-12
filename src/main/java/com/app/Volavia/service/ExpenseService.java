package com.app.Volavia.service;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.Volavia.model.Expense;
import com.app.Volavia.model.ExpenseCategory;
import com.app.Volavia.model.Trip;
import com.app.Volavia.repository.ExpenseRepository;
import com.app.Volavia.repository.TripRepository; // Added import

@Service
public class ExpenseService {

    @Autowired
	ExpenseRepository expenseRepository;

	@Autowired // Added TripRepository
	TripRepository tripRepository;

    @Transactional
    public boolean añadirGasto(Expense gasto) { // This method can remain for other uses or be refactored later if not needed
        if (expenseRepository.saveAndFlush(gasto) != null){
            return true;
        }
        return false;
    }

    @Transactional
    public void addExpenseToTrip(String descripcion, double importe, ExpenseCategory category, Long tripId) {
        Optional<Trip> optionalTrip = tripRepository.findById(tripId);
        if (!optionalTrip.isPresent()) {
            // Handle error: Trip not found. For now, just log or throw an exception.
            // For simplicity in this step, we'll assume the trip exists or controller handles this.
            // In a real app, you might throw a custom exception like TripNotFoundException.
            System.err.println("Trip not found with ID: " + tripId); // Basic error handling
            return;
        }

        Trip trip = optionalTrip.get();

        Expense expense = new Expense();
        expense.setDescripcion(descripcion);
        expense.setImporte(importe);
        expense.setCategory(category);
        expense.setTrip(trip);

        expenseRepository.save(expense); // Using save() as saveAndFlush() might be unnecessary unless immediate flush is critical
    }

    public void eliminarPorId(Long id) {
        expenseRepository.deleteById(id);
    }

}