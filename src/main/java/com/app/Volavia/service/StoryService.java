package com.app.Volavia.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.app.Volavia.model.Diary;
import com.app.Volavia.model.Story;
import com.app.Volavia.model.Trip;
import com.app.Volavia.repository.DiaryRepository;
import com.app.Volavia.repository.StoryRepository;
import com.app.Volavia.repository.TripRepository;

@Service
public class StoryService {

    @Autowired
	StoryRepository storyRepository;
    
    @Autowired
    TripRepository tripRepository;
    
    @Autowired
    DiaryRepository diaryRepository;

	public Story guardarAnecdota(Story anecdota, Long tripId) {
		Trip trip = tripRepository.findById(tripId)
		        .orElseThrow(() -> new RuntimeException("Viaje no encontrado"));

		    Diary diario = diaryRepository.findByTrip(trip);
		    if (diario == null) {
		        diario = new Diary();
		        diario.setTrip(trip);
		        diario = diaryRepository.saveAndFlush(diario);
		    }

		    anecdota.setDiary(diario);
		return storyRepository.saveAndFlush(anecdota);
	}

	public List<Story> findAll() {
		return storyRepository.findAll();
	}

	public List<Story> findAllOrderByFechaAnecdotaDesc() {
		return storyRepository.findAll(Sort.by(Sort.Direction.DESC, "fechaAnecdota"));
	}

	public Page<Story> findByTripIdOrderByFechaAnecdotaDesc(Long tripId, int page, int size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "fechaAnecdota"));
		return storyRepository.findByTripIdOrderByFechaAnecdotaDesc(tripId, pageable);
	}

	public void eliminarPorId(Long storyId) {
		storyRepository.deleteById(storyId);
	}

	public Optional<Story> findById(Long storyId) {
		return storyRepository.findById(storyId);
		
	}

	public Story actualizarDescripcion(Story actualizarStory) {
		return storyRepository.saveAndFlush(actualizarStory);
		
	}

		
}
