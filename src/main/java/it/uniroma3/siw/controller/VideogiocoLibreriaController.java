package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.model.VideogiocoLibreria;
import it.uniroma3.siw.service.VideogiocoLibreriaService;

@Controller
public class VideogiocoLibreriaController {

    @Autowired
    private VideogiocoLibreriaService videogiocoLibreriaService;

    // Questa rotta mostrerà tutti i giochi aggiunti nelle librerie
    @GetMapping("/libreria")
    public String showLibreria(Model model) {
        model.addAttribute("elementiLibreria", videogiocoLibreriaService.findAll());
        return "libreria";
    }

    // Questa rotta gestisce il salvataggio di un nuovo gioco nella libreria
    @PostMapping("/libreria/aggiungi")
    public String addVideogiocoLibreria(@ModelAttribute VideogiocoLibreria videogiocoLibreria) {
        videogiocoLibreriaService.save(videogiocoLibreria);
        // Dopo averlo salvato, reindirizziamo l'utente alla pagina del gioco appena aggiunto
        return "redirect:/videogioco/" + videogiocoLibreria.getVideogioco().getId();
    }
}