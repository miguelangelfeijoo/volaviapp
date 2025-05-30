package com.app.Volavia.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.app.Volavia.model.Story;

@Repository
public interface StoryRepository extends JpaRepository<Story, Long> {

	List<Story> findAllByOrderByFechaAnecdotaDesc();
	
	@Query("SELECT s FROM Story s WHERE s.diary.trip.id = :tripId ORDER BY s.fechaAnecdota DESC")
	Page<Story> findByTripIdOrderByFechaAnecdotaDesc(@Param("tripId") Long tripId, Pageable pageable);
	
}
