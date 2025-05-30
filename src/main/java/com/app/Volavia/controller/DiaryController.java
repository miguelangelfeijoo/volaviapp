package com.app.Volavia.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.app.Volavia.model.Story;
import com.app.Volavia.service.StoryService;

@Controller
@RequestMapping("/diary")
public class DiaryController {

	@Autowired
	StoryService storyService;
	
    @GetMapping("/travel-diary")
    public String diarioViaje(@RequestParam String name,
    						  @RequestParam Long tripId,
    						  @RequestParam(defaultValue = "0") int page,
    						  @RequestParam(defaultValue = "6") int size,
    						  Model model) {
    	Page<Story> stories = storyService.findByTripIdOrderByFechaAnecdotaDesc(tripId, page, size);
    	
    	model.addAttribute("countryName", name);
    	model.addAttribute("tripId", tripId);
    	model.addAttribute("stories", stories);
    	model.addAttribute("currentPage", page);
    	model.addAttribute("totalPages", stories.getTotalPages());
    	model.addAttribute("totalItems", stories.getTotalElements());
    	model.addAttribute("pageSize", size);
    	
    	return "travel-diary";
    }
}
