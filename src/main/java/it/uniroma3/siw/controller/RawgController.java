package it.uniroma3.siw.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import it.uniroma3.siw.service.RawgApiService;
import it.uniroma3.siw.service.VideogiocoLibreriaService;

@Controller
public class RawgController {

    private final RawgApiService rawgApiService;
    private final VideogiocoLibreriaService videogiocoLibreriaService;

    public RawgController(RawgApiService rawgApiService, VideogiocoLibreriaService videogiocoLibreriaService) {
        this.rawgApiService = rawgApiService;
        this.videogiocoLibreriaService = videogiocoLibreriaService;
    }

    // Questa rotta restituisce la vista dei giochi popolari
    @GetMapping("/rawg/popolari")
    public String showPopularGames(Model model) {
        model.addAttribute("giochi", rawgApiService.getPopularGames());
        return "rawg_popolari"; // Restituisce la vista rawg_popolari.html
    }

    // Questa rotta mostra i dettagli di un singolo gioco preso da RAWG tramite il suo ID
    @GetMapping("/rawg/gioco/{id}")
    public String showGameDetails(@PathVariable("id") Long id, Model model) {
        // Passiamo i dettagli del gioco all'HTML
        model.addAttribute("gioco", rawgApiService.getGameDetails(id));
        // Simuliamo l'utente loggato
        Long idUtenteAttuale = 1L; 
        boolean inLibreria = videogiocoLibreriaService.esisteGiocoDaRawgInLibreria(idUtenteAttuale, id);
        // Passiamo la variabile "isInLibreria" all'HTML
        model.addAttribute("isInLibreria", inLibreria);
        return "rawg_dettagli";
    }
}
