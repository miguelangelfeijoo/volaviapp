package com.app.Volavia.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.Volavia.model.Trip;
import com.app.Volavia.model.User;
import com.app.Volavia.repository.TripRepository;

@Service
public class TripService {

    @Autowired
	TripRepository tripRepository;

    @Transactional
    public Optional<Trip> findById(Long id) {
        return tripRepository.findById(id);
    }

	public Trip guardarViaje(Trip trip) {
		return tripRepository.saveAndFlush(trip);
	}

	public Optional<Trip> findByNombrePaisAndUser(String countryName, User user) {
		return tripRepository.findByNombrePaisAndUser(countryName, user);
		
	}

	public List<Trip> findByUser(User user) {
		return tripRepository.findByUser(user);
	}

	public void deleteAll(List<Trip> trips) {
		tripRepository.deleteAll(trips);
	}
}