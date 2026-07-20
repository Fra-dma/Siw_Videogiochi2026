package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import it.uniroma3.siw.model.Utente;
import it.uniroma3.siw.model.Videogioco;
import it.uniroma3.siw.model.VideogiocoLibreria;
import it.uniroma3.siw.repo.RepositoryUtente;
import it.uniroma3.siw.repo.RepositoryVideogiocoLibreria;
import it.uniroma3.siw.service.VideogiocoService;

@Controller
public class VideogiocoController {

    @Autowired
    private VideogiocoService videogiocoService;

    // Aggiungiamo i repository che ci servono per recuperare la recensione
    @Autowired
    private RepositoryUtente utenteRepository;

    @Autowired
    private RepositoryVideogiocoLibreria videogiocoLibreriaRepository;

    @GetMapping("/videogiochi")
    public String showVideogiochi(Model model) {
        model.addAttribute("videogiochi", videogiocoService.findAll());
        return "videogiochi"; 
    }

    @GetMapping("/videogioco/{id}")
    public String showVideogioco(@PathVariable Long id, Model model) {
        Videogioco videogioco = videogiocoService.findById(id).orElse(null);
        model.addAttribute("videogioco", videogioco);
        
        // Simuliamo l'utente 1, coerentemente con il resto del tuo codice
        Long idUtenteAttuale = 1L; 

        if (videogioco != null) {
            Utente utente = utenteRepository.findById(idUtenteAttuale).orElse(null);
            
            if (utente != null) {
                // Cerchiamo la recensione (il collegamento nella libreria) per questo utente e questo gioco
                VideogiocoLibreria recensione = videogiocoLibreriaRepository
                        .findByUtenteAndVideogioco(utente, videogioco)
                        .orElse(null);
                
                // Passiamo la recensione al model (l'HTML la userà con ${recensione})
                model.addAttribute("recensione", recensione);
            }
        }
        
        return "videogioco"; 
    } 
    
    @GetMapping("/")
    public String showHomePage() {
        return "redirect:/rawg/popolari"; 
    }
}