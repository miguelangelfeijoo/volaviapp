package com.app.Volavia.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.app.Volavia.model.Story;
import com.app.Volavia.service.StoryService;

@Controller
@RequestMapping("/stories")
public class StoryController {
	
	@Autowired
	StoryService storyService;

    @GetMapping("/guardar-anecdota")
    public String guardarAnecdota(@RequestParam String name,
    							  @RequestParam Long tripId,
    							  Model model) {
        model.addAttribute("countryName", name);
        model.addAttribute("tripId", tripId);
    	return "add-story";
    }
    
    @PostMapping("/guardar-anecdota")
	public String guardarAnecdota(@RequestParam String titulo,
								  @RequestParam String descripcion,
								  @RequestParam("imagen") MultipartFile imagen,	
								  @RequestParam LocalDate fechaAnecdota,
								  @RequestParam String countryName,
								  @RequestParam Long tripId,
								  Model model) {
    	
    	Story anecdota = new Story();
    	anecdota.setTitulo(titulo);
    	anecdota.setDescripcion(descripcion);
    	anecdota.setFechaAnecdota(fechaAnecdota);
    	
        if (!imagen.isEmpty()) {
            try {
                String nombreArchivo = System.currentTimeMillis() + "_" + imagen.getOriginalFilename();
                Path ruta = Paths.get("src/main/resources/static/uploads/" + nombreArchivo);
                Files.createDirectories(ruta.getParent()); // Asegura que la carpeta exista
                Files.write(ruta, imagen.getBytes());

                anecdota.setImagen("/uploads/" + nombreArchivo); // Ruta para la vista
            } catch (IOException e) {
                e.printStackTrace();
                model.addAttribute("error", "Error al guardar la imagen");
                return "travel-diary";
            }
        }
        
    	storyService.guardarAnecdota(anecdota,tripId);
    	model.addAttribute("mensaje", "Anécdota guardada");
    	model.addAttribute("countryName", countryName);
    	model.addAttribute("tripId", tripId);
    	return "redirect:/diary/travel-diary?name=" + URLEncoder.encode(countryName, StandardCharsets.UTF_8)
        + "&tripId=" + tripId;	
    }
    
    @PostMapping("/eliminar-anecdota")
    public String eliminarAnecdota(@RequestParam Long storyId,
    							@RequestParam Long tripId,
                                @RequestParam String countryName,
                                Model model) {

        // Obtener la anécdota antes de eliminarla
        Optional<Story> optionalAnecdota = storyService.findById(storyId);
        
        if (optionalAnecdota.isPresent()) {
        	Story anecdota = optionalAnecdota.get();
            // Eliminar el archivo físico si existe
            String rutaImagen = anecdota.getImagen();
            if (rutaImagen != null && !rutaImagen.isEmpty()) {
                try {
                    Path rutaArchivo = Paths.get("src/main/resources/static" + rutaImagen);
                    Files.deleteIfExists(rutaArchivo);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            
            // Eliminar la anécdota de la base de datos
            storyService.eliminarPorId(storyId);
        }
        
    	model.addAttribute("countryName", countryName);
    	model.addAttribute("tripId", tripId);
       	return "redirect:/diary/travel-diary?name=" + URLEncoder.encode(countryName, StandardCharsets.UTF_8)
        + "&tripId=" + tripId;	
    }
    
    @PostMapping("/actualizar-descripcion")
    	public String actualizarDescripcion(@RequestParam Long storyId,
    										@RequestParam Long tripId,
    										@RequestParam String countryName,
    										@RequestParam String descripcion){
    	
    Story actualizarStory =	storyService.findById(storyId).get();
    actualizarStory.setDescripcion(descripcion);
    storyService.actualizarDescripcion(actualizarStory);
    
    	return "redirect:/diary/travel-diary?name=" + URLEncoder.encode(countryName, StandardCharsets.UTF_8)
    	        + "&tripId=" + tripId;	
    }
    
    	
}
