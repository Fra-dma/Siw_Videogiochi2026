package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.siw.service.RawgApiService;

@Controller
public class RawgController {

    @Autowired
    private RawgApiService rawgApiService;

    // Questa rotta restituisce la vista in cui React farà la ricerca dei giochi
    @GetMapping("/rawg/popolari")
    public String showPopularGames() {
        return "rawg_popolari"; // Restituisce la vista rawg_popolari.html
    }

    // Questa rotta mostra i dettagli di un singolo gioco preso da RAWG tramite il suo ID
    @GetMapping("/rawg/gioco/{id}")
    public String showGameDetails(@PathVariable("id") Long id, Model model) {
        model.addAttribute("gioco", rawgApiService.getGameDetails(id));
        return "rawg_dettagli"; // Restituisce la vista rawg_dettagli.html
    }
}
