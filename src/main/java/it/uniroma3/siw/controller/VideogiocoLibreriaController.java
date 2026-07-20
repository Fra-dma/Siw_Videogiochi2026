package it.uniroma3.siw.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.siw.model.Videogioco;
import it.uniroma3.siw.model.VideogiocoLibreria;
import it.uniroma3.siw.service.VideogiocoLibreriaService;

@Controller
public class VideogiocoLibreriaController {

    @Autowired
    private VideogiocoLibreriaService videogiocoLibreriaService;

    // Rotta che gestisce il salvataggio di un nuovo gioco nella libreria
    @PostMapping("/libreria/aggiungi")
    public String addVideogiocoLibreria(@ModelAttribute VideogiocoLibreria videogiocoLibreria) {
        videogiocoLibreriaService.save(videogiocoLibreria);
        return "redirect:/videogioco/" + videogiocoLibreria.getVideogioco().getId();
    }
    
    // Rotta per l'aggiunta di un gioco nella libreria
    @PostMapping("/libreria/aggiungiDaRawg")
    public String aggiungiGiocoDaRawg(@RequestParam("rawgId") Long rawgId, @RequestParam("idUtente") Long idUtente) {
        videogiocoLibreriaService.aggiungiDaRawgALibreria(idUtente, rawgId);
        return "redirect:/rawg/gioco/" + rawgId; 
    }
    
    //Rotta per recensire
    @PostMapping("/libreria/recensisci")
    public String recensisciGioco(@RequestParam("videogiocoId") Long videogiocoId, 
                                  @RequestParam("voto") Integer voto, 
                                  @RequestParam("commento") String commento) {
        Long idUtenteAttuale = 1L; 
        videogiocoLibreriaService.aggiornaVotoECommento(idUtenteAttuale, videogiocoId, voto, commento);
        
        return "redirect:/videogioco/" + videogiocoId; 
    }

    // Rotta per rimuovere recensione
    @PostMapping("/libreria/recensione/rimuovi")
    public String rimuoviRecensione(@RequestParam("videogiocoId") Long videogiocoId) {
        Long idUtenteAttuale = 1L; 
               videogiocoLibreriaService.aggiornaVotoECommento(idUtenteAttuale, videogiocoId, null, null);
        
        return "redirect:/videogioco/" + videogiocoId; 
    }
    
    // Rotta per rimuovere gioco da libreria
    @PostMapping("/libreria/rimuovi")
    public String rimuoviGiocoDaLibreria(@RequestParam("videogiocoId") Long videogiocoId, @RequestParam("idUtente") Long idUtente) {
        
        // Chiamiamo il service per rimuovere il gioco
        videogiocoLibreriaService.rimuoviDaLibreria(idUtente, videogiocoId);
        
        // Una volta rimosso, reindirizziamo l'utente alla vista generale della libreria
        return "redirect:/libreria"; 
    }
    
    // Rotta che mostra la pagina della libreria personale
    @GetMapping("/libreria")
    public String mostraLibreriaPersonale(Model model) {
        
        Long idUtenteAttuale = 1L; 
        
        List<Videogioco> videogiochiDellUtente = videogiocoLibreriaService.ottieniVideogiochiLibreriaUtente(idUtenteAttuale);                
        model.addAttribute("videogiochi", videogiochiDellUtente);
        return "libreria"; 
    }   
}