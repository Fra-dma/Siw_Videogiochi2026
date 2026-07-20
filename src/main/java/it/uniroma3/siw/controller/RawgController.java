package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import it.uniroma3.siw.service.RawgApiService;
import it.uniroma3.siw.service.VideogiocoLibreriaService;

@Controller
public class RawgController {

    @Autowired
    private RawgApiService rawgApiService;
    
    @Autowired
    private VideogiocoLibreriaService videogiocoLibreriaService;

    // Rotta dei giochi popolari
    @GetMapping("/rawg/popolari")
    public String showPopularGames(Model model) {
        model.addAttribute("giochi", rawgApiService.getPopularGames());
        return "rawg_popolari";
    }

    // Rotta per mostrare i dettagli di un singolo gioco
    @GetMapping("/rawg/gioco/{id}")
    public String showGameDetails(@PathVariable("id") Long id, Model model) {
        model.addAttribute("gioco", rawgApiService.getGameDetails(id));
        Long idUtenteAttuale = 1L; 
        boolean inLibreria = videogiocoLibreriaService.esisteGiocoDaRawgInLibreria(idUtenteAttuale, id);
        model.addAttribute("isInLibreria", inLibreria);
        return "rawg_dettagli";
    }
}
