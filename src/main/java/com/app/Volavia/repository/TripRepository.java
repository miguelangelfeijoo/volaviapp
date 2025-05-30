package com.app.Volavia.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.Volavia.model.Trip;
import com.app.Volavia.model.User;


@Repository
public interface TripRepository extends JpaRepository<Trip, Long>{
	
    Optional<Trip> findById(Long id);
	Optional<Trip> findByNombrePaisAndUser(String countryName, User user);
	List<Trip> findByUser(User user);
}
