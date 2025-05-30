package com.app.Volavia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.Volavia.model.Diary;
import com.app.Volavia.model.Trip;

@Repository
public interface DiaryRepository extends JpaRepository<Diary, Long>  {

	Diary findByTrip(Trip trip);

	
	
}
