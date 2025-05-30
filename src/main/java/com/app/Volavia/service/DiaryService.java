package com.app.Volavia.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.Volavia.model.Diary;
import com.app.Volavia.model.Trip;
import com.app.Volavia.repository.DiaryRepository;

@Service
public class DiaryService {

	@Autowired
	DiaryRepository diaryRepository;

	public Diary findByTrip(Trip trip) {
		return diaryRepository.findByTrip(trip);
	}

	public void delete(Diary diario) {
		diaryRepository.delete(diario);
	}
	

}
