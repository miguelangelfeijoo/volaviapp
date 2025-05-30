package com.app.Volavia.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.Volavia.model.Expense;
import com.app.Volavia.repository.ExpenseRepository;

@Service
public class ExpenseService {

    @Autowired
	ExpenseRepository expenseRepository;

    @Transactional
    public boolean añadirGasto(Expense gasto) {
        if (expenseRepository.saveAndFlush(gasto) != null){
            return true;
        }
        return false;
    }

    public void eliminarPorId(Long id) {
        expenseRepository.deleteById(id);
    }

}