package com.app.Volavia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.Volavia.model.Expense;


@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long>{
	

}
